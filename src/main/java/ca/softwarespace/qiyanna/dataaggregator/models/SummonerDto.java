package ca.softwarespace.qiyanna.dataaggregator.models;

import com.merakianalytics.orianna.types.common.Division;
import com.merakianalytics.orianna.types.common.Tier;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder(toBuilder = true)
public class SummonerDto {

  private String name;
  private Tier tier;
  private Division division;
  private Set<MatchDto> matches;

}
