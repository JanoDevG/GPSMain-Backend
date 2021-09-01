package cl.gpsmain.datasource.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.UUID;

@Document(collection = "fleet")
public class Fleet extends GPS {

    @MongoId
    @JsonProperty("_id")
    private UUID id;

    @JsonProperty("carName")
    private String carName;

    @JsonProperty("patent")
    private String patent;

    @JsonProperty("chassisNumber")
    private String chassisNumber;

    @JsonProperty("year")
    private short year;

    public Fleet() {
        super();
    }

    public Fleet(final UUID id, final UUID clientId, final String clientSecret, final boolean isActive,
                 final boolean installed, final UUID id1, final String carName, final String patent,
                 final String chassisNumber, final short year) {
        super(id, clientId, clientSecret, isActive, installed);
        this.id = id1;
        this.carName = carName;
        this.patent = patent;
        this.chassisNumber = chassisNumber;
        this.year = year;
    }
}
