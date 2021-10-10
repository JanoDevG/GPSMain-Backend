package cl.gpsmain.datasource.service;

import cl.gpsmain.datasource.model.Key;
import cl.gpsmain.datasource.service.repository.KeyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class TokenService {

    @Autowired
    private KeyRepository keyRepository;

    public boolean validateClientSecret(String clientSecret, String clientId) {
        Key key = keyRepository.findByOAuth_ClientIdAndOAuth_ClientSecret(UUID.fromString(clientId), UUID.fromString(clientSecret));
        return key != null;
    }
}
