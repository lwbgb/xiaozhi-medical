package pers.lwb;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pers.lwb.assistant.PersistenceAssistant;

@SpringBootTest
public class PromptTest {

    @Autowired
    PersistenceAssistant assistant;

    @Test
    public void promptTest1() {
        String ans = assistant.chat("4", "今天星期几了？");
        System.out.println(ans);
    }

    @Test
    public void promptTest2() {
        String ans = assistant.chat("5", "你能帮我登记一下我的个人信息吗？", "Sayuri", 23);
        System.out.println(ans);
    }
}
