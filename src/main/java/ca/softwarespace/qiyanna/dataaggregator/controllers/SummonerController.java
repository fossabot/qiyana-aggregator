package ca.softwarespace.qiyanna.dataaggregator.controllers;

import ca.softwarespace.qiyanna.dataaggregator.models.SummonerDto;
import ca.softwarespace.qiyanna.dataaggregator.services.SummonerService;
import com.merakianalytics.orianna.types.core.staticdata.Champion;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/summoner")
public class SummonerController {

  private final SummonerService summonerService;

  @PostMapping("/{name}")
  public SummonerDto getSummonerByName(
      @ApiParam(example = "Marcarrian")
      @PathVariable String name,
      @ApiParam(example = "EUW")
      @RequestParam() String regionName) {

    return summonerService.getSummonerByName(name, regionName);
  }

}
