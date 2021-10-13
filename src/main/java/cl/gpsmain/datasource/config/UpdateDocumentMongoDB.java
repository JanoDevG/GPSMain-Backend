package cl.gpsmain.datasource.config;

import cl.gpsmain.datasource.model.Account;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;


@Service
public class UpdateDocumentMongoDB {

    @Autowired
    private MongoTemplate mongoTemplate;

    public void updateDocument(Account account){
        Query query = new Query(Criteria.where("mail").is(account.getMail()));
        Document doc = new Document();
        mongoTemplate.getConverter().write(account, doc);
        Update update = Update.fromDocument(doc);
        mongoTemplate.updateFirst(query, update, "account");
    }

}
