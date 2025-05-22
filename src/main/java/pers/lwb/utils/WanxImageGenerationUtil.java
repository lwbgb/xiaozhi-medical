package pers.lwb.utils;


import com.alibaba.dashscope.aigc.imagesynthesis.ImageSynthesis;
import com.alibaba.dashscope.aigc.imagesynthesis.ImageSynthesisParam;
import com.alibaba.dashscope.aigc.imagesynthesis.ImageSynthesisResult;
import com.alibaba.dashscope.exception.ApiException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.alibaba.dashscope.utils.JsonUtils;
import org.springframework.stereotype.Component;
import pers.lwb.properties.AliyunModelProperties;

@Component
public class WanxImageGenerationUtil {

    private final AliyunModelProperties properties;

    public WanxImageGenerationUtil(AliyunModelProperties properties) {
        this.properties = properties;
    }

    public ImageSynthesisResult asyncCall(String prompt, String resolution) {
        System.out.println("---create task----");
        String taskId = this.createAsyncTask(prompt, resolution);
        System.out.println("---wait task done then return image url----");
        return this.waitAsyncTask(taskId);
    }


    /**
     * 创建异步任务
     *
     * @return taskId
     */
    public String createAsyncTask(String prompt, String resolution) {
//        String prompt = "一间有着精致窗户的花店，漂亮的木质门，摆放着花朵";
        ImageSynthesisParam param =
                ImageSynthesisParam.builder()
//                        .apiKey(System.getenv("DASHSCOPE_API_KEY"))
                        .apiKey(properties.getApiKey())
                        .model(properties.getModelName())
                        .prompt(prompt)
                        .n(1)
//                        .size("1024*1024")
                        .size(resolution)
                        .build();

        ImageSynthesis imageSynthesis = new ImageSynthesis();
        ImageSynthesisResult result = null;
        try {
            result = imageSynthesis.asyncCall(param);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        System.out.println(JsonUtils.toJson(result));
        String taskId = result.getOutput().getTaskId();
        System.out.println("taskId=" + taskId);
        return taskId;
    }


    /**
     * 等待异步任务结束
     *
     * @param taskId 任务id
     * @return
     */
    public ImageSynthesisResult waitAsyncTask(String taskId) {
        ImageSynthesis imageSynthesis = new ImageSynthesis();
        ImageSynthesisResult result = null;
        try {
            //如果已经在环境变量中设置了 DASHSCOPE_API_KEY，wait()方法可将apiKey设置为null
            result = imageSynthesis.wait(taskId, properties.getApiKey());
        } catch (ApiException | NoApiKeyException e) {
            throw new RuntimeException(e.getMessage());
        }
//        System.out.println(JsonUtils.toJson(result));
        System.out.println(JsonUtils.toJson(result.getOutput()));
        return result;
    }
}
