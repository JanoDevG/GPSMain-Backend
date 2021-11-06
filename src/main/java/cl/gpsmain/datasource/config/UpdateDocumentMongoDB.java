package cl.gpsmain.datasource.config;

import cl.gpsmain.datasource.model.Account;
import cl.gpsmain.datasource.model.Fleet;
import cl.gpsmain.datasource.model.GPS;
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

    public void updateAccount(Account account) {
        Query query = new Query(Criteria.where("mail").is(account.getMail()));
        Document doc = new Document();
        mongoTemplate.getConverter().write(account, doc);
        Update update = Update.fromDocument(doc);
        mongoTemplate.updateFirst(query, update, "account");
    }

    public void updateFleet(Fleet fleet) {
        Query query = new Query(Criteria.where("patent").is(fleet.getPatent()));
        fleet.setId(null); // Se deja nulo para no "actualizarlo" (MongoUpdate reconoce como intetno de actualziar)
        Document doc = new Document();
        mongoTemplate.getConverter().write(fleet, doc);
        Update update = Update.fromDocument(doc);
        mongoTemplate.updateFirst(query, update, "fleet");
    }

    public void updateGPS(GPS gps) {
        Query query = new Query(Criteria.where("_id").is(gps.getId()));
        gps.setId(null); // Se deja nulo para no "actualizarlo" (MongoUpdate reconoce como intetno de actualziar)
        Document doc = new Document();
        mongoTemplate.getConverter().write(gps, doc);
        Update update = Update.fromDocument(doc);
        mongoTemplate.updateFirst(query, update, "gps");
    }


}
