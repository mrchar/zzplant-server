# ZZPLANT-SERVER 发财树后端服务

这个项目是一款简易记账软件的后台程序，使用RESTFUL接口实现用户管理、店铺管理、商品管理、记账管理的功能。

## 技术栈

* SpringBoot3
* SpringWeb
* SpringDataJPA
* SpringSecurity

## 快速开始

克隆项目

```shell
git clone https://github.com/mrchar/zzplant-server.git
```

启动项目

```shell
gradle bootRun
```

构建项目

```shell
gradle bootJar
```

## 项目结构

* gradle gradle wrapper 文件
* src 源码
    * main
        * net.mrchar.zzplant 主包
            * config Spring 配置
            * controller
            * exception 错误定义
            * model
            * repository
            * service 服务定义
                * impl 服务实现
            * ZzplantApplication 程序入口
            * resources 资源文件
                * db.changelog 数据库迁移定义文件
                * application* properties文件
    * test 测试程序
* .gitignore git配置
* build.gradle 项目定义
* gradlew* gradle wrapper 入口
* README.md 自述文档
* setting.gradle 项目配置

## 项目规划

```catalpa
计划中:

开发中:
账户管理
    注册邀请码 #v0.2.0
    短信验证码 #v0.2.0
    修改账号密码 #v0.2.0

已完成:
账户管理
    注册登录 #v0.1.0
商铺管理
    创建商铺 #v0.1.0
商品管理
    创建商品 #v0.1.0
会员管理
    添加会员 #v0.1.0
订单管理
    创建订单 #v0.1.0
    删除订单 #v0.1.0
    确认订单 #v0.1.0
```