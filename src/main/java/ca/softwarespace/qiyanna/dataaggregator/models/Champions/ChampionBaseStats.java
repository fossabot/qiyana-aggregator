package ca.softwarespace.qiyanna.dataaggregator.models.Champions;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ChampionBaseStats {

 private double armor;
 private double armorperlevel;
 private double attackdamage;
 private double attackdamageperlevel;
 private double attackrange;
 private double attackspeedoffset;
 private double attackspeedperlevel;
 private double crit;
 private double critperlevel;
 private double hp;
 private double hpperlevel;
 private double hpregen;
 private double hpregenperlevel;
 private double movespeed;
 private double mp;
 private double mpperlevel;
 private double mpregen;
 private double mpregenperlevel;
 private double spellblock;
 private double spellblockperlevel;

}
