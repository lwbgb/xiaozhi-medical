package pers.lwb;

import dev.langchain4j.community.model.dashscope.QwenTokenCountEstimator;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader;
import dev.langchain4j.data.document.splitter.DocumentByParagraphSplitter;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import pers.lwb.properties.AliyunModelProperties;

import java.nio.file.FileSystems;
import java.nio.file.PathMatcher;
import java.util.List;

@SpringBootTest
public class EmbeddingStoreTest {

    @Qualifier("pineconeEmbeddingStore")
    @Autowired
    private EmbeddingStore<TextSegment> embeddingStore;

    @Autowired
    private AliyunModelProperties aliyunModelProperties;

    @Qualifier("qwenEmbeddingModel")
    @Autowired
    private EmbeddingModel embeddingModel;

    /**
     * 将 RAG 数据转化为向量存放到 Pinecone
     */
    @Test
    public void store() {
        PathMatcher pathMatcher = FileSystems.getDefault().getPathMatcher("glob:*.md");
        List<Document> documents = FileSystemDocumentLoader.loadDocuments("src/main/resources/knowledge", pathMatcher);

        DocumentByParagraphSplitter documentSplitter = new DocumentByParagraphSplitter(300,
                30,
                new QwenTokenCountEstimator(aliyunModelProperties.getApiKey(), "qwen-max"));

        // 配置 EmbeddingStoreIngestor，并实现文本嵌入和存储
        EmbeddingStoreIngestor.builder()
                .embeddingStore(embeddingStore)
                .documentSplitter(documentSplitter)
                .embeddingModel(embeddingModel)
                .build()
                .ingest(documents);
    }
}
