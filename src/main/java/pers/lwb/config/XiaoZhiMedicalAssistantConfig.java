package pers.lwb.config;

import dev.langchain4j.community.model.dashscope.QwenChatModel;
import dev.langchain4j.community.model.dashscope.QwenTokenCountEstimator;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader;
import dev.langchain4j.data.document.splitter.DocumentByParagraphSplitter;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pers.lwb.assistant.XiaoZhiMedicalAssistant;
import pers.lwb.properties.AliyunModelProperties;
import pers.lwb.store.MongoChatMemoryStore;
import pers.lwb.tools.AppointmentTool;

import java.nio.file.FileSystems;
import java.nio.file.PathMatcher;
import java.util.List;

/**
 * @author LiWenBin
 */
@Configuration
public class XiaoZhiMedicalAssistantConfig {

    private final MongoChatMemoryStore mongoChatMemoryStore;

    private final QwenChatModel qwenChatModel;

    private final AliyunModelProperties properties;

    private final EmbeddingModel embeddingModel;

    private final EmbeddingStore<TextSegment> embeddingStore;

    public XiaoZhiMedicalAssistantConfig(MongoChatMemoryStore mongoChatMemoryStore, QwenChatModel qwenChatModel, AliyunModelProperties properties, EmbeddingModel embeddingModel, EmbeddingStore<TextSegment> embeddingStore) {
        this.mongoChatMemoryStore = mongoChatMemoryStore;
        this.qwenChatModel = qwenChatModel;
        this.properties = properties;
        this.embeddingModel = embeddingModel;
        this.embeddingStore = embeddingStore;
    }

    /**
     * 配置小智医疗助手的聊天能力，包括：聊天记忆、记忆隔离和聊天消息持久化
     *
     * @return xiaoZhiChatMemoryProvider
     */
    @Bean("xiaoZhiChatMemoryProvider")
    public ChatMemoryProvider xiaoZhiChatMemoryProvider() {
        return memoryId ->
                MessageWindowChatMemory
                        .builder()
                        .id(memoryId)
                        .chatMemoryStore(mongoChatMemoryStore)
                        .maxMessages(20)
                        .build();
    }

    /**
     * 创建小智医疗助手，设置包括：聊天模型、聊天配置、挂号功能、RAG
     *
     * @param xiaoZhiChatMemoryProvider 配置聊天能力
     * @param appointmentTool           函数调用/工具
     * @param xiaoZhiContentRetriever   RAG 检索
     * @return XiaoZhiMedicalAssistant
     */
    @Bean
    public XiaoZhiMedicalAssistant xiaoZhiMedicalAssistant(ChatMemoryProvider xiaoZhiChatMemoryProvider, AppointmentTool appointmentTool, ContentRetriever xiaoZhiContentRetriever, ContentRetriever contentRetriever) {
        return AiServices
                .builder(XiaoZhiMedicalAssistant.class)
                .chatModel(qwenChatModel)
                .chatMemoryProvider(xiaoZhiChatMemoryProvider)
                .tools(appointmentTool)
                .contentRetriever(contentRetriever)
                .build();
    }

    /**
     * RAG：将文本信息转化为向量，并存放在向量库
     *
     * @return ContentRetriever 从生成的向量库提取内容检索器
     */
//    @Bean
    public ContentRetriever xiaoZhiContentRetriever() {
        // 读取用于 RAG 的文档
        PathMatcher pathMatcher = FileSystems.getDefault().getPathMatcher("glob:*.md");
        List<Document> documents = FileSystemDocumentLoader.loadDocuments("src/main/resources/knowledge", pathMatcher);

        // 创建向量存储库
        InMemoryEmbeddingStore<TextSegment> embeddingStore = new InMemoryEmbeddingStore<>();

        // 自定义文档分割器：
        // - Segment 最大 token 数：300
        // - Segment token 重叠个数：30 （用于保证连贯性）
        // - token 计算器：QwenTokenCountEstimator
        DocumentByParagraphSplitter documentSplitter = new DocumentByParagraphSplitter(300,
                30,
                new QwenTokenCountEstimator(properties.getApiKey(), "qwen-max"));

        // 配置 EmbeddingStoreIngestor，并实现文本嵌入和存储
        EmbeddingStoreIngestor.builder()
                .embeddingStore(embeddingStore)
                .documentSplitter(documentSplitter)
                .embeddingModel(embeddingModel)
                .build();
        EmbeddingStoreIngestor.ingest(documents, embeddingStore);
        return EmbeddingStoreContentRetriever.from(embeddingStore);
    }


    /**
     * 构建基于 Pinecone 的内容检索库，增强 RAG 检索
     * @return
     */
    @Bean
    public ContentRetriever xiaoZhiPineconeRetriever() {
        return EmbeddingStoreContentRetriever.builder()
                .embeddingModel(embeddingModel)
                .embeddingStore(embeddingStore)
                // 查询的最大结果数量
                .maxResults(1)
                // 过滤的最低得分
                .minScore(0.8)
                .build();
    }
}

