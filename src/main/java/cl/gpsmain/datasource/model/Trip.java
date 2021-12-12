package cl.gpsmain.datasource.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

public class Trip {

    @JsonProperty("coordenadas")
    @Setter
    @Getter
    private List<Coordinate> coordinates;

    @JsonProperty("partida")
    @Getter
    @Setter
    private Departure departure;

    @JsonProperty("destino")
    @Getter
    @Setter
    private Destiny destiny;

    @JsonProperty("patente")
    @Getter
    @Setter
    private String patent;

    public static class Departure {

        @JsonProperty("partida")
        @Getter
        @Setter
        private String departure;

        public List<String> getCoordinates() {
            if (this.coordinates == null) {
                this.coordinates = new ArrayList<>();
            }
            return coordinates;
        }

        @JsonProperty("coordenadas")
        @Setter
        private List<String> coordinates;
    }

    public static class Destiny {

        @JsonProperty("destino")
        @Getter
        @Setter
        private String destiny;

        public List<String> getCoordinates() {
            if (this.coordinates == null) {
                this.coordinates = new ArrayList<>();
            }
            return coordinates;
        }

        @JsonProperty("coordenadas")
        @Setter
        private List<String> coordinates;
    }

    public static class Coordinate {

        @JsonProperty("coordenada")
        @Setter
        @Getter
        private List<String> coordinate;

        @JsonProperty("dia")
        @Getter
        @Setter
        private int day;

        @JsonProperty("hora")
        @Getter
        @Setter
        private int hour;

        @JsonProperty("mes")
        @Getter
        @Setter
        private int month;

        @JsonProperty("minuto")
        @Getter
        @Setter
        private int minute;

        @JsonProperty("segundo")
        @Getter
        @Setter
        private int second;

    }
}


