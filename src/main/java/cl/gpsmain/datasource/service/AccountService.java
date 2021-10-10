package cl.gpsmain.datasource.service;

import cl.gpsmain.datasource.model.Account;
import cl.gpsmain.datasource.model.Key;
import cl.gpsmain.datasource.model.Response;
import cl.gpsmain.datasource.service.repository.AccountRepository;
import cl.gpsmain.datasource.service.repository.KeyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private KeyRepository keyRepository;

    @Autowired
    private TokenService tokenService;

    private static final Response RESPONSE = new Response();

    public ResponseEntity<Response> accountService(Account account, String clientId, String clientSecret, String option) {
        if (!tokenService.validateClientSecret(clientSecret, clientId)) {
            RESPONSE.setBody("el clientSecret no es válido para el clientId informado");
            RESPONSE.setStatus(HttpStatus.UNAUTHORIZED);
            return new ResponseEntity<>(RESPONSE, RESPONSE.getStatus());
        }
        Account acc = accountRepository.findByMailAndBusinessName(account.getMail(), account.getBusinessName());
        switch (option) {
            case "CREATE": // create account if not exist
                if (acc != null) {
                    RESPONSE.setBody("La cuenta ya existe, se debe actualizar si se requiere modificar.");
                    RESPONSE.setStatus(HttpStatus.BAD_REQUEST);
                } else {
                    accountRepository.insert(account);
                    RESPONSE.setBody("La cuenta fue creada exitosamente.");
                    RESPONSE.setStatus(HttpStatus.OK);
                }
                break;
            case "UPDATE": // update account if exist
                if (acc == null) {
                    RESPONSE.setBody("La cuenta no existe. No hay datos que actualizar.");
                    RESPONSE.setStatus(HttpStatus.BAD_REQUEST);

                } else {
                    accountRepository.save(account);
                    RESPONSE.setBody("Cuenta actualizada exitosamente.");
                    RESPONSE.setStatus(HttpStatus.OK);
                }
                break;
            case "DELETE": // delete account if exist
                if (acc == null) {
                    RESPONSE.setBody("La cuenta no existe. No hay datos que eliminar.");
                    RESPONSE.setStatus(HttpStatus.BAD_REQUEST);
                } else {
                    accountRepository.delete(acc);
                    RESPONSE.setBody("La cuenta de: ".concat(acc.getNames()).concat(" fue eliminada exitosamente."));
                    RESPONSE.setStatus(HttpStatus.OK);
                }
                break;
            default:
                RESPONSE.setBody("la operación: ".concat(option).concat(" no es válida (Header: X-option)."));
                RESPONSE.setStatus(HttpStatus.METHOD_NOT_ALLOWED);
                break;
        }
        return new ResponseEntity<>(RESPONSE, RESPONSE.getStatus());
    }

    private boolean validateTokens(UUID clientId, UUID clientSecret, Account account) {
        //TODO validar que las KEY también coincidan con el accuntId
        Key key = keyRepository.findByOAuth_ClientIdAndOAuth_ClientSecret(clientId, clientSecret);
        return key != null;
    }
}
