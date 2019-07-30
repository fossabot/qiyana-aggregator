package ca.softwarespace.qiyanna.dataaggregator.models;

import com.merakianalytics.orianna.types.common.Division;
import com.merakianalytics.orianna.types.common.Tier;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class SummonerDto {

  private String name;
  private int level;
  private Tier tier;
  private Division division;
  private int leaguePoints;
  private int wins;
  private int losses;
  private double winrate;

}
