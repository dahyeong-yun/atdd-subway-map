package subway.application;

import org.springframework.stereotype.Service;
import subway.domain.Line;
import subway.domain.Station;
import subway.infrastructure.LineRepository;
import subway.infrastructure.StationRepository;
import subway.presentation.LineRequest;
import subway.presentation.LineResponse;
import subway.presentation.LineUpdateRequest;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LineService {
    private LineRepository lineRepository;
    private StationRepository stationRepository;

    public LineService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    public LineResponse saveStation(LineRequest lineRequest) {
        Station upStation = stationRepository.findById(lineRequest.getUpStationId()).orElseThrow();
        Station downStation = stationRepository.findById(lineRequest.getDownStationId()).orElseThrow();

        Line createdline = lineRepository.save(new Line(
                lineRequest.getName(),
                lineRequest.getColor(),
                upStation,
                downStation,
                lineRequest.getDistance())
        );

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

    public void updateLine(Long id, LineUpdateRequest lineUpdateRequest) {
        Line line = lineRepository.findById(id).orElseThrow();
        line.changeName(lineUpdateRequest.getName());
        line.changeColor(lineUpdateRequest.getColor());

        lineRepository.save(line);
    }
}
