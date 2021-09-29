package cl.gpsmain.datasource.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.UUID;

@Document(collection = "fleet")
public class Fleet {

    @MongoId
    @JsonProperty("_id")
    private ObjectId id;

    @JsonProperty("gpsAssigned")
    @Getter
    @Setter
    private UUID gpsAssigned;

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
