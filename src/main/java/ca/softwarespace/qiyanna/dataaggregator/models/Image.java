package ca.softwarespace.qiyanna.dataaggregator.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Image {

  private String full;
  private String sprite;
  private String group;
  private int h;
  private int w;
  private int x;
  private int y;
  private String url;
}
