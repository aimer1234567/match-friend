package com.aimer.utils;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
//TODO 重复上传图片会报错，文件名的问题
/**
 * 对七云牛SDK的封装
 */
public class OSS {
    String accessKey = "efm3TJYLLt56anjO8pBKWis6WLKUoe5eYJ8cvzV2";
    String secretKey = "sdorDexQkT2QLzcbCGPx8792w84Gj-AccwkH2_EO";
    String domainOfBucket = "http://scg133wxc.hd-bkt.clouddn.com";
    String bucket = "friendmatch";

    public String putFile(byte[] uploadBytes,String key){
        //构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Region.region0());
        cfg.resumableUploadAPIVersion = Configuration.ResumableUploadAPIVersion.V2;// 指定分片上传版本
//...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
//...生成上传凭证，然后准备上传
        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket,key);
        String fileName = null;
        try {
            Response response = uploadManager.put(uploadBytes, key, upToken);
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            fileName=putRet.key;
        } catch (QiniuException ex) {
            ex.printStackTrace();
            if (ex.response != null) {
                System.err.println(ex.response);
                try {
                    String body = ex.response.toString();
                    System.err.println(body);
                } catch (Exception ignored) {
                }
            }
        }
        return fileName;
    }
    public String getFile(String fileName) throws UnsupportedEncodingException {
        String encodedFileName = URLEncoder.encode(fileName, "utf-8").replace("+", "%20");
        String publicUrl = String.format("%s/%s", domainOfBucket, encodedFileName);
        Auth auth = Auth.create(accessKey, secretKey);
        String finalUrl = auth.privateDownloadUrl(publicUrl);
        System.out.println(finalUrl);
        return finalUrl;
    }
    public boolean deleteFile(String fileName) {
        Configuration configuration = new Configuration(Region.region0());
        Auth auth = Auth.create(accessKey, secretKey);
        BucketManager bucketManager = new BucketManager(auth, configuration);
        try {
            if (fileName != null) {
                bucketManager.delete(bucket, fileName);
                return true;
            }
        } catch (QiniuException ex) {
            return false;
        }
        return false;
    }
}
