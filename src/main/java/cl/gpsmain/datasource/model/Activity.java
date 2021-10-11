package cl.gpsmain.datasource.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

public class Activity {

    @JsonProperty("timestamp")
    @Getter
    @Setter
    private LocalDateTime timestamp;

    @JsonProperty("activityTitle")
    @Getter
    @Setter
    private String activityTitle;

    @JsonProperty("description")
    @Getter
    @Setter
    private String description;

    public Activity(LocalDateTime timestamp, String activityTitle, String description) {
        this.timestamp = timestamp;
        this.activityTitle = activityTitle;
        this.description = description;
    }

    public Activity() {
    }
}
