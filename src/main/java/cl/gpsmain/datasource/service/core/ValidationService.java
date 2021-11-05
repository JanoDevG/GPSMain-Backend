package cl.gpsmain.datasource.service.core;

import cl.gpsmain.datasource.model.Key;
import cl.gpsmain.datasource.service.repository.KeyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ValidationService {

    @Autowired
    private KeyRepository keyRepository;


    public boolean validateClientSecret(UUID id, UUID clientSecret) {
        //TODO implementar el find retorna siempre null (false)
        Key key = keyRepository.findByBusiness_BusinessIdAndOauth_ClientSecret(id, clientSecret);
        return false;
    }

}
