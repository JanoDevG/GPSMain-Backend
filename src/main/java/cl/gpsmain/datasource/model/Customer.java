package cl.gpsmain.datasource.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "customer")
public class Customer {

    @JsonProperty("clientName")
    @Getter
    @Setter
    private String clientName;

    @JsonProperty("businessName")
    @Getter
    @Setter
    private String businessName;

    @JsonProperty("_contactId")
    @Getter
    @Setter
    private List<Contact> contactId;


}
