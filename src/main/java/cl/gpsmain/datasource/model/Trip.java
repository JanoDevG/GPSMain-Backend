package cl.gpsmain.datasource.model;

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

    public Departure getDeparture() {
        if (this.departure == null) {
            this.departure = new Departure();
        }
        return departure;
    }

    public Destiny getDestiny() {
        if (this.destiny == null) {
            this.destiny = new Destiny();
        }
        return destiny;
    }

    @JsonProperty("coordenadas")
    @Setter
    private List<Coordinate> coordinates;

    @JsonProperty("partida")
    @Setter
    private Departure departure;

    @JsonProperty("destino")
    @Setter
    private Destiny destiny;

    @JsonProperty("patente")
    @Setter
    @Getter
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

        @JsonProperty("fin")
        @Getter
        @Setter
        private boolean end;

    }

    public static class Coordinate {

        public List<String> getCoordinate() {
            if (this.coordinate == null) {
                this.coordinate = new ArrayList<>();
            }
            return coordinate;
        }

        @JsonProperty("coordenada")
        @Setter
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


