package cl.gpsmain.datasource.service.core;

import cl.gpsmain.datasource.config.UpdateDocumentMongoDB;
import cl.gpsmain.datasource.model.Account;
import cl.gpsmain.datasource.model.Activity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ActivityService {

    @Autowired
    private UpdateDocumentMongoDB updateDocumentMongoDB;

    public void logActivity(Account account, String activity, String description) {
        account.getActivity().add(new Activity(LocalDateTime.now(), activity, description));
        updateDocumentMongoDB.updateAccount(account);
    }

}
