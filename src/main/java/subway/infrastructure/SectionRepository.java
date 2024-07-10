package subway.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import subway.domain.Section;

public interface SectionRepository extends JpaRepository<Section, Long> {
}
