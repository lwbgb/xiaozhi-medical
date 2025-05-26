package pers.lwb.config;

import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.pinecone.PineconeEmbeddingStore;
import dev.langchain4j.store.embedding.pinecone.PineconeServerlessIndexConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pers.lwb.properties.PineconeProperties;

/**
 * @author LiWenBin
 */
@Configuration
public class EmbeddingStoreConfig {

    private final EmbeddingModel embeddingModel;

    private final PineconeProperties pineconeProperties;

    public EmbeddingStoreConfig(EmbeddingModel embeddingModel, PineconeProperties pineconeProperties) {
        this.embeddingModel = embeddingModel;
        this.pineconeProperties = pineconeProperties;
    }

    /**
     * 配置 Pinecone 向量存储库
     *
     * @return 向量存储库
     */
    @Bean
    public EmbeddingStore<TextSegment> embeddingStore() {
        return PineconeEmbeddingStore.builder()
                .apiKey(pineconeProperties.getApiKey())
                .index(pineconeProperties.getIndexName())
                .nameSpace(pineconeProperties.getNamespace())
                .createIndex(PineconeServerlessIndexConfig.builder()
                        .cloud(pineconeProperties.getCloud())
                        .region(pineconeProperties.getRegion())
                        .dimension(embeddingModel.dimension())
                        .build())
                .build();
    }
}
