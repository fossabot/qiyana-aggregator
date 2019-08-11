package ca.softwarespace.qiyanna.dataaggregator.services;

import ca.softwarespace.qiyanna.dataaggregator.models.MatchDto;
import ca.softwarespace.qiyanna.dataaggregator.util.Constants;
import com.merakianalytics.orianna.Orianna;
import com.merakianalytics.orianna.types.common.Queue;
import com.merakianalytics.orianna.types.common.Region;
import com.merakianalytics.orianna.types.common.Season;
import com.merakianalytics.orianna.types.core.match.Match;
import com.merakianalytics.orianna.types.core.match.MatchHistory;
import com.merakianalytics.orianna.types.core.match.Participant;
import com.merakianalytics.orianna.types.core.summoner.Summoner;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;


/**
 * Author: Steve Mbiele Date: 5/15/2019
 */
@Service
@Log4j2
@RequiredArgsConstructor
public class MatchesCollectionService {

  private MatchHistory filterMatchHistory(Summoner summoner) {
    return Orianna.matchHistoryForSummoner(summoner).withSeasons(Season.getLatest())
        .withQueues(Constants.getQeuesList()).get();
  }

  @Async
  public void oriannaTest(String summonerName) {
    Summoner summoner = Orianna.summonerNamed(summonerName).get();
    Region region = summoner.getRegion();
    aggregate(summoner, region);
  }

  @Async
  public void oriannaTest(String summonerName, String regionName) {
    Summoner summoner = Orianna.summonerNamed(summonerName).withRegion(Region.valueOf(regionName))
        .get();
    Region region = summoner.getRegion();
    aggregate(summoner, region);
  }

  @Async
  public CompletableFuture<Set<MatchDto>> getMatchHistoryBySummoner(Summoner summoner) {
    MatchHistory matches = filterMatchHistory(summoner);
    Set<MatchDto> matchDtos = new HashSet<>();

    for (Match match : matches) {
      Match pulledMatch = Match.withId(match.getId()).get();
      MatchDto matchDto = MatchDto.builder()
          .id(pulledMatch.getId())
          .build();
      matchDtos.add(matchDto);
    }

    return CompletableFuture.completedFuture(matchDtos);
  }

  private void aggregate(Summoner summoner, Region region) {
    HashSet<String> unpulledSummonerIds = new HashSet<>();
    unpulledSummonerIds.add(summoner.getId());

    HashSet<String> pulledSummonerIds = new HashSet<>();
    HashSet<Long> unpulledMatchIds = new HashSet<>();
    HashSet<Long> pulledMatchIds = new HashSet<>();

    while (!unpulledSummonerIds.isEmpty()) {
      // Get a new summoner from our list of unpulled summoners and pull their match history
      final String newSummonerId = unpulledSummonerIds.iterator().next();
      final Summoner newSummoner = Summoner.withId(newSummonerId).withRegion(region).get();
      final MatchHistory matches = filterMatchHistory(newSummoner);
      for (final Match match : matches) {
        if (!pulledMatchIds.contains(match.getId())) {
          unpulledMatchIds.add(match.getId());
        }
      }
      unpulledSummonerIds.remove(newSummonerId);
      pulledSummonerIds.add(newSummonerId);

      while (!unpulledMatchIds.isEmpty()) {
        // Get a random match from our list of matches
        final long newMatchId = unpulledMatchIds.iterator().next();
        final Match newMatch = Match.withId(newMatchId).withRegion(region).get();
        for (final Participant p : newMatch.getParticipants()) {
          if (!pulledSummonerIds.contains(p.getSummoner().getId())) {
            unpulledSummonerIds.add(p.getSummoner().getId());
          }
        }
        // The above lines will trigger the match to load its data by iterating over all the participants.
        // If you have a database in your datapipeline, the match will automatically be stored in it.
        unpulledMatchIds.remove(newMatchId);
        pulledMatchIds.add(newMatchId);
      }
    }
  }
}

