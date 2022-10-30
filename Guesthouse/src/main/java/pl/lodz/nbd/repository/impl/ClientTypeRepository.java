package pl.lodz.nbd.repository.impl;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.InsertOneResult;
import org.bson.conversions.Bson;
import pl.lodz.nbd.model.ClientTypes.ClientType;
import pl.lodz.nbd.model.ClientTypes.Gold;
import pl.lodz.nbd.repository.AbstractMongoRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ClientTypeRepository extends AbstractMongoRepository {

    MongoCollection<ClientType> clientTypeCollection = mongoDatabase.getCollection("client_types", ClientType.class);

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

    public Gold getGoldClientType() {
        Bson filter = Filters.eq("_clazz", "Gold");
        MongoCollection<Gold> goldCollection = mongoDatabase.getCollection("client_types", Gold.class);
        return goldCollection.find(filter).first();
    }

    public ClientType getByType(Class type) {
        Bson filter = Filters.eq("_clazz", type.getSimpleName());
        return clientTypeCollection.find(filter).first();
    }
}
