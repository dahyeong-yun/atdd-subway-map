package subway.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Station;
import subway.infrastructure.LineRepository;
import subway.infrastructure.SectionRepository;
import subway.infrastructure.StationRepository;
import subway.presentation.LineRequest;
import subway.presentation.LineResponse;
import subway.presentation.LineUpdateRequest;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class LineService {
    private LineRepository lineRepository;
    private StationRepository stationRepository;
    private SectionRepository sectionRepository;

    public LineService(LineRepository lineRepository, StationRepository stationRepository, SectionRepository sectionRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
        this.sectionRepository = sectionRepository;
    }

    @Transactional
    public LineResponse saveLine(LineRequest lineRequest) {
        Station upStation = stationRepository.findById(lineRequest.getUpStationId()).orElseThrow();
        Station downStation = stationRepository.findById(lineRequest.getDownStationId()).orElseThrow();

        Line createdline = lineRepository.save(new Line(
                lineRequest.getName(),
                lineRequest.getColor())
        );

        Section section = new Section(
                createdline,
                upStation,
                downStation,
                lineRequest.getDistance()
        );
        sectionRepository.save(section);
        createdline.addSections(section);

        return LineResponse.of(createdline);
    }

    public List<LineResponse> findAllLines() {
        return lineRepository.findAll().stream()
                .map(LineResponse::of)
                .collect(Collectors.toList());
    }

    public LineResponse findLineById(Long id) {
        Line line = lineRepository.findById(id).orElseThrow();
        return LineResponse.of(line);
    }

    @Transactional
    public void updateLine(Long id, LineUpdateRequest lineUpdateRequest) {
        Line line = lineRepository.findById(id).orElseThrow();
        line.changeName(lineUpdateRequest.getName());
        line.changeColor(lineUpdateRequest.getColor());

        lineRepository.save(line);
    }

    @Transactional
    public void deleteLine(Long id) {
        Optional<Line> lineOptional = lineRepository.findById(id);
        if (lineOptional.isPresent()) {
            lineRepository.delete(lineOptional.get());
        } else {
            throw new EntityNotFoundException("Line with id " + id + " not found");
        }
    }
}
