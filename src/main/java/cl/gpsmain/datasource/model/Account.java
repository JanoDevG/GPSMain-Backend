package cl.gpsmain.datasource.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "account")
public class Account extends Customer {

    @JsonProperty("businessName")
    private String businessName;

    @JsonProperty("businessId")
    private String businessId;

    @JsonProperty("profile")
    private Profile profile;

    @JsonProperty("activity")
    private List<Activity> activity;

    @JsonProperty("GPSAssigned")
    private List<GPS> gPSAssigned;

    @JsonProperty("names")
    private String names;

    @JsonProperty("surnames")
    private String surnames;

    @JsonProperty("mail")
    private String mail;

    @JsonProperty("password")
    private String password;

    private static class Profile {

        @JsonProperty("profileName")
        private String profileName;

        @JsonProperty("permission")
        private String permission;

        @JsonProperty("key")
        private String key;

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        protected String getProfileName() {
            return profileName;
        }

        protected void setProfileName(final String profileName) {
            this.profileName = profileName;
        }

        protected Object getPermission() {
            return permission;
        }

        protected void setPermission(final String permission) {
            this.permission = permission;
        }
    }

    public Account() {
        super();
    }


    public Account(final String names, final String surnames, final List<String> numbers,
                   final List<String> mails, final String clientName, final String businessName,
                   final String address, final List<Contact> contactId, final String businessName1,
                   final String businessId, final Profile profile, final List<Activity> activity,
                   final List<GPS> gPSAssigned, final String names1, final String surnames1,
                   final String mail, final String password) {
        this.businessName = businessName1;
        this.businessId = businessId;
        this.profile = profile;
        this.activity = activity;
        this.gPSAssigned = gPSAssigned;
        this.names = names1;
        this.surnames = surnames1;
        this.mail = mail;
        this.password = password;
    }
}

