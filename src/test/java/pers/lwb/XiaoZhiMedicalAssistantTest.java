package pers.lwb;

import dev.langchain4j.service.Result;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pers.lwb.assistant.XiaoZhiMedicalAssistant;

@Slf4j
//@SpringBootTest
public class XiaoZhiMedicalAssistantTest {

    @Autowired
    XiaoZhiMedicalAssistant xiaoZhiMedicalAssistant;

    @Test
    public void Test1() {
        Result<String> result = xiaoZhiMedicalAssistant.chat(3L, "你是谁？");
        log.info("结果：{}", result.content());
    }
}
