package subway.integration;

import static org.assertj.core.api.Assertions.*;

import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import subway.dto.InitialSectionAddRequest;
import subway.dto.SectionAddRequest;
import subway.dto.SectionDeleteRequest;

@DisplayName("구간 관련 기능")
public class SectionIntegrationTest extends IntegrationTest{

    static Stream<Arguments> addInitialSectionInvalidRequest() {
        return Stream.of(
            Arguments.of(new InitialSectionAddRequest(0L, 1L, 2L, 1)),
            Arguments.of(new InitialSectionAddRequest(1L, 0L, 2L, 1)),
            Arguments.of(new InitialSectionAddRequest(1L, 1L, 0L, 1)),
            Arguments.of(new InitialSectionAddRequest(1L, 1L, 2L, 0)),
            Arguments.of(new InitialSectionAddRequest(2L, 1L, 2L, 1))
        );
    }

    @Test
    @DisplayName("구간이 없는 노선에 구간을 추가한다.")
    void addInitialSectionTest() {
        InitialSectionAddRequest initialSectionAddRequest = new InitialSectionAddRequest(1L, 1L, 2L, 1);

        ExtractableResponse<Response> response = RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(initialSectionAddRequest)
            .when().post("/sections/initial")
            .then().log().all()
            .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    //TODO: 1L에는 없음 2L 아이디 호선에는 역 있음
    @ParameterizedTest(name = "구간이 있는 노선에 첫 구간을 추가한다.")
    @MethodSource("addInitialSectionInvalidRequest")
    void addInitialSectionTestFail(InitialSectionAddRequest initialSectionAddRequest) {
        ExtractableResponse<Response> response = RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(initialSectionAddRequest)
            .when().post("/sections/initial")
            .then().log().all()
            .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("구간이 있는 노선에 구간을 추가한다.")
    void addSectionTest(SectionAddRequest sectionAddRequest) {
        ExtractableResponse<Response> response = RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(sectionAddRequest)
            .when().post("/sections")
            .then().log().all()
            .extract();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @ParameterizedTest(name = "구간이 있는 노선에 첫 구간을 추가한다.")
    @MethodSource("addSectionInvalidRequest")
    void addSectionTestFail(SectionAddRequest sectionAddRequest) {
        ExtractableResponse<Response> response = RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(sectionAddRequest)
            .when().post("/sections")
            .then().log().all()
            .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    static Stream<Arguments> addSectionInvalidRequest() {
        return Stream.of(
            Arguments.of(new SectionAddRequest(0L, 1L, 2L, 1L, 0)),
            Arguments.of(new SectionAddRequest(1L, 1L, 0L, 2L, 1)),
            Arguments.of(new SectionAddRequest(1L, 1L, 1L, 0L, 1)),
            Arguments.of(new SectionAddRequest(1L, 1L, 1L, 2L, 0)),
            Arguments.of(new SectionAddRequest(1L, 2L, 1L, 2L, 1))
        );
    }

    @Test
    @DisplayName("노선에서 역을 삭제한다.")
    void deleteSectionTest() {
        SectionDeleteRequest sectionDeleteRequest = new SectionDeleteRequest(1L, 2L);

        ExtractableResponse<Response> response = RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(sectionDeleteRequest)
            .when().delete("/sections")
            .then().log().all()
            .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }
}
