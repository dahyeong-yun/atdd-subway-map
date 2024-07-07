package subway.presentation;

import subway.domain.Station;

import java.util.List;

public class LineResponse {
    private final Long id;
    private final String name;
    private final String color;
    private final List<Station> stations;

    public LineResponse(Long id, String name, String color, List<Station> stations) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.stations = stations;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Station> getStations() {
        return stations;
    }

    public static LineResponse of(Long id, String name, String color, List<Station> stations) {
        return new LineResponse(id, name, color , stations);
    }
}
