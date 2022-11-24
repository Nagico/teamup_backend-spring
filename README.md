<div align="center">

# 赛道友你 后端项目

<!-- markdownlint-disable-next-line MD036 -->
_✨ Author: [Nagico](https://github.com/Nagico/) ✨_
</div>

<p align="center">
  <img src="https://img.shields.io/badge/LICENSE-AGPLv3-red" alt="license">
  <a href="stargazers">
    <img src="https://img.shields.io/github/stars/Nagico/teamup_backend?color=yellow&label=Github%20Stars" alt="star">
  </a>
  <br />
  <img src="https://img.shields.io/badge/Java-17-red" alt="java">
  <img src="https://img.shields.io/badge/Kotlin-1.7-purple" alt="kotlin">
  <img src="https://img.shields.io/badge/Springboot-2.7.5-greeaen" alt="springboot">
  <br />
  <a href="https://github.com/LSX-s-Software/teamup">
    <img src="https://img.shields.io/badge/Github-general-brightgreen?logo=github" alt="wxapp_repository">
  </a>
  <a href="">
    <img src="https://img.shields.io/badge/Github-app-brightgreen?logo=github" alt="frontend_repository">
  </a>
  <a href="https://github.com/Nagico/teamup_backend/">
    <img src="https://img.shields.io/badge/Github-backend-brightgreen?logo=github" alt="backend_repository">
  </a>
  <br />
  <a href="https://github.com/Nagico/teamup_backend/actions/workflows/test-self.yml">
    <img src="https://github.com/Nagico/teamup_backend/actions/workflows/test-self.yml/badge.svg?branch=main" alt="Test Server Self-Host CI/CD">
  </a>
</p>
<!-- markdownlint-enable MD033 -->

## 项目地址

- 测试环境: [api.teamup.nagico.cn](https://api.teamup.nagico.cn/)

### API文档

- Postman: [Postman Workspace](https://elements.getpostman.com/redirect?entityId=2940417-875ace31-5a58-43e6-b5ac-3b54c8e73231&entityType=collection)

- Swagger: [Swagger API](https://api.teamup.nagico.cn/swagger-ui/index.html)

## 技术栈

- Springboot
- JPA
- MySQL
- Redis
- Gradle Kotlin

## 项目模块

- teamup-common 公共模块，包括实体类、基础信息等
- teamup-dao 持久层
- teamup-service 业务层
- teamup-web 视图控制层，包括鉴权、异常处理、响应包装等


## 部署

### 手动部署

在docker文件夹下有相关部署文件，请使用`.env.template`创建`.env`文件，填入相关信息。

```ini
HTTP_PORT=服务暴露端口

JWT_SECRET=JWT密钥

MYSQL_HOST=MySQL数据库地址
MYSQL_USER=MySQL数据库用户名
MYSQL_PASSWORD=MySQL数据库密码

REDIS_HOST=10.10.10.2 # Redis数据库地址，使用docker-compose需与redis容器ip一致

WECHAT_APPID=微信小程序APPID
WECHAT_SECRET=微信小程序密钥
```

并将`.env`文件与`docker-compose.yml`（prod环境）或`docker-compose.yml`（test环境）放于同一目录下。

执行`docker-compose up -d`即可启动服务。

## CI/CD

测试环境对应`main`分支，使用`test.yml`文件。

生产环境对应`production`分支，使用`prod.yml`文件。

### yml文件变量

在`.github/workflows/.*yml`下有`jobs->define_env->env`一项列表，可以配置相关属性：

```ini
  BASE_NAME=镜像名
  DOCKER_HOST=Docker镜像仓库地址
  DOCKER_NAMESPACE=镜像仓库命名空间
  DOCKER_COMPOSE_FILE=docker-compose yml文件名
```

### Secrets变量

需要在Github Secrets中设置Actions所需的环境变量：

```ini
DOCKER_USERNAME=Docker镜像仓库用户名
DOCKER_PASSWORD=Docker镜像仓库密码

TEST_SSH_HOST=测试服务器地址
TEST_SSH_USERNAME=测试服务器用户名
TEST_SSH_PASSWORD=测试服务器密码

SSH_HOST=生产服务器地址
SSH_USERNAME=生产服务器用户名
SSH_PASSWORD=生产服务器密码
```