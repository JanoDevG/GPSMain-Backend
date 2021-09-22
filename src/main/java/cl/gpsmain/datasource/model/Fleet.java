package cl.gpsmain.datasource.model;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    private UUID gpsAssigned;

    @JsonProperty("carName")
    private String carName;

    @JsonProperty("patent")
    private String patent;

    @JsonProperty("chassisNumber")
    private String chassisNumber;

    @JsonProperty("year")
    private short year;

    public Fleet() {

    }

    public Fleet(final ObjectId id, final UUID clientId, final UUID clientSecret, final boolean isActive,
                 final boolean installed, final UUID id1, final String carName, final String patent,
                 final String chassisNumber, final short year) {
        this.id = id;
        this.carName = carName;
        this.patent = patent;
        this.chassisNumber = chassisNumber;
        this.year = year;
    }
}
