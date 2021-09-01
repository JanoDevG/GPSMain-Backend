package cl.gpsmain.datasource.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.UUID;

@Document(collection = "gps")
public class GPS {

    @MongoId
    @JsonProperty("_id")
    private UUID id;

    @JsonProperty("clientId")
    private UUID clientId;

    @JsonProperty("clientSecretId")
    private String clientSecretId;

    @JsonProperty("isActive")
    private boolean isActive;

    @JsonProperty("installed")
    private boolean installed;

    public GPS() {
    }

    public GPS(final UUID id, final UUID clientId, final String clientSecret, final boolean isActive, final boolean installed) {
        this.id = id;
        this.clientId = clientId;
        this.clientSecretId = clientSecret;
        this.isActive = isActive;
        this.installed = installed;
    }
}
