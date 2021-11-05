package cl.gpsmain.datasource.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.UUID;

@Document(collection = "gps")
public class GPS {

    @JsonProperty("_id")
    @Getter
    @Setter
    private String id;

    @JsonProperty("clientId")
    @Getter
    @Setter
    private UUID clientId;

    @JsonProperty("clientSecretId")
    @Getter
    @Setter
    private UUID clientSecretId;

    @JsonProperty("isActive")
    @Getter
    @Setter
    private boolean isActive;

    @JsonProperty("isInstalled")
    @Getter
    @Setter
    private boolean isInstalled;
}