# Spring AI  Demo (Spring AI 应用落地示例)

[![Java Version](https://img.shields.io/badge/Java-17%20%2F%2021-oracle.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2%20%2F%203.3-green.svg)](https://spring.io/projects/spring-boot)
[![Spring AI](https://img.shields.io/badge/Spring%20AI-1.0.0--M1-blue.svg)](https://spring.io/projects/spring-ai)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](LICENSE)

## 📌 项目简介

`spring-ai-demo` 是一个基于 **Spring Boot 3.x** 和 **Spring AI** 框架构建的企业级 AI 应用落地示例项目。本项目旨在帮助 Java 开发者快速掌握如何将大语言模型（LLM）、向量数据库（Vector Store）、检索增强生成（RAG）以及工具回调（Function Calling）等前沿 AI 技术无缝整合到传统的 Java 企业级架构中。

通过本项目，您可以学习到如何不依赖 Python 生态（如 LangChain/LlamaIndex），纯原生使用 Java 构建高性能、高可扩展性的 **AI Agent** 和智能业务系统。

---

## 🚀 核心特性

- **🔌 多模型生态适配**：原生支持主流闭源大模型（OpenAI、Anthropic）以及国内优秀大模型（**DeepSeek**、智谱清言、通义千问），并完美兼容基于 **Ollama** 本地部署的开源大模型（Llama 3、Qwen2、Mistral）。
- **💬 智能对话与流式响应**：基于 `ChatClient` 响应式流（Reactive Stream）架构，支持 `Flux<ChatResponse>` 低延迟流式文本输出，提供丝滑的用户打字机体验。
- **📋 结构化输出 (Structured Outputs)**：解决大模型输出不可控的痛点。通过 `BeanOutputConverter` 和 `MapOutputConverter`，强制大模型将回答解析为标准的 Java POJO 对象或 JSON 映射，便于下游微服务直接消费。
- **🛠️ 工具回调 (Function Calling)**：赋予大模型“执行动作”的能力。通过 Spring 机制将本地 Java Service 注册为 LLM 工具，使大模型能动态感知并调用外部 API（如实时天气、物流查询、数据库检索）。
- **📚 高级 RAG (检索增强生成)**：
  - 支持多种文档解析（PDF、Word、Markdown、JSON）。
  - 基于 `TokenTextSplitter` 的智能文本切片与重叠度控制。
  - 集成高性能向量数据库（Pgvector / Milvus / Chroma），实现本地企业私有知识库的精准检索与问答。
- **🧠 记忆与上下文管理**：内置基于内存及 Redis 的会话历史管理（Chat Memory），完美支持多轮复杂对话。

---

## 🛠️ 技术栈

| 技术组件 | 版本 | 说明 |
| :--- | :--- | :--- |
| **JDK** | 21 | 核心开发环境，充分利用 Record 等新特性 |
| **Spring Boot** | 3.4.x | 基础微服务框架 |
| **Spring AI** | 1.0.0-M1+ | AI 能力核心抽象层 |
| **Vector Database** | Pgvector | 向量数据存储与嵌入 |
| **Docker** | 20.10+ | 用于快速本地化部署向量库与 Ollama |
| **Build Tool** | Maven 3.9+ | 项目构建与依赖管理 |

---

## 📁 项目结构

```text
spring-ai-demo
├── src/main/java/com/shizixuan/ai
│   ├── config             # AI 客户端及多模型流控配置
│   ├── controller         # RESTful API 暴露层（Chat、RAG、Tools）
│   ├── domain             # POJO 实体、结构化输出定义对象
│   ├── functions          # Function Calling 自定义工具函数 (注册为 Bean)
│   ├── service            # 核心业务逻辑层
│   │   ├── ChatService.java     # 基础对话与流式调用服务
│   │   └── RagService.java      # 知识库向量化、检索与 RAG 管道
│   └── SpringAiDemoApplication.java  # 启动类
└── src/main/resources
    ├── application.yml    # 全局主配置文件
    └── documents/         # RAG 私有知识库预置原始文档 (PDF/MD)

```

---

## 🔑 快速开始

### 1. 克隆项目

```bash
git clone [https://github.com/shizixuan-ai/spring-ai-demo.git](https://github.com/shizixuan-ai/spring-ai-demo.git)
cd spring-ai-demo

```

### 2. 环境配置

根据您的业务场景，在 `src/main/resources/application.yml` 中配置 AI 供应商凭证（二选一即可）：

#### 💡 方案 A：在线大模型（以 DeepSeek / OpenAI 为例）

```yaml
spring:
  ai:
    openai:
      api-key: ${OPENAI_API_KEY:your_actual_api_key_here}
      base-url: [https://api.deepseek.com](https://api.deepseek.com) # 若使用 DeepSeek，填入官方 API 地址
      chat:
        options:
          model: deepseek-chat # 亦可替换为 gpt-4o
          temperature: 0.7

```

#### 💡 方案 B：本地大模型（基于 Ollama 离线运行）

1. 在本地终端启动 Ollama 并拉取目标模型：
```bash
ollama run qwen2.5:7b

```


2. 修改 `application.yml` 激活 Ollama 适配器：
```yaml
spring:
  ai:
    ollama:
      base-url: http://localhost:11434
      chat:
        options:
          model: qwen2.5:7b
          temperature: 0.5

```



### 3. 编译与启动

```bash
mvn clean package
java -jar target/spring-ai-demo-0.0.1-SNAPSHOT.jar

```

---

## 💡 典型应用场景示例 (API Endpoints)

### 1. 基础流式对话 (Chat Stream)

* **Endpoint**: `GET /api/v1/ai/chat/stream?message=你好，请介绍一下你自己`
* **响应类型**: `text/event-stream`
* > **说明**：支持标准的 SSE 协议，模拟类似 ChatGPT 的逐字打字机输出响应，降低首字延迟。



### 2. 结构化输出转化 (Structured Data)

* **Endpoint**: `GET /api/v1/ai/generate/actor?actorName=周星驰`
* **期望返回 (JSON)**:
```json
{
  "name": "周星驰",
  "birthPlace": "香港",
  "representativeWorks": ["大话西游", "功夫", "少林足球"],
  "achievements": "开创了香港“无厘头”喜剧先河"
}

```


* > **说明**：通过内置的 `Converter` 动态控制大模型输出格式，确保业务系统能够稳定解析响应数据。



### 3. 动态工具调用 (Function Calling)

* **业务场景**：用户提问 *“天津今天天气怎么样？我需要穿外套吗？”*
* **执行流程**：
```text
[用户请求] ➡️ [Spring AI 拦截] ➡️ [自动触发本地 WeatherService.java] ➡️ [数据回填 Context] ➡️ [LLM 综合分析并返回穿衣建议]

```



### 4. 知识库私有问答 (RAG)

* **第一步：文档向量化入库**
* `POST /api/v1/ai/rag/embedding`
* *说明：系统自动读取 `resources/documents/` 目录下的私有数据，智能切片并灌入向量数据库。*


* **第二步：精准闭环问答**
* `GET /api/v1/ai/rag/ask?question=我们公司的请假制度是怎样的？`
* *说明：严格基于企业内部文档检索结果进行回答，从根源上杜绝大模型的幻觉问题。*



---

## 🗺️ 未来规划 (Roadmap)

* [ ] **向量混合检索 (Hybrid Search)**：引入关键词+向量双路召回，大幅提升复杂私有文档的检索准确率。
* [ ] **高级 Agent 状态机编排**：整合 LangGraph 或 Flowable 状态机，在 Java 生态中构建具备自我反思与纠错能力的多 Agent 协同系统。
* [ ] **可视化交互控制台**：结合 Vue3 / Tailwind CSS 提供开箱即用的前端 Web 控制台。

---

## 🤝 贡献指南

非常欢迎共同完善这个示例项目！如果您发现了 Bug、有更好的企业级架构实践，或者想补充其他大模型的整合示例，欢迎提交 Pull Request：

1. **Fork** 本项目到您的个人账号。
2. 基于主分支创建特性分支：`git checkout -b feature/AmazingFeature`。
3. 提交您的修改，并保持代码规范：`git commit -m 'Add some AmazingFeature'`。
4. 推送到您的远端分支：`git push origin feature/AmazingFeature`。
5. 在 GitHub 上向本项目发起 **Pull Request**。

---

## 📄 开源协议

本项目基于 [Apache License 2.0](https://www.google.com/search?q=LICENSE) 协议开源。
