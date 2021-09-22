package cl.gpsmain.datasource.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.UUID;

@Document(collection = "gps")
public class GPS {

    @MongoId
    @JsonProperty("_id")
    private ObjectId id;

    @JsonProperty("clientId")
    private UUID clientId;

    @JsonProperty("clientSecretId")
    private UUID clientSecretId;

    @JsonProperty("isActive")
    private boolean isActive;

    @JsonProperty("installed")
    private boolean installed;

    public GPS() {
    }

    public GPS(final ObjectId id, final UUID clientId, final UUID clientSecret, final boolean isActive, final boolean installed) {
        this.id = id;
        this.clientId = clientId;
        this.clientSecretId = clientSecret;
        this.isActive = isActive;
        this.installed = installed;
    }

    public ObjectId getId() {
        return id;
    }

    protected void setId(ObjectId id) {
        this.id = id;
    }

    public UUID getClientId() {
        return clientId;
    }

    protected void setClientId(UUID clientId) {
        this.clientId = clientId;
    }

    public UUID getClientSecretId() {
        return clientSecretId;
    }

    protected void setClientSecretId(UUID clientSecretId) {
        this.clientSecretId = clientSecretId;
    }

    public boolean isActive() {
        return isActive;
    }

    protected void setActive(boolean active) {
        isActive = active;
    }

    public boolean isInstalled() {
        return installed;
    }

    protected void setInstalled(boolean installed) {
        this.installed = installed;
    }
}
