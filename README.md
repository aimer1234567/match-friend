# 介绍
项目描述：一个基于Vue3+SpringBoot3的H5移动端交友匹配系统，用户选择符合自己兴趣爱好
的标签，即可以相互匹配。通过标签搜索感兴趣的用户，并可以根据相似度优先，同龄人优先，
附近的人优先，更精确地匹配与自己志同道合的人。匹配后，发送申请，获取联系方式。
---
技术栈：SpringBoot、Mybatis、MySQL、Vue、Vant、SSE、JWT、七牛云对象存储


技术实现：

- 使用JWT登录及身份验证，自定义拦截器实现用户认证，并通过ThreadLocal进行鉴权优化。
- 使用Stream并行流，实现通过标签对用户进行筛选，并进行三种优先级的排序。提高运算效
率。 通过函数式编程优化代码结构，提高可维护和可扩展性。
通过 SSE 连接实现实时信息提示，及申请验证，申请同意或拒绝的信息提示。
- 使用百度地图API，获取用户位置的地理信息。使用七牛云对象存储服务，对用户上传的图片
进行存储。