package cl.gpsmain.datasource.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public class Activity {

    @JsonProperty("timestamp")
    private LocalDateTime timestamp;

    @JsonProperty("activityTitle")
    private String activityTitle;

    @JsonProperty("description")
    private String description;

    public Activity() {
    }

    public Activity(final LocalDateTime timestamp, final String activityTitle, final String description) {
        this.timestamp = timestamp;
        this.activityTitle = activityTitle;
        this.description = description;
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
