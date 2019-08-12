package ca.softwarespace.qiyanna.dataaggregator.services;

import ca.softwarespace.qiyanna.dataaggregator.models.AggregatedChampionDto;
import ca.softwarespace.qiyanna.dataaggregator.models.ChampionDto;
import java.util.ArrayList;
import java.util.List;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ChampionService.class)
public class ChampionServiceTest {

  @Autowired
  private ChampionService championService;

  private static List<ChampionDto> viChampions = new ArrayList<>();
  private static final String VI_NAME = "Vi";

  @BeforeClass
  public static void setup() {

    viChampions.add(ChampionDto.builder()
        .name(VI_NAME)
        .kills(7)
        .deaths(3)
        .assists(8)
        .cs(120)
        .gold(11452)
        .csPerMin(5.7)
        .isWinner(true)
        .build());

    viChampions.add(ChampionDto.builder()
        .name(VI_NAME)
        .kills(3)
        .deaths(2)
        .assists(12)
        .cs(144)
        .gold(13633)
        .csPerMin(6.1)
        .isWinner(true)
        .build());

    viChampions.add(ChampionDto.builder()
        .name(VI_NAME)
        .kills(12)
        .deaths(4)
        .assists(3)
        .cs(167)
        .gold(14200)
        .csPerMin(5.4)
        .isWinner(false)
        .build());
  }

  @Test
  public void aggregateChampion_shouldAggregateStatsCorrectly() {
    AggregatedChampionDto aggregatedChampionDto = championService.aggregateChampion(viChampions, VI_NAME);
    double averageViKills = ((7 + 3 + 12) / (double) 3);
    double averageViDeaths = ((3 + 2 + 4) / (double) 3);
    double averageViAssists = ((8 + 12 + 3) / (double) 3);
    double averageViCs = ((120 + 144 + 167) / (double) 3);
    double averageViGold = ((11452 + 13633 + 14200) / (double) 3);
    double averageCsPerMin = ((5.7 + 6.1 + 5.4) / (double) 3);
    int played = 3;
    int wins = 2;
    int losses = 1;
    double winrate = (double) wins / ((double) wins + (double) losses);

    assertEquals(VI_NAME, aggregatedChampionDto.getName());
    assertEquals(averageViKills, aggregatedChampionDto.getAverageKills(), 0.0f);
    assertEquals(averageViDeaths, aggregatedChampionDto.getAverageDeaths(), 0.0f);
    assertEquals(averageViAssists, aggregatedChampionDto.getAverageAssists(), 0.0f);
    assertEquals(averageViCs, aggregatedChampionDto.getAverageCs(), 0.0f);
    assertEquals(averageViGold, aggregatedChampionDto.getAverageGold(), 0.0f);
    assertEquals(averageCsPerMin, aggregatedChampionDto.getAverageCsPerMin(), 0.0f);
    assertEquals(played, aggregatedChampionDto.getPlayed());
    assertEquals(wins, aggregatedChampionDto.getWins());
    assertEquals(losses, aggregatedChampionDto.getLosses());
    assertEquals(winrate, aggregatedChampionDto.getWinrate(), 0.0f);
  }

}
