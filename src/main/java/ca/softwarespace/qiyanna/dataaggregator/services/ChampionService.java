package ca.softwarespace.qiyanna.dataaggregator.services;

import ca.softwarespace.qiyanna.dataaggregator.models.AggregatedChampionDto;
import ca.softwarespace.qiyanna.dataaggregator.models.ChampionDto;
import ca.softwarespace.qiyanna.dataaggregator.util.AggregatedChampionConsumer;
import ca.softwarespace.qiyanna.dataaggregator.util.RegionUtil;
import com.merakianalytics.orianna.Orianna;
import com.merakianalytics.orianna.types.common.Queue;
import com.merakianalytics.orianna.types.common.Region;
import com.merakianalytics.orianna.types.common.Season;
import com.merakianalytics.orianna.types.core.match.MatchHistory;
import com.merakianalytics.orianna.types.core.match.Participant;
import com.merakianalytics.orianna.types.core.match.ParticipantStats;
import com.merakianalytics.orianna.types.core.summoner.Summoner;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.joda.time.Duration;
import org.springframework.stereotype.Service;

@Service
@Log4j2
@RequiredArgsConstructor
public class ChampionService {

  public CompletableFuture<List<AggregatedChampionDto>> aggregateChampionStatsBySummoner(String summonerName, String championName, String regionName) {
    Region region = RegionUtil.getRegionByTag(regionName);
    Summoner summoner = Orianna.summonerNamed(summonerName)
        .withRegion(region)
        .get();
    // TODO include queue in request when orianna is updated to have updated queue ids
    MatchHistory matches = MatchHistory.forSummoner(summoner)
        .withSeasons(Season.getLatest())
        .withQueues(Queue.TEAM_BUILDER_RANKED_SOLO) // TODO update with actual ranked solo as soon as orianna gets updated
        .withChampions((championName == null || championName.isEmpty()) ?
            Collections.emptySet() :
            Orianna.championsNamed(championName).get())
        .get();
    List<ChampionDto> championsFromMatchHistory = getChampionsFromMatchHistory(summoner, matches);
    List<AggregatedChampionDto> aggregatedChampions = aggregateAllChampions(championsFromMatchHistory);
    return CompletableFuture.completedFuture(aggregatedChampions);
  }

  private List<ChampionDto> getChampionsFromMatchHistory(Summoner summoner, MatchHistory matches) {
    List<ChampionDto> champions = new ArrayList<>();
    matches.stream()
        .filter(match -> !match.isRemake())
        .forEach(match -> {
          Optional<ChampionDto> champion = match.getParticipants().stream()
              .filter(participant -> participant.getSummoner().getAccountId().equals(summoner.getAccountId()))
              .map(participant -> this.buildChampionDto(participant, match.getDuration()))
              .filter(Objects::nonNull)
              .findFirst();
          champion.ifPresent(champions::add);
        });
    return champions;
  }

  private List<AggregatedChampionDto> aggregateAllChampions(List<ChampionDto> champions) {
    Map<String, List<ChampionDto>> championsGroupedByName = champions.stream()
        .collect(Collectors.groupingBy(ChampionDto::getName));
    List<AggregatedChampionDto> aggregatedChampions = new ArrayList<>();
    for (Map.Entry<String, List<ChampionDto>> entry : championsGroupedByName.entrySet()) {
      AggregatedChampionDto aggregatedChampion = aggregateChampion(entry.getValue(), entry.getKey());
      aggregatedChampions.add(aggregatedChampion);
    }
    return aggregatedChampions.stream()
        .sorted(Comparator.comparing(AggregatedChampionDto::getPlayed).reversed())
        .collect(Collectors.toList());
  }

  private ChampionDto buildChampionDto(Participant participant, Duration matchDuration) {
    ParticipantStats stats = participant.getStats();
    if (stats == null) { // TODO check why stats are sometimes null
      return null;
    }

    int cs = stats.getCreepScore() + stats.getNeutralMinionsKilled();
    double matchDurationInMinutes = matchDuration.getStandardMinutes();
    double csPerMin = cs / matchDurationInMinutes;

    return ChampionDto.builder()
        .name(participant.getChampion().getName())
        .kills(stats.getKills())
        .deaths(stats.getDeaths())
        .assists(stats.getAssists())
        .cs(cs)
        .isWinner(participant.getTeam().isWinner())
        .gold(stats.getGoldEarned())
        .csPerMin(csPerMin)
        .build();
  }

  public AggregatedChampionDto aggregateChampion(List<ChampionDto> champions, String name) {
    return champions.stream()
        .collect(AggregatedChampionConsumer::new, AggregatedChampionConsumer::accept, AggregatedChampionConsumer::combine)
        .getAggregatedChampionDto(name);
  }
}