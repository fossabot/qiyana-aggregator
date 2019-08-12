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
public class ChampionSpell {
  private String cooldownBurn;
  private String costBurn;
  private String rangeBurn;
  private String description;
  private List<String> effectBrurn;
  private int maxrank;
  private String name;
  private Image image;
}
