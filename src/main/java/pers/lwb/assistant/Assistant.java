package pers.lwb.assistant;

import dev.langchain4j.service.spring.AiService;
import dev.langchain4j.service.spring.AiServiceWiringMode;

/**
 * @author LiWenBin
 */
@AiService(wiringMode = AiServiceWiringMode.EXPLICIT, chatModel = "ollamaChatModel")
public interface Assistant {

    String chat(String userMessage);
}
