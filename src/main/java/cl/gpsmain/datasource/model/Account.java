package cl.gpsmain.datasource.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Document(collection = "account")
public class Account {

    @MongoId
    @JsonProperty("_id")
    @Getter
    @Setter
    private String id;

    @JsonProperty("businessName")
    @Getter
    @Setter
    private String businessName;

    @JsonProperty("businessId")
    @Getter
    @Setter
    private UUID businessId;

    @JsonProperty("profile")
    @Getter
    @Setter
    private String profile;

    @JsonProperty("activity")
    public List<Activity> getActivity(){
        if (this.activity == null){
            this.activity = new ArrayList<Activity>();
        }
        return this.activity;
    }
    @Setter
    private List<Activity> activity;

    @JsonProperty("GPSAssigned")
    @Getter
    @Setter
    private List<GPS> gPSAssigned;

    @JsonProperty("names")
    @Getter
    @Setter
    private String names;

    @JsonProperty("surnames")
    @Getter
    @Setter
    private String surnames;

    @JsonProperty("mail")
    @Getter
    @Setter
    private String mail;

    @JsonProperty("password")
    @Getter
    @Setter
    private String password;

}

