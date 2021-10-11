package cl.gpsmain.datasource.service.core;

import cl.gpsmain.datasource.model.Account;
import cl.gpsmain.datasource.model.Activity;
import cl.gpsmain.datasource.service.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ActivityService {

    @Autowired
    private AccountRepository accountRepository;

    public void logActivity(Account account, String activity, String description) {
        account.getActivity().add(new Activity(LocalDateTime.now(), activity, description));
        accountRepository.save(account);

    }

}
