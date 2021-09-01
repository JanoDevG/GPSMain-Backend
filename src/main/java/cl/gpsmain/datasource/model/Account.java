package cl.gpsmain.datasource.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "account")
public class Account extends Customer {

    @JsonProperty("businessName")
    private String businessName;

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

    private static class Profile {

        @JsonProperty("profile")
        private String profileName;

        @JsonProperty("permission")
        // TODO debatir tema de permisos
        private Object permission;

        protected String getProfileName() {
            return profileName;
        }

        protected void setProfileName(final String profileName) {
            this.profileName = profileName;
        }

        protected Object getPermission() {
            return permission;
        }

        protected void setPermission(final Object permission) {
            this.permission = permission;
        }
    }

    public Account() {
        super();
    }

    public Account(final String names, final String surnames, final List<String> numbers, final List<String> mails,
                   final String clientName, final String businessName, final String address, final List<Contact> contactId,
                   final String businessName1, final Profile profile, final List<Activity> activity,
                   final List<GPS> gPSAssigned, final String names1, final String surnames1, final String mail) {
        super(names, surnames, numbers, mails, clientName, businessName, address, contactId);
        this.businessName = businessName1;
        this.profile = profile;
        this.activity = activity;
        this.gPSAssigned = gPSAssigned;
        this.names = names1;
        this.surnames = surnames1;
        this.mail = mail;
    }
}

