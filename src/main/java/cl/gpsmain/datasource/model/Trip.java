package cl.gpsmain.datasource.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

public class Trip {

    public List<Coordinate> getCoordinates() {
        if (this.coordinates == null) {
            this.coordinates = new ArrayList<>();
        }
        return coordinates;
    }

    @JsonProperty("coordenadas")
    @Setter
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

    public class Departure {

        @JsonProperty("partida")
        @Getter
        @Setter
        private String departure;

        public List<Integer> getCoordinates() {
            if (this.coordinates == null) {
                this.coordinates = new ArrayList<>();
            }
            return coordinates;
        }

        @JsonProperty("coordenadas")
        @Setter
        private List<Integer> coordinates;
    }

    public class Destiny {

        @JsonProperty("destino")
        @Getter
        @Setter
        private String destiny;

        public List<Integer> getCoordinates() {
            if (this.coordinates == null) {
                this.coordinates = new ArrayList<>();
            }
            return coordinates;
        }

        @JsonProperty("coordenadas")
        @Setter
        private List<Integer> coordinates;
    }

    public class Coordinate {

        public List<Integer> getCoordinate() {
            if (this.coordinate == null) {
                this.coordinate = new ArrayList<>();
            }
            return coordinate;
        }

        @JsonProperty("coordenada")
        @Setter
        private List<Integer> coordinate;

        @JsonProperty("hora")
        @Getter
        @Setter
        private int hour;

        @JsonProperty("minuto")
        @Getter
        @Setter
        private int minute;

        @JsonProperty("segundo")
        @Getter
        @Setter
        private int second;

        @JsonProperty("dia")
        @Getter
        @Setter
        private int day;

        @JsonProperty("mes")
        @Getter
        @Setter
        private int month;

    }
}


