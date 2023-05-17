package subway.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

import org.jgrapht.graph.WeightedMultigraph;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import fixture.Fixture;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.domain.farecalculator.BasicFareCalculator;
import subway.domain.pathfinder.JgraphtPathFinder;
import subway.domain.pathfinder.LineWeightedEdge;
import subway.dto.PathResponse;

@ExtendWith(MockitoExtension.class)
class PathServiceTest {

    @InjectMocks
    PathService pathService;
    @Mock
    SectionDao sectionDao;
    @Mock
    LineDao lineDao;
    @Mock
    StationDao stationDao;

    @BeforeEach
    void setUp() {
        pathService = new PathService(
                new JgraphtPathFinder(new WeightedMultigraph<>(LineWeightedEdge.class)),
                new BasicFareCalculator(),
                lineDao,
                stationDao,
                sectionDao
        );
    }


    @Test
    @DisplayName("source로 부터 target 까지의 경로와 거리, 요금을 반환한다.")
    void computePath() {
        //given
        when(sectionDao.findAll()).thenReturn(Fixture.SECTIONS);
        when(stationDao.findAll()).thenReturn(Fixture.STATIONS);
        when(lineDao.findAll()).thenReturn(Fixture.LINES);

        //when
        final PathResponse result = pathService.computePath(1L, 8L);

        //then
        assertAll(
                () -> assertThat(result.getDistance()).isEqualTo(15),
                () -> assertThat(result.getPrice()).isEqualTo(1350),
                () -> assertThat(result.getPath()).hasSize(3)
        );
    }
}