package pers.lwb.config;

import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import org.springframework.context.annotation.Configuration;

/**
 * @author LiWenBin
 */
@Configuration
public class SeparateChatAssistantConfig {

//    @Bean
    public ChatMemoryProvider chatMemoryProvider() {
        return memoryId -> MessageWindowChatMemory.builder()
                .id(memoryId)
                .maxMessages(20)
//                .chatMemoryStore(new InMemoryChatMemoryStore())
                .build();
    }
}
