会议管理系统
项目简介
本项目是一个基于Vue3+Spring Boot的前后端分离会议管理系统，采用现代化技术栈开发。前端使用Vue3+Element Plus+Pinia构建，后端采用Spring Boot+MyBatis框架，数据库选用国产金仓数据库(Kingbase8)。系统提供完整的会议全生命周期管理功能，包括会议创建、审核、展示、议程管理等核心模块，适用于各类组织的会议管理需求。

技术栈
前端技术
Vue 3.0 - 前端核心框架

Element Plus - UI组件库

Pinia - 状态管理

Vue Router - 路由管理

Axios - HTTP请求库

ECharts - 数据可视化

Vite - 构建工具

后端技术
Spring Boot 3.4.7 - 后端框架

MyBatis 3.0.4 - ORM框架

Kingbase8 JDBC - 数据库驱动

Spring Security - 安全认证

Maven - 依赖管理

数据库
金仓数据库Kingbase8

项目结构
text
meeting-management-system/
├── frontend/               # 前端项目
│   ├── public/             # 静态资源
│   ├── src/                # 源代码
│   │   ├── api/            # API请求封装
│   │   ├── assets/         # 静态资源
│   │   ├── components/     # 公共组件
│   │   ├── router/         # 路由配置
│   │   ├── stores/         # Pinia状态管理
│   │   ├── styles/         # 全局样式
│   │   ├── utils/          # 工具函数
│   │   ├── views/          # 页面组件
│   │   ├── App.vue         # 根组件
│   │   └── main.js         # 入口文件
│   ├── vite.config.js      # Vite配置
│   └── package.json        # 前端依赖
│
├── backend/                # 后端项目
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/       # Java源代码
│   │   │   │   ├── config/ # 配置类
│   │   │   │   ├── controller/ # 控制器
│   │   │   │   ├── entity/ # 实体类
│   │   │   │   ├── mapper/ # MyBatis映射
│   │   │   │   ├── service/ # 服务层
│   │   │   │   └── util/   # 工具类
│   │   │   └── resources/  # 资源文件
│   │   │       ├── mapper/ # MyBatis XML
│   │   │       ├── static/ # 静态资源
│   │   │       ├── templates/ # 模板文件
│   │   │       └── application.properties # 应用配置
│   │   └── test/           # 测试代码
│   └── pom.xml             # Maven配置
│
├── docs/                   # 项目文档
│   ├── deployment.md       # 部署指南
│   ├── api-docs.md         # API文档
│   ├── database.md         # 数据库设计
│   └── development.md      # 开发指南
│
├── .gitignore             # Git忽略配置
└── README.md              # 项目说明
开发环境搭建
前端环境配置
安装Node.js (推荐v16.x或v18.x LTS版本)

安装pnpm (推荐)或npm:

bash
npm install -g pnpm
安装依赖:

bash
cd frontend
pnpm install
后端环境配置
安装JDK 17

安装Maven 3.8+

安装金仓数据库Kingbase8

创建数据库并导入初始化SQL脚本

修改后端配置:

properties
# application.properties
spring.datasource.url=jdbc:kingbase8://localhost:54321/meeting_db
spring.datasource.username=your_username
spring.datasource.password=your_password
运行项目
前端开发模式
bash
cd frontend
pnpm dev
后端开发模式
bash
cd backend
mvn spring-boot:run
代码规范
前端规范
组件使用组合式API写法

组件文件使用PascalCase命名

方法/变量使用camelCase命名

常量使用UPPER_CASE命名

CSS类名使用kebab-case命名

重要组件必须包含TypeScript类型定义

后端规范
遵循Java编码规范

使用Lombok简化代码

统一异常处理

接口返回统一封装

重要方法必须包含JavaDoc注释

提交规范
我们遵循Conventional Commits规范，提交信息格式为：

text
<type>(<scope>): <subject>
常用type类型：

feat: 新功能

fix: bug修复

docs: 文档更新

style: 代码格式化

refactor: 代码重构

test: 测试相关

chore: 构建过程或辅助工具变动

示例：

text
git commit -m "feat(meeting): 添加会议创建功能"
git commit -m "fix(auth): 修复登录验证问题"
分支管理
main分支 - 生产环境代码

develop分支 - 开发主分支

feature/xxx分支 - 功能开发分支

hotfix/xxx分支 - 紧急修复分支

开发流程：

从develop分支创建feature分支

开发完成后发起Pull Request

通过代码审查后合并到develop分支

定期将develop分支合并到main分支

项目文档
完整文档请查看docs目录：

部署指南 - 生产环境部署说明

API接口文档 - 后端API接口规范

数据库设计 - 数据库表结构说明

开发指南 - 开发规范与最佳实践

贡献指南
欢迎贡献代码，请确保：

在GitHub上fork本项目

从develop分支创建你的特性分支

提交清晰的commit信息

编写适当的单元测试

更新相关文档

发起Pull Request并描述变更内容

许可证
本项目采用MIT开源许可证。详情请查看LICENSE文件。
