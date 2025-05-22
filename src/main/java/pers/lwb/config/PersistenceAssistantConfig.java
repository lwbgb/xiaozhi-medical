package pers.lwb.config;

import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pers.lwb.store.MongoChatMemoryStore;

/**
 * @author LiWenBin
 */
@Configuration
public class PersistenceAssistantConfig {

    private final MongoChatMemoryStore mongoChatMemoryStore;

    public PersistenceAssistantConfig(MongoChatMemoryStore mongoChatMemoryStore) {
        this.mongoChatMemoryStore = mongoChatMemoryStore;
    }

    @Bean
    public ChatMemoryProvider chatMemoryProvider() {
        return memoryId -> MessageWindowChatMemory.builder()
                .id(memoryId)
                .maxMessages(20)
                .chatMemoryStore(mongoChatMemoryStore)
                .build();
    }
}
