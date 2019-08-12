package ca.softwarespace.qiyanna.dataaggregator.models.Champions;

import ca.softwarespace.qiyanna.dataaggregator.models.Image;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ChampionPassive {
  private String description;
  private String name;
  private Image image;

}
