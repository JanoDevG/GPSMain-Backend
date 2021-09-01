package cl.gpsmain.datasource.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "customer")
public class Customer extends Contact {

    @JsonProperty("clientName")
    private String clientName;

    @JsonProperty("businessName")
    private String businessName;

    @JsonProperty("address")
    private String address;

    @JsonProperty("_contactId")
    private List<Contact> contactId;

    public Customer() {
        super();
    }

    public Customer(final String names, final String surnames, final List<String> numbers, final List<String> mails,
                    final String clientName, final String businessName, final String address, final List<Contact> contactId) {
        super(names, surnames, numbers, mails);
        this.clientName = clientName;
        this.businessName = businessName;
        this.address = address;
        this.contactId = contactId;
    }
}
