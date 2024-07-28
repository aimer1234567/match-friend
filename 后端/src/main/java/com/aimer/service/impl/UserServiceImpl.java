package com.aimer.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.aimer.commen.BaseContext;
import com.aimer.commen.myException;
import com.aimer.commen.validator.GenderValidator;
import com.aimer.commen.validator.PhoneValidator;
import com.aimer.commen.validator.UsernameValidator;
import com.aimer.commen.validator.WchatValidator;
import com.aimer.constant.ExceptionMessage;
import com.aimer.constant.JWTConstant;
import com.aimer.funation.UserSort;
import com.aimer.mapper.UserMapper;
import com.aimer.pojo.VO.UserDetailVO;
import com.aimer.pojo.VO.UserVO;
import com.aimer.pojo.dto.RegisterDTO;
import com.aimer.pojo.entity.User;
import com.aimer.pojo.entity.UserSoredByAddress;
import com.aimer.pojo.entity.UsersSorted;
import com.aimer.pojo.request.LoginRequest;
import com.aimer.pojo.request.UpdateUserRequest;
import com.aimer.service.UserService;
import com.aimer.utils.AddressCoding;
import com.aimer.utils.OSS;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Year;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    @Resource
    UserMapper userMapper;
    private final static String SALT="luoxiaoyong";
    private final static int OFFSET=10000;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 注册
     * @param registerDTO
     */
    @Override
    @Transactional
    public void register(RegisterDTO registerDTO) {
        String username = registerDTO.getUsername();
        String userAccount = registerDTO.getUserAccount();
        String userPassword = registerDTO.getUserPassword();
        String checkPassword = registerDTO.getCheckPassword();
        if(StringUtils.isAnyBlank(userAccount,userPassword,username,checkPassword)){
            throw new myException(ExceptionMessage.PARAMS_ERROR);
        }
        //检查账号有没有特殊字符
        String validPattern = "[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if (matcher.find()) {
            throw  new myException(ExceptionMessage.ACCOUNT_ERROR);
        }
        //账号不能重复
        if(userMapper.selectByAccount(userAccount)>=1){
            throw new myException(ExceptionMessage.ACCOUNT_EXIST);
        }
        //校验密码
        if(!checkPassword.equals(userPassword)){
            throw new myException(ExceptionMessage.CHECK_ERROR);
        }
        //加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        registerDTO.setUserPassword(encryptPassword);
        userMapper.insertUser(registerDTO);
    }


    /**
     * 登录
     * @param loginRequest
     */
    @Override
    public String login(LoginRequest loginRequest, HttpServletResponse response) {
        String userPassword = loginRequest.getUserPassword();
        String userAccount = loginRequest.getUserAccount();
        if(StringUtils.isAnyBlank(userAccount,userPassword)){
            throw new myException(ExceptionMessage.PARAMS_ERROR);
        }
        //密码加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        loginRequest.setUserPassword(encryptPassword);
        User user = userMapper.selectUserByAccount(userAccount);
        //账号是否存在
        if(user==null) {
            throw new myException(ExceptionMessage.ACCOUNT_IS_EXIST);
            //若存在，密码是否正确
        }else if(!loginRequest.getUserPassword().equals(user.getUserPassword())){
            throw new myException(ExceptionMessage.PASSWORD_ERROR);
            //账号是否被冻结
        }else if(user.getUserStatus()==1){
            throw new myException(ExceptionMessage.ACCOUNT_FROZEN);
        }
        // 获取当前时间
        Date currentDate = new Date();
        // 创建 Calendar 对象，并将当前时间设置为其值
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        // 将日期加上三天
        calendar.add(Calendar.DAY_OF_MONTH, 3);
        // 获取三天后的时间
        Date threeDaysLater = calendar.getTime();
        Algorithm algorithm = Algorithm.HMAC256("secret");
        String token = JWT.create()
                .withExpiresAt(threeDaysLater)
                .withClaim(JWTConstant.USERID, user.getId())
                .sign(algorithm);
        return token;
    }

    /**
     * 获取用户基本信息
     * @return
     */

    @Override
    public User getUserInformation() {
        Long id = BaseContext.getId();
        return userMapper.getUserInformation(id);
    }

    /**
     * 修改用户基本信息
     */

    @Override
    public void updateUserInformation(UpdateUserRequest updateUserRequest) {
        Long id = BaseContext.getId();
        User user = new User();
        String value = updateUserRequest.getValue();
        user.setId(id);
        switch (updateUserRequest.getKey()) {
            case "username" -> {
                log.info("username:"+value);
                UsernameValidator.validate(value);
                user.setUsername(value);
            }
            case "gender" -> {
                GenderValidator.validate(Integer.parseInt(value));
                user.setGender(Integer.parseInt(value));
            }
            case "phone" -> {
                PhoneValidator.validate(value);
                user.setPhone(value);
            }
            case "wchat" -> {
                WchatValidator.validate(value);
                user.setWchat(value);
            }
            case "address"->{
                if(value==null){
                    throw new myException(ExceptionMessage.EXIST_NULL);
                }
                List<BigDecimal> longitudeAndDimension = AddressCoding.parse(value);
                user.setAddress(value);
                user.setLongitude(longitudeAndDimension.get(0));
                user.setDimension(longitudeAndDimension.get(1));
            }
            case "age"->{
                user.setAge(Year.parse(value));
            }
            case "tags" -> user.setTags(value);
            case "introduction" -> {
                if(value.equals("")){
                    user.setIntroduction("无");
                }else {
                    user.setIntroduction(value);
                }
            }
            case "problem" ->{
                if(value.equals("")){
                    user.setProblem("无");
                }else {
                    user.setProblem(value);
                }
            }
            default -> throw new myException(ExceptionMessage.PARAMS_ERROR);
        }
        userMapper.updateUserInformation(user);
    }

    /**
     * 通过标签筛选用户
     * @param tagNameList
     * @return
     */
    @Override
    public List<UserVO> searchUsersByTagsInitial(List<String> tagNameList) {
        Long id = BaseContext.getId();
        Integer userConut = userMapper.selectUserConut(id);
        List<UserVO> userList;
        //生成随机数,偏移量不能超过总量
        if(userConut<OFFSET){
            userList = userMapper.selectList(0,userConut,id);
        }else {
            int randomIndex = RandomUtil.randomInt(0, userConut - OFFSET+1);
            //随机取去出一个区间的所有用户
            userList = userMapper.selectList(randomIndex,OFFSET,id);
        }
        //如果筛选条件标签为空则返回全部
        if (CollectionUtils.isEmpty(tagNameList)){
            return userList;
        }
        Gson gson = new Gson();
        // 2. 在内存中判断是否包含要求的标签
        return userList.parallelStream().filter(user -> {
            String tagsStr = user.getTags();
            Set<String> tempTagNameSet = gson.fromJson(tagsStr, new TypeToken<Set<String>>() {
            }.getType());
            tempTagNameSet = Optional.ofNullable(tempTagNameSet).orElse(new HashSet<>());
            for (String tagName : tagNameList) {
                if (!tempTagNameSet.contains(tagName)) {
                    return false;
                }
            }
            return true;
        }).collect(Collectors.toList());
    }

    /**
     * 通过匹配度进行排序
     * @param userList
     * @return
     */
    @Override
    public List<UserVO> sortBySimilar(List<UserVO> userList){
        Long id = BaseContext.getId();
        Gson gson = new Gson();
        String userTags = userMapper.getUserTags(id);
        //当用户自己的标签为空时
        if(userTags==null){
            userTags="[]";
        }
        Set<String> tagNameSet=gson.fromJson(userTags,new TypeToken<Set<String>>() {
        }.getType());
        List<UserVO> list = userList
                .parallelStream()
                .map((user) -> {
                    Set<String> userTagNameSet = gson.fromJson(user.getTags(), new TypeToken<Set<String>>() {
                    }.getType());
                    userTagNameSet = Optional.ofNullable(userTagNameSet).orElse(new HashSet<>());
                    int num = 0;
                    for (String tagName : tagNameSet) {
                        if (userTagNameSet.contains(tagName)) {
                            num++;
                        }
                    }
                    return UsersSorted.builder().user(user).num(num).build();
                })
                .sorted().map(UsersSorted::getUser).collect(Collectors.toList());
        return list;
    }

    /**
     * 对搜索结果进行年龄排序
     * @param userList
     * @return
     */
    @Override
    public List<UserVO> sortByAge(List<UserVO> userList){
        Long id = BaseContext.getId();
        Year age = userMapper.getUserInformation(id).getAge();
        if(age==null){
            throw new myException(ExceptionMessage.AGE_EMPTY);
        }
        return userList
                .stream()
                .map(user->{
                    int sortNum;
                    if(user.getAge()==null){ //如果年龄为空，则为年龄差最大，排在表中最后
                        sortNum=999;
                    }else {
                        sortNum=Math.abs(age.getValue()-user.getAge().getValue());
                    }
                    return UsersSorted.builder().user(user).num(sortNum).build();
                })
                .sorted(Comparator.reverseOrder()).map(UsersSorted::getUser)
                .collect(Collectors.toList());
    }

    /**
     * 对搜索结果进行位置排序
     * @param userList
     * @return
     */
    @Override
    public List<UserVO> sortByAddress(List<UserVO> userList){
        Long id = BaseContext.getId();
        BigDecimal userLongitude = userMapper.getUserInformation(id).getLongitude();
        BigDecimal userDimension = userMapper.getUserInformation(id).getDimension();
        if(userDimension==null||userLongitude==null){  //如果用户自己的经纬度为空，则提示填写自己的位置信息
            throw new myException(ExceptionMessage.ADDRESS_EMPTY);
        }
        return userList.stream()
                .map(user->{
                    BigDecimal longitude = user.getLongitude();
                    BigDecimal dimension = user.getDimension();
                    BigDecimal distance;
                    if(longitude==null||dimension==null){  //如果经纬度为空，则为距离差最大，排在表中最后
                        distance=BigDecimal.valueOf(9999999999L);
                    }else {
                        BigDecimal distance1 = longitude.subtract(userLongitude);
                        BigDecimal distance2 = dimension.subtract(userDimension);
                        distance = distance1.multiply(distance1).add(distance2.multiply(distance2));
                    }
                    return UserSoredByAddress.builder().user(user).distance(distance).build();
                })
                    .sorted()
                    .map(UserSoredByAddress::getUser)
                    .collect(Collectors.toList());
    }

    /**
     * 对排序后的结果取出前几个
     * @param tagNameList
     * @param pageSize
     * @return
     */
    @Override
    public List<UserVO> searchUsersByTags(List<String> tagNameList, Integer pageSize, UserSort userSort) {
        List<UserVO> list = searchUsersByTagsInitial(tagNameList);//可能返回一个空列表，如果查询的标签不被满足
        log.info("筛选时间："+LocalDateTime.now());
        //函数式编程，对查询出的user列表再次进行排序
        List<UserVO> userList = userSort.sort(list);
        log.info("排序时间："+LocalDateTime.now());
        if(pageSize>userList.size()){
            return userList.subList(0,userList.size());
        }else {
            return userList.subList(0,pageSize);
        }
    }

    /**
     * 对searchUsersByTags再封装一次，根据排序方法的不同，调用不同的函数实现
     * @param tagNameList
     * @param pageSize
     * @param sortMethod 排序方法
     * @return
     */
    @Override
    public List<UserVO> searchUsersByTagsSort(List<String> tagNameList, Integer pageSize, String sortMethod){
        List<UserVO> userVOList = null;
        switch (sortMethod){
            case "similarSort"->{
                userVOList = searchUsersByTags(tagNameList, pageSize, this::sortBySimilar);
            }
            case "ageSort"->{
                userVOList = searchUsersByTags(tagNameList, pageSize,this::sortByAge);
            }
            case "addressSort"->{
                userVOList = searchUsersByTags(tagNameList, pageSize,this::sortByAddress);
            }
        }
        return userVOList;
    }

    /**
     * 更新用户头像
     * @param file
     * @throws IOException
     */

    @Override
    public void updateAvatarUrl(MultipartFile file) throws IOException {
        Long id = BaseContext.getId();
        OSS oss = new OSS();
        String userAccount = userMapper.getUserInformation(id).getUserAccount();
        String fileName=userAccount+"-avatarUrl";
        oss.deleteFile(fileName);
        oss.putFile(file.getBytes(),fileName);
        String fileUrl = oss.getFile(fileName);
        userMapper.updateAvatarUrl(fileUrl,id);
    }

    /**
     * 获取用户详情信息
     * @param userAccount
     * @return
     */

    @Override
    public UserDetailVO getUserDetailVOByUserAccount(String userAccount) {
        return userMapper.getUserDetailVOByUserAccount(userAccount);
    }

}
