package subway;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import subway.presentation.LineRequest;
import subway.presentation.SectionRequest;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DisplayName("지하철 구간 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Sql(scripts = "classpath:truncate-tables.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class SectionAcceptanceTest {
    /**
     * Given: 새로운 지하철 구간 정보를 입력하고,
     * When: 관리자가 구간을 생성하면,
     * Then: 해당 구간선이 생성된다.
     * Then: 해당 구간을 포함한 노선의 하행이, 추가된 구간의 하행과 동일하게 변경된다.
     */
    @Test
    @DisplayName("지하철 구간을 생성한다.")
    void createLine() {
        // given
        Long 강남역 = Long.valueOf(StationSteps.createStation("강남역").body().jsonPath().getString("id"));
        Long 을지로4가역 = Long.valueOf(StationSteps.createStation("을지로4가역").body().jsonPath().getString("id"));
        LineRequest newLine = new LineRequest("신분당선", "bg-red-600", 강남역, 을지로4가역, 10);
        Long 신분당선 = Long.valueOf(LineSteps.createLine(newLine).body().jsonPath().getString("id"));

        Long 또다른역 = Long.valueOf(StationSteps.createStation("또다른역").body().jsonPath().getString("id"));

        // when
        ExtractableResponse<Response> response = SectionSteps.createSection(신분당선, new SectionRequest(을지로4가역, 또다른역, 10));

        // then
        assertThat(response.statusCode()).isEqualTo(201);
        assertThat(Long.valueOf(response.body().jsonPath().getString("lineId"))).isEqualTo(신분당선);
        assertThat(Long.valueOf(response.body().jsonPath().getString("downStationId"))).isEqualTo(또다른역);
    }

}
