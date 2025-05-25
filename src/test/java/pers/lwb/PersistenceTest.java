package pers.lwb;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pers.lwb.assistant.PersistenceAssistant;

//@SpringBootTest
public class PersistenceTest {

    @Autowired
    private PersistenceAssistant persistenceAssistant;

    @Test
    public void Test01() {
        String ans1 = persistenceAssistant.chat("1", "我叫 Sayuri");
        System.out.println(ans1);
        String ans2 = persistenceAssistant.chat("1", "我是谁？");
        System.out.println(ans2);
        String ans3 = persistenceAssistant.chat("2", "我是谁？");
        System.out.println(ans3);
    }

}
