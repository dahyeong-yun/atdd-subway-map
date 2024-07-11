package subway.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Station;
import subway.infrastructure.LineRepository;
import subway.infrastructure.SectionRepository;
import subway.infrastructure.StationRepository;
import subway.presentation.SectionRequest;
import subway.presentation.SectionResponse;

import java.util.Objects;

@Service
@Transactional
public class SectionService {
    private LineRepository lineRepository;
    private SectionRepository sectionRepository;
    private StationRepository stationRepository;


    public SectionService(LineRepository lineRepository, SectionRepository sectionRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.sectionRepository = sectionRepository;
        this.stationRepository = stationRepository;
    }

    public SectionResponse saveSection(Long lineId, SectionRequest sectionRequest) {
        Line line = lineRepository.findById(lineId).orElseThrow(); // TODO 존재하지 않는 라인 예외 처리

        Station upStation = stationRepository.findById(sectionRequest.getUpStationId()).orElseThrow();
        Station downStation = stationRepository.findById(sectionRequest.getDownStationId()).orElseThrow();

        Section requestSection = new Section(
                line,
                upStation,
                downStation,
                sectionRequest.getDistance()
        );
        line.addSections(requestSection);
        Section createdSection = sectionRepository.save(requestSection);

        return SectionResponse.of(createdSection);
    }

    public void deleteSection(Long lineId, Long stationId) {
        Line line = lineRepository.findById(lineId).orElseThrow(); // TODO 존재하지 않는 라인 예외 처리

        line.deleteLastSection();
        sectionRepository.deleteById(stationId);
    }
}
