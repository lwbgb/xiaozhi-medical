package pers.lwb;

import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class LLMTest {

    @Test
    public void apiTest() {
        OpenAiChatModel model = OpenAiChatModel.builder()
                .baseUrl("http://langchain4j.dev/demo/openai/v1")
                .apiKey("demo")
                .modelName("gpt-4o-mini")
                .build();

        String ans = model.chat("你好");
        System.out.println(ans);
    }

    @Autowired
    private OpenAiChatModel chatModel;

    @Test
    public void springApiTest() {
        String ans = chatModel.chat("你好");
        System.out.println(ans);
    }

    @Autowired
    private OllamaChatModel ollamaChatModel;

    @Test
    public void ollamaTest() {
        String ans = ollamaChatModel.chat("你是谁？");
        System.out.println(ans);
    }
}
