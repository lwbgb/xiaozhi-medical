package pers.lwb.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author LiWenBin
 */
@Data
@Component
//@ConfigurationProperties("langchain4j.community.dashscope.chat-model")
@ConfigurationProperties("aliyun.image-model")
public class AliyunModelProperties {

    private String apiKey;

    private String modelName;

    private String baseUrl;
}
