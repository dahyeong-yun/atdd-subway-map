package subway.domain;

import subway.presentation.LineRequest;

import javax.persistence.*;

@Entity
public class Line {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20, nullable = false)
    private String name;

    @Column(length = 20, nullable = false)
    private String color;

    @Embedded
    private Sections sections = new Sections();

    protected Line() {

    }

    public Line(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public static Line createLine(Station upStation, Station downStation, LineRequest lineRequest) {
        Line createLine = new Line(lineRequest.getName(), lineRequest.getColor());

        Section section = new Section(
                createLine,
                upStation,
                downStation,
                lineRequest.getDistance()
        );

        Sections sections = createLine.getSections();
        sections.addSections(section);
        return createLine;
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

    public Sections getSections() {
        return sections;
    }

    public Station getUpStation() {
        return sections.getUpStation();
    }

    public Station getDownStation() {
        return sections.getDownStation();
    }

    public void changeName(String name) {
        this.name = name;
    }

    public void changeColor(String color) {
        this.color = color;
    }
}
