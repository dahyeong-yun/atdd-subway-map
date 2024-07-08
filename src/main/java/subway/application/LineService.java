package subway.application;

import org.springframework.stereotype.Service;
import subway.domain.Line;
import subway.domain.Station;
import subway.infrastructure.LineRepository;
import subway.infrastructure.StationRepository;
import subway.presentation.LineUpdateRequest;
import subway.presentation.LineRequest;
import subway.presentation.LineResponse;

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
        List<Station> stations = List.of(upStation, downStation);

        Line line = lineRepository.save(new Line(
                lineRequest.getName(),
                lineRequest.getColor(),
                lineRequest.getUpStationId(),
                lineRequest.getDownStationId(),
                lineRequest.getDistance(),
                stations)
        );
        return LineResponse.of(line.getId(), line.getName(), line.getColor(), stations);
    }

    public List<LineResponse> findAllLines() {
        return lineRepository.findAll().stream()
                .map(this::createLineResponse)
                .collect(Collectors.toList());
    }

    public LineResponse findLineById(Long id) {
        Line line = lineRepository.findById(id).orElseThrow();
        return createLineResponse(line);
    }

    public void updateLine(Long id, LineUpdateRequest lineUpdateRequest) {
        Line line = lineRepository.findById(id).orElseThrow();
        line.changeName(lineUpdateRequest.getName());
        line.changeColor(lineUpdateRequest.getColor());

        lineRepository.save(line);
    }

    private LineResponse createLineResponse(Line line) {
        return new LineResponse(
                line.getId(),
                line.getName(),
                line.getColor(),
                List.of(
                        stationRepository.findById(line.getUpStationId()).orElseThrow(),
                        stationRepository.findById(line.getDownStationId()).orElseThrow()
                )
        );
    }
}
