package pers.lwb.properties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author LiWenBin
 */
@Data
@Component
@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties(prefix = "pinecone")
public class PineconeProperties {

    private String apiKey;

    private String indexName;

    private String namespace;

    private String cloud;

    private String region;
}
