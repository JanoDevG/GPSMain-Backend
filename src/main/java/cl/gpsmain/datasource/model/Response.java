package cl.gpsmain.datasource.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

public class Response {

    @JsonProperty("status")
    @Getter
    @Setter
    private HttpStatus status;
    @JsonProperty("body")
    @Getter
    @Setter
    private Object body;
}
