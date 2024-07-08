package subway.domain;

import javax.persistence.*;
import java.util.List;

@Entity
public class Line {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20, nullable = false)
    private String name;

    @Column(length = 20, nullable = false)
    private String color;

    private Long  upStationId;

    private Long downStationId;

//    @OneToMany
//    private List<Station> stations;

    private Integer distance;

    public Line() {

    }

    public Line(String name, String color, Long upStationId, Long downStationId, Integer distance, List<Station> stations) {
        this.name = name;
        this.color = color;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public String getName() {
        return name;
    }

    public Long getId() {
        return id;
    }
    public String getColor() {
        return color;
    }
    public Long getUpStationId() {
        return upStationId;
    }
    public Long getDownStationId() {
        return downStationId;
    }
    public Integer getDistance() {
        return distance;
    }

    public void changeName(String name) {
        this.name = name;
    }

    public void changeColor(String color) {
        this.color = color;
    }
}
