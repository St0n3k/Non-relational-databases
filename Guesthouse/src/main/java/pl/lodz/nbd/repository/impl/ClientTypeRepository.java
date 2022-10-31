package pl.lodz.nbd.repository.impl;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.result.InsertOneResult;
import org.bson.Document;
import org.bson.conversions.Bson;
import pl.lodz.nbd.model.ClientTypes.ClientType;
import pl.lodz.nbd.model.ClientTypes.Gold;
import pl.lodz.nbd.repository.AbstractMongoRepository;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ClientTypeRepository extends AbstractMongoRepository {

    MongoCollection<ClientType> clientTypeCollection = mongoDatabase.getCollection("client_types", ClientType.class);

    public ClientTypeRepository() {
        Document index = new Document("_clazz", 1);
        IndexOptions options = new IndexOptions().unique(true);
        clientTypeCollection.createIndex(index, options);
    }

    public ClientType add(ClientType clientType) {
        InsertOneResult insertOneResult = clientTypeCollection.insertOne(clientType);
        if (insertOneResult.wasAcknowledged()) {
            return clientType;
        } else {
            return null;
        }
    }


    public void remove(ClientType clientType) {
        Bson filter = Filters.eq("_id", clientType.getUuid());
        clientTypeCollection.findOneAndDelete(filter);
    }


    public ClientType getById(UUID id) {
        Bson filter = Filters.eq("_id", id);
        return clientTypeCollection.find(filter).first();
    }

//    @Override
//    public ClientType update(ClientType clientType) {
//        try (EntityManager em = EntityManagerCreator.getEntityManager()) {
//            em.getTransaction().begin();
//            ClientType newClientType = em.find(ClientType.class, clientType.getId());
//            em.getTransaction().commit();
//            return newClientType;
//        } catch (Exception e) {
//            return null;
//        }
//    }


    public List<ClientType> getAll() {

        return clientTypeCollection.find().into(new ArrayList<>());
    }

    public ClientType getByType(Class type){
        Bson filter = Filters.eq("_clazz", type.getName());
        ClientType clientType = clientTypeCollection.find(filter).first();
        if(clientType == null){
            try {
                return add((ClientType) type.getConstructor().newInstance());
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        return clientType;
    }
}
