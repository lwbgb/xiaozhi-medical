package pers.lwb;

import com.alibaba.dashscope.aigc.imagesynthesis.ImageSynthesisResult;
import com.alibaba.dashscope.utils.JsonUtils;
import dev.langchain4j.community.model.dashscope.QwenChatModel;
import dev.langchain4j.community.model.dashscope.WanxImageModel;
import dev.langchain4j.data.image.Image;
import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.output.Response;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pers.lwb.properties.AliyunModelProperties;
import pers.lwb.utils.WanxImageGenerationUtil;

import java.net.URI;

//@SpringBootTest
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
    private OpenAiChatModel openAiChatModel;

    @Test
    public void springApiTest() {
        String ans = openAiChatModel.chat("你好");
        System.out.println(ans);
    }

    @Autowired
    private OllamaChatModel ollamaChatModel;

    @Test
    public void ollamaTest() {
        String ans = ollamaChatModel.chat("你是谁？");
        System.out.println(ans);
    }


    @Autowired
    private QwenChatModel qwenChatModel;

    @Test
    public void qWenTest() {
        String ans = qwenChatModel.chat("你是谁？");
        System.out.println(ans);
    }

    @Autowired
    private WanxImageGenerationUtil wanxImageGenerationUtil;

    @Test
    public void wanxImageTest() {
        String prompt = "中国女孩，圆脸，看着镜头，优雅的民族服装，商业摄影，室外，电影级光照，半身特写，精致的淡妆，锐利的边缘。";
        ImageSynthesisResult result = wanxImageGenerationUtil.asyncCall(prompt, "1024*1024");
        String json = JsonUtils.toJson(result.getOutput());
    }

    @Autowired
    private AliyunModelProperties aliyunModelProperties;

    @Test
    public void wanxImageTest2() {
        WanxImageModel imageModel = WanxImageModel.builder()
                .apiKey(aliyunModelProperties.getApiKey())
                .modelName(aliyunModelProperties.getModelName())
                .build();

        String prompt = "动漫角色薇尔莉特撑伞站立在紫罗兰花园，裙子随风而起";
        Response<Image> response = imageModel.generate(prompt);
        URI url = response.content().url();
        System.out.println(url);
    }
}
