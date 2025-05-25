package pers.lwb.assistant;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;
import dev.langchain4j.service.spring.AiService;
import dev.langchain4j.service.spring.AiServiceWiringMode;

/**
 * @author LiWenBin
 */
@AiService(wiringMode = AiServiceWiringMode.EXPLICIT,
        chatModel = "qwenChatModel",
        chatMemoryProvider = "chatMemoryProvider")
public interface PersistenceAssistant {

    @SystemMessage(fromResource = "templates/my-prompt-template.txt")
    String chat(@MemoryId String memoryId, @UserMessage String userMessage);

    @SystemMessage("我名叫{{name}}, 年龄{{age}}")
    String chat(@MemoryId String memoryId, @UserMessage String userMessage,
                @V("name") String name, @V("age") int age);
}
