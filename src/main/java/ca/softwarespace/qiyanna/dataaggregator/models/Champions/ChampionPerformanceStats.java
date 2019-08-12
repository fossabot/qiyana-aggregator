package ca.softwarespace.qiyanna.dataaggregator.models.Champions;

import com.merakianalytics.orianna.types.common.Tier;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ChampionPerformanceStats {

  private double winRate;
  private double pickRate;
  private double banRate;
  private long nbGames;
  private String region;
  private Tier tier;
}
