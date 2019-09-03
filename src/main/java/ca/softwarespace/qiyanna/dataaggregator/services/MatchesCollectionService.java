package ca.softwarespace.qiyanna.dataaggregator.services;

import ca.softwarespace.qiyanna.dataaggregator.models.MatchDto;
import ca.softwarespace.qiyanna.dataaggregator.models.generated.tables.records.SummonerRecord;
import ca.softwarespace.qiyanna.dataaggregator.util.Constants;
import ca.softwarespace.qiyanna.dataaggregator.util.RegionUtil;
import com.merakianalytics.orianna.Orianna;
import com.merakianalytics.orianna.types.common.Region;
import com.merakianalytics.orianna.types.common.Season;
import com.merakianalytics.orianna.types.core.match.Match;
import com.merakianalytics.orianna.types.core.match.MatchHistory;
import com.merakianalytics.orianna.types.core.match.Participant;
import com.merakianalytics.orianna.types.core.summoner.Summoner;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.joda.time.DateTime;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;


/**
 * Author: Steve Mbiele Date: 5/15/2019
 */
@Service
@Log4j2
@RequiredArgsConstructor
public class MatchesCollectionService {

  @Autowired
  private DSLContext dsl;

  public MatchHistory filterMatchHistory(Summoner summoner) {
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

//TODO: make this async
  @Async
  public void prepareAggregationV2(String summonerName, String regionName, Integer startSeasonId) {
    Season startSeason;
    Region region = RegionUtil.getRegionByTag(regionName);
    Summoner summoner = Orianna.summonerNamed(summonerName).withRegion(region).get();

    if (startSeasonId != null) {
      startSeason = Season.withId(startSeasonId);
    } else {
      startSeason = Season.getLatest();
    }
    aggregateV2(summoner, region, startSeason);
  }

//  TODO: save the summoner is the SQL database,
  private void aggregateV2(Summoner summoner, Region region, Season season) {
    HashSet<String> unpulledSummonerIds = new HashSet<>();
    unpulledSummonerIds.add(summoner.getId());

    HashSet<String> pulledSummonerIds = new HashSet<>();
    HashSet<Long> unpulledMatchIds = new HashSet<>();
    HashSet<Long> pulledMatchIds = new HashSet<>();

    while (!unpulledSummonerIds.isEmpty()) {

      // Get a new summoner from our list of unpulled summoners and pull their match history
      final String newSummonerId = unpulledSummonerIds.iterator().next();

      final SummonerRecord record = dsl.selectFrom(
          ca.softwarespace.qiyanna.dataaggregator.models.generated.tables.Summoner.SUMMONER)
          .where(
              ca.softwarespace.qiyanna.dataaggregator.models.generated.tables.Summoner.SUMMONER.ACCOUNTID
                  .eq(newSummonerId))
          .fetchAny();
      final Summoner newSummoner = Summoner.withId(newSummonerId).withRegion(region).get();
      final MatchHistory matches;
      if (record != null) {
        matches = filterMatchHistory(newSummoner, season,
            millsToLocalDateTime(record.getRevisiondate()));
      } else {
        matches = filterMatchHistory(newSummoner, season, null);
      }

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
    //TODO pull other information like rank and all
  }

  private MatchHistory filterMatchHistory(Summoner summoner, Season season, DateTime startTime) {
    if (startTime != null) {
      return Orianna.matchHistoryForSummoner(summoner).withSeasons(Season.getLatest())
          .withQueues(Constants.getQeuesList()).withStartTime(startTime).get();
    } else {
      return Orianna.matchHistoryForSummoner(summoner).withSeasons(Season.getLatest())
          .withQueues(Constants.getQeuesList()).get();
    }
  }

  private static DateTime millsToLocalDateTime(long millis) {
    return new DateTime(millis);
  }

}

