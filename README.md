# Todo Backend (PostgreSQL + MyBatis 版)

本项目是 Todo 应用的后端服务，采用了当前国内主流的 **Spring Boot + PostgreSQL + MyBatis** 架构。

该架构去除了复杂的 JPA/Hibernate 重量级 ORM 映射，回归 SQL 本质，利用 MyBatis 强大的动态 SQL 能力，完美适配 PostgreSQL 的高级特性（如 JSONB、全文检索等）。

---

## 📂 项目目录结构说明

本项目严格遵循经典的分层架构（Layered Architecture），各模块职责清晰，数据流向单向且可控。

```text
src/main/
├── java/com/example/learn/
│   ├── Main.java                 # 【启动入口】Spring Boot 项目的启动类，整个程序的引擎。
│   │
│   ├── common/                   # 【通用模块】存放贯穿整个项目的公共类。
│   │   └── Result.java           # -> 统一的 API 响应封装格式（如 {code: 200, data: ..., msg: "ok"}）。
│   │
│   ├── controller/               # 【控制层/表现层】负责对外暴露 API 接口。
│   │   ├── TodoController.java   # -> 接收 HTTP 请求（GET/POST），参数校验，调用 Service。
│   │   └── TodoMarkController.java
│   │
│   ├── entity/                   # 【实体层/模型层】数据在 Java 里的纯粹化身 (POJO)。
│   │   ├── Todo.java             # -> 不含任何数据库映射注解，干净的数据承载对象。
│   │   ├── TodoMark.java
│   │   └── TodoMarkRelation.java
│   │
│   ├── mapper/                   # 【数据访问层/持久层】MyBatis 的核心接口。
│   │   ├── TodoMapper.java       # -> 只定义方法（如 findAll），无具体实现。与 XML 文件绑定。
│   │   ├── TodoMarkMapper.java
│   │   └── TodoMarkRelationMapper.java
│   │
│   └── service/                  # 【业务逻辑层】程序的大脑，处理核心业务规则。
│       ├── TodoService.java      # -> 接口定义。
│       ├── TodoMarkService.java
│       │
│       └── impl/                 # 【业务实现层】具体的业务逻辑代码。
│           ├── TodoServiceImpl.java     # -> 组合调用多个 Mapper 完成复杂事务，如“创建待办并绑定标签”。
│           └── TodoMarkServiceImpl.java
│
└── resources/                    # 【资源配置目录】配置文件与静态资源。
    ├── application.yml           # -> 核心配置。包含 PostgreSQL 数据库连接池、端口号、MyBatis 日志开关等。
    │
    ├── schema.sql                # -> 数据库建表初始化脚本。在项目启动时自动执行，建立表结构。
    │
    └── mapper/                   # 【MyBatis SQL 映射文件】灵魂所在。
        ├── TodoMapper.xml        # -> 编写纯正的 PostgreSQL 语句和强大的 <if>、<foreach> 动态 SQL。
        ├── TodoMarkMapper.xml
        └── TodoMarkRelationMapper.xml
```

---

## 🔄 核心数据流向示例

以 **“获取所有待办事项”** 为例，数据在各层之间的流转过程如下：

1. **用户/前端**：发送 `GET /todos` 请求。
2. **Controller**：`TodoController` 接收请求，调用 `TodoService.findAll()`。
3. **Service**：`TodoServiceImpl` 处理业务逻辑，并调用底层的 `TodoMapper.findAll()`。
4. **Mapper (接口)**：MyBatis 拦截该调用，去对应的 XML 文件寻找 SQL。
5. **XML (SQL)**：在 `TodoMapper.xml` 中找到 `SELECT * FROM todo` 并发给 PostgreSQL 数据库。
6. **Entity**：数据库返回结果集，MyBatis 自动将数据行映射为一个个 `Todo` 对象 (Entity)。
7. **返回闭环**：`List<Todo>` 一层层向上传递（Mapper -> Service -> Controller），最终由 Controller 包装成 JSON 返回给前端。

---

## 🚀 快速启动指南

### 1. 准备数据库

确保本地已安装 PostgreSQL 数据库，并创建一个名为 `todo_db` 的空数据库。
您可以使用 Docker 快速启动一个环境：

```bash
docker run --name pg -e POSTGRES_PASSWORD=postgres_password -d -p 5432:5432 postgres
```

### 2. 检查配置

打开 `src/main/resources/application.yml`，核对数据库连接信息是否正确：

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/todo_db
    username: postgres
    password: postgres_password
```

### 3. 运行项目

直接在 IDE 中运行 `Main.java`，或使用 Maven 命令行：

```bash
JAVA_HOME=/usr/lib/jvm/java-25-openjdk-amd64 mvn spring-boot:run
```

_注：项目首次启动时，会自动执行 `schema.sql` 完成数据表的创建。_

### 4. 调试 backend-mb

1. 确认 PostgreSQL 已启动。在 WSL 中进入 `backend-mb` 目录。如果普通启动的后端仍占用 `8090`，先在原终端按 `Ctrl+C` 停止，再运行以下命令。

   ```bash
   JAVA_HOME=/usr/lib/jvm/java-25-openjdk-amd64 mvn spring-boot:run \
     -Dspring-boot.run.jvmArguments="-agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=127.0.0.1:5005"
   ```

2. 看到 `Listening for transport dt_socket at address: 5005` 后，在 IDEA 顶部选择 **backend-mb Attach (5005)** 并点击 **调试**。如果列表里没有这个配置，可通过 **运行 → 编辑配置 → 新增远程 JVM 调试** 创建，选择 **附加到远程 JVM**，主机填 `127.0.0.1`，端口填 `5005`。
3. `suspend=y` 会让应用等到调试器连接后再继续启动。要调试启动流程，在 `Main.main()` 中打断点。要调试接口，等应用启动后，在 Controller 中打断点并请求 `http://localhost:8090/api/todos`。

应用日志和 `System.out.println` 会显示在启动 Maven 的 WSL 终端。IDEA 中重新运行附加配置只会重连调试器。要再次命中启动断点，先停止 WSL 中的 Maven 进程，再重复上面的步骤。

---

## 💡 为什么选择 PostgreSQL + MyBatis？

相较于传统的 JPA，本架构赋予了开发者对 SQL **100% 的掌控力**：

- **无痛动态查询**：通过 MyBatis 的 XML 标签，轻松应对多条件搜索、复杂组合查询，告别 JPA `@Query` 中难看的静态字符串拼接。
- **释放 PG 潜力**：能够毫无阻碍地使用 PostgreSQL 的高级语法（如 `JSONB` 字段操作 `->>`、数组类型、`tsvector` 全文检索等），让数据库不仅仅是一个存储容器。
- **解耦的实体类**：实体类不再被 `@Entity`、`@Column` 等数据库物理结构强行绑定，回归纯粹的领域对象。
