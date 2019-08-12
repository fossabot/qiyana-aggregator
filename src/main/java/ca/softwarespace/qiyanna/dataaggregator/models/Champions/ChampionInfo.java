package ca.softwarespace.qiyanna.dataaggregator.models.Champions;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ChampionInfo {
  private int attack;
  private int defense;
  private int difficulty;
  private int magic;
}
