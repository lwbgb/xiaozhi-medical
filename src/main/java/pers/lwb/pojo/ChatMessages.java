package pers.lwb.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author LiWenBin
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
// 在数据库中创建对应的集合
@Document("chat_messages")
public class ChatMessages {

    // 对应 MangoDB 记录的 id
    @Id
    private ObjectId messageId;

    // chatMemory 的 id
    private Long memoryId;

    // 对应 MangoDB 记录的内容，格式为 json
    private String content;

    public ChatMessages(String content) {
        this.content = content;
    }
}
