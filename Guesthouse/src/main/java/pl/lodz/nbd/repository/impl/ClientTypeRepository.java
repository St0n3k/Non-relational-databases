package pl.lodz.nbd.repository.impl;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.InsertOneResult;
import org.bson.Document;
import org.bson.conversions.Bson;
import pl.lodz.nbd.model.ClientTypes.ClientType;
import pl.lodz.nbd.repository.AbstractMongoRepository;
import pl.lodz.nbd.repository.Repository;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ClientTypeRepository extends AbstractMongoRepository implements Repository<ClientType> {

    MongoCollection<ClientType> clientTypeCollection = mongoDatabase.getCollection("client_types", ClientType.class);

    public ClientTypeRepository() {
        clientTypeCollection.drop();
        clientTypeCollection = mongoDatabase.getCollection("client_types", ClientType.class);
        Document index = new Document("_clazz", 1);
        IndexOptions options = new IndexOptions().unique(true);
        clientTypeCollection.createIndex(index, options);
    }

    public Optional<ClientType> add(ClientType clientType) {
        InsertOneResult insertOneResult = clientTypeCollection.insertOne(clientType);
        if (insertOneResult.wasAcknowledged()) {
            return Optional.of(clientType);
        } else {
            return Optional.empty();
        }
    }


    public void remove(ClientType clientType) {
        Bson filter = Filters.eq("_id", clientType.getUuid());
        clientTypeCollection.findOneAndDelete(filter);
    }


    public Optional<ClientType> getById(UUID id) {
        Bson filter = Filters.eq("_id", id);
        return Optional.ofNullable(clientTypeCollection.find(filter).first());
    }

    @Override
    public boolean update(ClientType ct) {
        Bson filter = Filters.eq("_id", ct.getUuid());
        Bson update = Updates.set("discount", ct.getDiscount());

        return clientTypeCollection.updateOne(filter, update).wasAcknowledged();
    }


    public List<ClientType> getAll() {
        return clientTypeCollection.find().into(new ArrayList<>());
    }

    public ClientType getByType(Class type) {
        Bson filter = Filters.eq("_clazz", type.getName());
        ClientType clientType = clientTypeCollection.find(filter).first();
        if (clientType == null) {
            try {
                Optional<ClientType> ct = add((ClientType) type.getConstructor().newInstance());
                if (ct.isPresent()) return ct.get();
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                     NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        return clientType;
    }
}
