package cl.gpsmain.datasource.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.UUID;

@Document(collection = "activity")
public class Activity {

    @JsonProperty("accountId")
    private UUID accountId;

    @JsonProperty("timestamp")
    private LocalDateTime timestamp;

    @JsonProperty("activityTitle")
    private String activityTitle;

    @JsonProperty("description")
    private String description;

    public Activity() {
    }

    public Activity(final UUID accountId, final LocalDateTime timestamp, final String activityTitle, final String description) {
        this.accountId = accountId;
        this.timestamp = timestamp;
        this.activityTitle = activityTitle;
        this.description = description;
    }

    public UUID getAccountId() {
        return accountId;
    }

    public void setAccountId(final UUID accountId) {
        this.accountId = accountId;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(final LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getActivityTitle() {
        return activityTitle;
    }

    public void setActivityTitle(final String activityTitle) {
        this.activityTitle = activityTitle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }
}
