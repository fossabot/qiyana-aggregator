package ca.softwarespace.qiyanna.dataaggregator.util;

import com.merakianalytics.orianna.types.common.Queue;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: Steve Mbiele Date: 5/15/2019
 */
public class Constants {

  public static final String MATCH_LIST_BEGIN_INDEX_PARAMETER = "beginIndex";
  public static final String MATCHES_NODE_IN_MATCH_LIST = "matches";
  public static final int SOLO_QUEUE_RANKED_ID = 420;
  private Constants() {
  }

  public static List<Queue> getQueuesList() {
    ArrayList<Queue> queues = new ArrayList<>();
    queues.add(Queue.OVERCHARGE);
    queues.add(Queue.SIEGE);
    queues.add(Queue.NEXUS_SIEGE);
    queues.add(Queue.ARURF_5X5);
    queues.add(Queue.ARURF);
    queues.add(Queue.ARAM_5x5);
    queues.add(Queue.ARAM);
    queues.add(Queue.ARSR_5x5);
    queues.add(Queue.ASCENSION_5x5);
    queues.add(Queue.ASSASSINATE_5x5);
    queues.add(Queue.BILGEWATER_5x5);
    queues.add(Queue.DARKSTAR_3x3);
    queues.add(Queue.FIRSTBLOOD_1x1);
    queues.add(Queue.FIRSTBLOOD_2x2);
    queues.add(Queue.NORMAL_3x3);
    queues.add(Queue.NORMAL_5x5_DRAFT);
    queues.add(Queue.NORMAL_3x3);
    queues.add(Queue.NORMAL_5x5_BLIND);
    queues.add(Queue.NORMAL_3X3_BLIND_PICK);
    queues.add(Queue.ONEFORALL_5x5);
    queues.add(Queue.SR_6x6);
    queues.add(Queue.GROUP_FINDER_5x5);
    queues.add(Queue.KING_PORO_5x5);
    queues.add(Queue.ONEFORALL_MIRRORMODE_5x5);
    queues.add(Queue.RANKED_PREMADE_3x3);
    queues.add(Queue.RANKED_PREMADE_5x5);
    queues.add(Queue.RANKED_SOLO_5x5);
    queues.add(Queue.RANKED_TEAM_3x3);
    queues.add(Queue.RANKED_TEAM_5x5);
    queues.add(Queue.RANKED_FLEX_SR);
    queues.add(Queue.RANKED_FLEX_TT);
    queues.add(Queue.TEAM_BUILDER_DRAFT_RANKED_5x5);
    queues.add(Queue.TEAM_BUILDER_DRAFT_UNRANKED_5x5);
    queues.add(Queue.TEAM_BUILDER_RANKED_SOLO);
    queues.add(Queue.COUNTER_PICK);
    queues.add(Queue.DEFINITELY_NOT_DOMINION_5x5);
    queues.add(Queue.TB_BLIND_SUMMONERS_RIFT_5x5);
    queues.add(Queue.CUSTOM);
    queues.add(Queue.HEXAKILL);
    return queues;
  }
}
