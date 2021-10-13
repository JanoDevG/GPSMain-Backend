package cl.gpsmain.datasource.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.UUID;

@Document(collection = "fleet")
public class Fleet {

    @MongoId
    @JsonProperty("_id")
    @Getter
    @Setter
    private String id;

    @JsonProperty("businessId")
    @Getter
    @Setter
    private UUID businessId;

    @JsonProperty("gpsAssigned")
    @Getter
    @Setter
    private String gpsAssigned;

    @JsonProperty("carName")
    @Getter
    @Setter
    private String carName;

    @JsonProperty("patent")
    @Getter
    @Setter
    private String patent;

    @JsonProperty("chassisNumber")
    @Getter
    @Setter
    private String chassisNumber;

    @JsonProperty("year")
    @Getter
    @Setter
    private short year;
}
