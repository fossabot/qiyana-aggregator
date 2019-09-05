package ca.softwarespace.qiyanna.dataaggregator.controllers;

import ca.softwarespace.qiyanna.dataaggregator.services.MatchesCollectionService;
import ca.softwarespace.qiyanna.dataaggregator.services.SummonerService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
//@RequiredArgsConstructor
@RequestMapping("/summoner")
public class SummonerController {

  private final SummonerService summonerService;

  private final MatchesCollectionService matchesCollectionService;

  @Autowired
  public SummonerController(SummonerService summonerService,
      MatchesCollectionService matchesCollectionService) {
    this.summonerService = summonerService;
    this.matchesCollectionService = matchesCollectionService;
  }

//  @GetMapping("/{name}")
//  public SummonerDto getSummonerByName(
//      @ApiParam(example = "Marcarrian")
//      @PathVariable String name,
//      @ApiParam(example = "EUW")
//      @RequestParam() String regionName) {
//    return summonerService.getSummonerByName(name, regionName);
//  }

  @GetMapping("/{sName}")
  public String testAggregateV2(@PathVariable String sName, @RequestParam String regionName) {
    matchesCollectionService.prepareAggregationV2(sName, regionName, null);
    return "Welcome to V2";
  }

}
