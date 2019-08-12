package ca.softwarespace.qiyanna.dataaggregator.models.Champions;

import ca.softwarespace.qiyanna.dataaggregator.models.Image;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ChampionDto2 {

  private List<String> allytips;
  private List<String> ennemytips;
  private int id;
  private Image image;
  private ChampionInfo championInfo;
  private String key;
  private String lore;
  private ChampionPassive passive;
  private List<ChampionSpell> spells;
  private ChampionBaseStats stats;
  private ChampionPerformanceStats performanceStatsDto;

}
