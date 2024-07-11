package subway.domain;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class Sections {
    @OneToMany(mappedBy = "line", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public Station getUpStation() {
        if (sections.isEmpty()) {
            throw new IllegalStateException("No sections available");
        }
        return sections.get(0).getUpStation();
    }

    public Station getDownStation() {
        if (sections.isEmpty()) {
            throw new IllegalStateException("No sections available");
        }
        return sections.get(sections.size() - 1).getDownStation();
    }

    public void add(Section newSection) {
        if (sections.isEmpty()) {
            sections.add(newSection);
            return;
        }

        if (newSection.getUpStation() != getDownStation()) {
            throw new RuntimeException("역이 연결되지 않았습니다.");
        }
        sections.add(newSection);
    }
}
