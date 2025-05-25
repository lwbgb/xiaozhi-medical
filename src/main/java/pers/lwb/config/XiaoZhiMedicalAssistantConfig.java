package pers.lwb.config;

import dev.langchain4j.community.model.dashscope.QwenChatModel;
import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.service.AiServices;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pers.lwb.assistant.XiaoZhiMedicalAssistant;
import pers.lwb.store.MongoChatMemoryStore;

/**
 * @author LiWenBin
 */
@Configuration
public class XiaoZhiMedicalAssistantConfig {

    private final MongoChatMemoryStore mongoChatMemoryStore;

    private final QwenChatModel qwenChatModel;

    public XiaoZhiMedicalAssistantConfig(MongoChatMemoryStore mongoChatMemoryStore, QwenChatModel qwenChatModel) {
        this.mongoChatMemoryStore = mongoChatMemoryStore;
        this.qwenChatModel = qwenChatModel;
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
     * 创建小智医疗助手，设置包括：聊天模型、聊天配置
     * @param xiaoZhiChatMemoryProvider 配置聊天能力
     * @return XiaoZhiMedicalAssistant
     */
    @Bean
    public XiaoZhiMedicalAssistant xiaoZhiMedicalAssistant(ChatMemoryProvider xiaoZhiChatMemoryProvider) {
        return AiServices
                .builder(XiaoZhiMedicalAssistant.class)
                .chatModel(qwenChatModel)
                .chatMemoryProvider(xiaoZhiChatMemoryProvider)
                .build();
    }
}

