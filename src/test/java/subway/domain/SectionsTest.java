package subway.domain;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import subway.exception.DomainException;

class SectionsTest {
    private List<Section> sectionsList = new ArrayList<>();

    @BeforeEach
    public void setUp() {
        sectionsList.add(new Section(3L, 3L, 4L, 1L, 10));
        sectionsList.add(new Section(1L, 1L, 2L, 1L, 10));
        sectionsList.add(new Section(2L, 2L, 3L, 1L, 10));
    }

    @Test
    @DisplayName("구간을 순서대로 정렬한다.")
    void lineUpTest() {
        Sections sections = new Sections(sectionsList);
        assertAll(
            () -> assertThat(sections.findFirstStationId()).isEqualTo(1L),
            () -> assertThat(sections.findLastStationId()).isEqualTo(4L));
    }

    @Test
    @DisplayName("구간 사이에 역을 추가하는 경우 sections가 비어있으면 예외가 발생한다.")
    void validateAddingSectionThrowByNotInitialized() {
        //given
        sectionsList.clear();
        final Sections sections = new Sections(sectionsList);

        //then
        Assertions.assertThatThrownBy(() -> sections.validateAddingSection(5L))
                .isInstanceOf(DomainException.class)
                .hasMessage("LINE_IS_NOT_INITIALIZED");
    }

    @Test
    @DisplayName("구간 사이에 역을 추가하는 경우 추가하려는 station이 이미 sections에 존재하면 예외가 발생한다.")
    void validateAddingSectionThrowByAlreadyExistedStation() {
        //given
        final Sections sections = new Sections(sectionsList);

        //then
        Assertions.assertThatThrownBy(() -> sections.validateAddingSection(1L))
                .isInstanceOf(DomainException.class)
                .hasMessage("STATION_ALREADY_EXIST_IN_LINE");
    }
}
