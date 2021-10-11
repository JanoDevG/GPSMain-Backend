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

    public Key(Business business, User user, OAuth oAuth) {
        this.business = business;
        this.user = user;
        this.oAuth = oAuth;
    }

    public static class Business {

        @JsonProperty("name")
        @Getter
        @Setter
        private String name;

        @JsonProperty("id")
        @Getter
        @Setter
        private UUID id;

        public Business(String name, UUID id) {
            this.name = name;
            this.id = id;
        }
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

        public User(UUID enterpriseAdmin, UUID supervisor, UUID backoffice, UUID manager) {
            this.enterpriseAdmin = enterpriseAdmin;
            this.supervisor = supervisor;
            this.backoffice = backoffice;
            this.manager = manager;
        }
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

        public OAuth(UUID clientId, UUID clientSecret) {
            this.clientId = clientId;
            this.clientSecret = clientSecret;
        }
    }
}

