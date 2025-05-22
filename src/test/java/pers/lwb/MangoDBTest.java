package pers.lwb;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import pers.lwb.pojo.ChatMessages;

//@SpringBootTest
public class MangoDBTest {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Test
    public void insertTest() {
        mongoTemplate.insert(new ChatMessages("Sayuri"));
    }

    @Test
    public void selectByIdTest() {
        ChatMessages chatMessages = mongoTemplate.findById("682f259ad14afb7a98a9221c", ChatMessages.class);
        System.out.println(chatMessages);
    }

    @Test
    public void updateTest() {
        Criteria criteria = Criteria.where("_id").is("682f259ad14afb7a98a9221c");
        Query query = new Query(criteria);
        Update update = new Update();
        update.set("content", "Asuka");
        // upsert 插入的数据如果 id 不存在则会新增，存在则覆盖
        mongoTemplate.upsert(query, update, ChatMessages.class);
    }

    @Test
    public void deleteByIdTest() {
        Criteria criteria = Criteria.where("_id").is("682f259ad14afb7a98a9221c");
        Query query = new Query(criteria);
        mongoTemplate.remove(query, ChatMessages.class);
    }
}
