package ca.softwarespace.qiyanna.dataaggregator.util;

import com.mongodb.MongoClientSettings;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import java.util.Collections;
import org.springframework.stereotype.Component;

@Component
public class MongoDBClient {

  private MongoClient mongoClient;

  public MongoDBClient() {
    mongoClient = MongoClients.create(
        MongoClientSettings.builder()
            .applyToClusterSettings(builder ->
                builder.hosts(Collections.singletonList(new ServerAddress("localhost", 27017))))
            .build());
  }

  public MongoClient getMongoClient() {
    return mongoClient;
  }

  public MongoDatabase getMatchDatabase() {
    return this.mongoClient.getDatabase("orianna");
  }
}
