package subway;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import subway.presentation.LineRequest;
import subway.presentation.LineResponse;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DisplayName("지하철 노선 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Sql(scripts = "classpath:truncate-tables.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class LineAcceptanceTest {
    /**
     * Given: 새로운 지하철 노선 정보를 입력하고,
     * When: 관리자가 노선을 생성하면,
     * Then: 해당 노선이 생성되고 노선 목록에 포함된다.
     */
    @Test
    @DisplayName("지하철 노선을 생성한다.")
    void createLine() {
        // given
        TestStation.createStation("강남역");
        TestStation.createStation("을지로4가역");
        LineRequest newLine = new LineRequest("신분당선", "bg-red-600", 1L, 2L, 10);

        // when
        ExtractableResponse<Response> response = TestLine.createLine(newLine);

        // then
        List<String> allLineNames = TestLine.findAllLineNames();
        Assertions.assertThat(allLineNames).containsAnyOf("신분당선");
        assertThat(response.statusCode()).isEqualTo(201);
    }

    /**
     * Given: 여러 개의 지하철 노선이 등록되어 있고,
     * When: 관리자가 지하철 노선 목록을 조회하면,
     * Then: 모든 지하철 노선 목록이 반환된다.
     */
    @Test
    @DisplayName("지하철 노선 목록을 조회한다.")
    void retrieveAllLines() {
        // given
        TestStation.createStation("강남역");
        TestStation.createStation("을지로4가역");
        TestStation.createStation("또다른역");

        LineRequest sinbundangLineRequest = new LineRequest("신분당선", "bg-red-600", 1L, 2L, 10);
        LineRequest fifthLineRequest = new LineRequest("5호선", "bg-purple-400", 1L, 3L, 10);

        TestLine.createLine(sinbundangLineRequest);
        TestLine.createLine(fifthLineRequest);

        // when
        List<String> allLineNames = TestLine.findAllLineNames();

        // then
        Assertions.assertThat(allLineNames).contains("신분당선", "5호선");
    }

    /**
     * 지하철 노선 조회
     * Given: 특정 지하철 노선이 등록되어 있고,
     * When: 관리자가 해당 노선을 조회하면,
     * Then: 해당 노선의 정보가 반환된다.
     */
    @Test
    @DisplayName("지하철 노선을 조회한다.")
    void retrieveLine() {
        // given
        TestStation.createStation("강남역");
        TestStation.createStation("을지로4가역");
        TestStation.createStation("또다른역");

        LineRequest sinbundangLineRequest = new LineRequest("신분당선", "bg-red-600", 1L, 2L, 10);
        LineRequest fifthLineRequest = new LineRequest("5호선", "bg-purple-400", 1L, 3L, 10);

        TestLine.createLine(sinbundangLineRequest);

        ExtractableResponse<Response> response = TestLine.createLine(fifthLineRequest);
        String fifthLineId = response.body().jsonPath().getString("id");

        // when
        LineResponse findline = TestLine.findByLineId(fifthLineId);

        // then
        assertThat(findline.getName()).isEqualTo("5호선");
        assertThat(findline.getColor()).isEqualTo("bg-purple-400");
        assertThat(findline.getStations().size()).isEqualTo(2);
    }

    /**
     * 지하철 노선 수정
     * Given: 특정 지하철 노선이 등록되어 있고,
     * When: 관리자가 해당 노선을 수정하면,
     * Then: 해당 노선의 정보가 수정된다.
     */
    @Test
    @DisplayName("지하철 노선을 수정한다.")
    void updateLine() {
        // given
        TestStation.createStation("강남역");
        TestStation.createStation("을지로4가역");
        LineRequest newLine = new LineRequest("신분당선", "bg-red-600", 1L, 2L, 10);

        LineRequest sinbundangLineRequest = new LineRequest("신분당선", "bg-red-600", 1L, 2L, 10);

        ExtractableResponse<Response> response = TestLine.createLine(sinbundangLineRequest);
        String sinbundangLineId = response.body().jsonPath().getString("id");

        // when
        TestLine.updateLine(sinbundangLineId, "신분당선2호선", "bg-red-700");
        LineResponse findline = TestLine.findByLineId(sinbundangLineId);

        // then
        assertThat(findline.getName()).isEqualTo("신분당선2호선");
        assertThat(findline.getColor()).isEqualTo("bg-red-700");
        assertThat(findline.getStations().size()).isEqualTo(2);
    }


    /**
     * 지하철 노선 삭제
     * Given: 특정 지하철 노선이 등록되어 있고,
     * When: 관리자가 해당 노선을 삭제하면,
     * When: 관리자가 해당 노선을 삭제하면,
     */
    @Test
    @DisplayName("지하철 노선을 삭제한다.")
    void deleteLine() {
        // given
        TestStation.createStation("강남역");
        TestStation.createStation("을지로4가역");
        LineRequest newLine = new LineRequest("신분당선", "bg-red-600", 1L, 2L, 10);

        LineRequest sinbundangLineRequest = new LineRequest("신분당선", "bg-red-600", 1L, 2L, 10);

        ExtractableResponse<Response> response = TestLine.createLine(sinbundangLineRequest);
        String sinbundangLineId = response.body().jsonPath().getString("id");

        // when
        TestLine.deleteLine(sinbundangLineId);
        List<String> allLineNames = TestLine.findAllLineNames();

        // then
        Assertions.assertThat(allLineNames).doesNotContain("신분당선");
        Assertions.assertThat(allLineNames.size()).isEqualTo(0);
    }

}
