package pers.lwb.controller;

import dev.langchain4j.service.TokenStream;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pers.lwb.DTO.ChatFormDTO;
import pers.lwb.assistant.XiaoZhiMedicalAssistant;

/**
 * @author LiWenBin
 */
@Slf4j
@RestController
@RequestMapping("/assistant/xiaozhi")
@Tag(name = "XiaoZhiController")
public class XiaoZhiController {

    private final XiaoZhiMedicalAssistant xiaoZhiMedicalAssistant;

    public XiaoZhiController(XiaoZhiMedicalAssistant xiaoZhiMedicalAssistant) {
        this.xiaoZhiMedicalAssistant = xiaoZhiMedicalAssistant;
    }

    @Operation(summary = "chat", description = "对话")
    @PostMapping(value = "/chat")
    public String chat(@RequestBody ChatFormDTO chatFormDTO) {
        log.info("用户{}发起对话：{}", chatFormDTO.getMemoryId(), chatFormDTO.getMessage());
        TokenStream tokenStream = xiaoZhiMedicalAssistant.streamChat(chatFormDTO.getMemoryId(), chatFormDTO.getMessage());
        tokenStream.onPartialResponse(System.out::print)
                .onError(Throwable::printStackTrace)
                .start();
        return "success";
    }
}
