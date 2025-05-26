package pers.lwb;

import dev.langchain4j.community.model.dashscope.QwenTokenCountEstimator;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader;
import dev.langchain4j.data.document.parser.TextDocumentParser;
import dev.langchain4j.data.document.parser.apache.pdfbox.ApachePdfBoxDocumentParser;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.onnx.HuggingFaceTokenCountEstimator;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ResourceLoader;
import pers.lwb.properties.AliyunModelProperties;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.PathMatcher;
import java.util.List;

//@SpringBootTest
public class RAGTest {

    @Qualifier("webApplicationContext")
    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private AliyunModelProperties properties;

    @Test
    public void documentLoaderTest() throws IOException {
        // 加载单个文件
        Document document1 = FileSystemDocumentLoader.loadDocument("src/main/resources/knowledge/医院信息.txt");
//        System.out.println(document1.text());
        // 加载目录下的所有文件
        List<Document> documents = FileSystemDocumentLoader.loadDocuments("src/main/resources/knowledge");
        documents.forEach(System.out::println);
        // 加载目录及其子目录下的所有文档
        List<Document> allDocuments = FileSystemDocumentLoader.loadDocumentsRecursively("src/main/resources/knowledge");
        // 你可以使用 glob 或正则表达式过滤文档：
        PathMatcher pathMatcher = FileSystems.getDefault().getPathMatcher("glob:*.pdf");
        List<Document> filteredDocuments = FileSystemDocumentLoader.loadDocuments("src/main/resources/knowledge", pathMatcher);
    }

    @Test
    public void documentParserTest() {
        Document pdfDocument = FileSystemDocumentLoader.loadDocument("src/main/resources/knowledge/医院信息.pdf",
                new ApachePdfBoxDocumentParser());
        System.out.println(pdfDocument.metadata());

        Document document1 = FileSystemDocumentLoader.loadDocument("src/main/resources/knowledge/医院信息.txt", new TextDocumentParser());
        System.out.println(document1.text());
    }

    @Test
    public void embeddingTest() {
        // 读取文件
        Document document = FileSystemDocumentLoader.loadDocument("src/main/resources/knowledge/人工智能.md");
        // 创建用于向量存储的对象
        InMemoryEmbeddingStore<TextSegment> embeddingStore = new InMemoryEmbeddingStore<>();
        // 实现文本分割、embedding 和存储的过程，主要包含以下操作
        // 1.分割文档：默认使用递归分割器，将文档分割为多个文本片段，每个片段包含不超过300个 token，且有30个 token 的重叠部分保证连贯性
        // 2.文本向量化：使用一个 LangChain4j 内置的轻量化向量模型对每个文本片段进行向量化
        // 3.将原始文本和向量存储到向量数据库中(InMemoryEmbeddingStore)
        EmbeddingStoreIngestor.ingest(document, embeddingStore);
        System.out.println(embeddingStore);
    }

    @Test
    public void testTokenCount() {
        String text = "这是一个示例文本，用于测试 token 长度的计算。 ";
        UserMessage userMessage = UserMessage.userMessage(text);
        HuggingFaceTokenCountEstimator tokenizer = new HuggingFaceTokenCountEstimator();
        QwenTokenCountEstimator qwenTokenCountEstimator = new QwenTokenCountEstimator(properties.getApiKey(), "qwen-max");
        int count = tokenizer.estimateTokenCountInMessage(userMessage);
        int qwenCount = qwenTokenCountEstimator.estimateTokenCountInMessage(userMessage);
        System.out.println("token长度： " + count);
        System.out.println("qwenCount： " + qwenCount);
    }
}
