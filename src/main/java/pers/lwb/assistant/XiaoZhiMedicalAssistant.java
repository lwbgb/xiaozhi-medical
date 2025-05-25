package pers.lwb.assistant;

import dev.langchain4j.service.*;

/**
 * @author LiWenBin
 */
//@AiService(wiringMode = AiServiceWiringMode.EXPLICIT,
//        chatModel = "qwenChatModel",
//        chatMemoryProvider = "xiaoZhiChatMemoryProvider")
public interface XiaoZhiMedicalAssistant {

    @SystemMessage(fromResource = "templates/xiaozhi-medical-template.txt")
    Result<String> chat(@MemoryId Long memoryId, @UserMessage String userMessage);
}
