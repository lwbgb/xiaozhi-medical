package pers.lwb;

import dev.langchain4j.community.model.dashscope.QwenChatModel;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.service.AiServices;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pers.lwb.assistant.Assistant;
import pers.lwb.assistant.ChatMemoryAssistant;
import pers.lwb.assistant.SeparateChatAssistant;

//@SpringBootTest
public class AIServiceTest {

    @Autowired
    private Assistant assistant;

    @Autowired
    private QwenChatModel qwenChatModel;

    @Test
    public void chatTest() {
        String ans = assistant.chat("你是谁？");
        System.out.println(ans);
    }

    @Test
    public void chatMemTest() {
        MessageWindowChatMemory chatMemory = MessageWindowChatMemory.withMaxMessages(10);

        Assistant as = AiServices.builder(Assistant.class)
                .chatMemory(chatMemory)
                .chatModel(qwenChatModel)
                .build();

        String ans1 = as.chat("我的名字是 Sayuri");
        System.out.println(ans1);

        String ans2 = as.chat("我是谁？");
        System.out.println(ans2);
    }

    @Autowired
    private ChatMemoryAssistant chatMemoryAssistant;

    @Test
    public void chatMemTest1() {
        String ans1 = chatMemoryAssistant.chat("我叫 Sayuri");
        System.out.println(ans1);
        String ans2 = chatMemoryAssistant.chat("我是谁？");
        System.out.println(ans2);
    }

    @Autowired
    private SeparateChatAssistant separateChatAssistant;

    @Test
    public void separateChatTest() {
        String ans1 = separateChatAssistant.chat("1", "我叫 Sayuri");
        System.out.println(ans1);
        String ans2 = separateChatAssistant.chat("1", "我是谁？");
        System.out.println(ans2);
        String ans3 = separateChatAssistant.chat("2", "我是谁？");
        System.out.println(ans3);
    }
}
