package pl.lodz.nbd.repository;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;

public abstract class AbstractMongoRepository {
    private MongoClient mongoClient;
    private MongoDatabase guesthouseDB;
}
