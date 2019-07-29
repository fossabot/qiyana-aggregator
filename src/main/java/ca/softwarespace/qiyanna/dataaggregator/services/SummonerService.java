package ca.softwarespace.qiyanna.dataaggregator.services;

import ca.softwarespace.qiyanna.dataaggregator.models.MatchDto;
import ca.softwarespace.qiyanna.dataaggregator.models.SummonerDto;
import com.merakianalytics.orianna.Orianna;
import com.merakianalytics.orianna.types.common.Queue;
import com.merakianalytics.orianna.types.common.Region;
import com.merakianalytics.orianna.types.core.summoner.Summoner;
import java.util.HashSet;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@Log4j2
@RequiredArgsConstructor
public class SummonerService {

  private final MatchesCollectionService matchesCollectionService;

  public SummonerDto getSummonerByName(String name) {
    Summoner summoner = Orianna.summonerNamed(name).withRegion(Region.NORTH_AMERICA).get();
    Set<MatchDto> matches = new HashSet<>();
    try {
      matches = matchesCollectionService.getMatchHistoryBySummoner(summoner).get();
    } catch(Exception e) {
      e.printStackTrace();
    }

    return SummonerDto.builder()
        .name(summoner.getName())
        .tier(summoner.getLeaguePosition(Queue.RANKED_SOLO_5x5).getTier())
        .division(summoner.getLeaguePosition(Queue.RANKED_SOLO_5x5).getDivision())
        .matches(matches)
        .build();
  }

}
