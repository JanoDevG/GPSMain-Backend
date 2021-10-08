package cl.gpsmain.datasource.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Document(collection = "key")
public class Key {

    @JsonProperty("business")
    @Getter
    @Setter
    private Business business;

    @JsonProperty("business")
    @Getter
    @Setter
    private User user;

    @JsonProperty("oauth")
    @Getter
    @Setter
    private OAuth oAuth;

    public static class Business {

        @JsonProperty("name")
        @Getter
        @Setter
        private String name;

        @JsonProperty("id")
        @Getter
        @Setter
        private UUID id;
    }

    public static class User {

        @JsonProperty("enterpriseAdmin")
        @Getter
        @Setter
        private UUID enterpriseAdmin;

        @JsonProperty("supervisor")
        @Getter
        @Setter
        private UUID supervisor;

        @JsonProperty("backoffice")
        @Getter
        @Setter
        private UUID backoffice;

        @JsonProperty("manager")
        @Getter
        @Setter
        private UUID manager;
    }

    public static class OAuth {

        @JsonProperty("clientId")
        @Getter
        @Setter
        private UUID clientId;

        @JsonProperty("clientSecret")
        @Getter
        @Setter
        private UUID clientSecret;
    }
}

