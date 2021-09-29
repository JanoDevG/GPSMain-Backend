package cl.gpsmain.datasource.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "contact")
public class Contact {

    @JsonProperty("names")
    @Getter
    @Setter
    private String names;

    @JsonProperty("surnames")
    @Getter
    @Setter
    private String surnames;

    @JsonProperty("numbers")
    @Getter
    @Setter
    private List<String> numbers;

    @JsonProperty("mails")
    @Getter
    @Setter
    private List<String> mails;

}
