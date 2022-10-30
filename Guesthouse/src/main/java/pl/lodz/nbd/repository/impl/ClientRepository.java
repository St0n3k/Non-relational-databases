package pl.lodz.nbd.repository.impl;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import org.bson.conversions.Bson;
import pl.lodz.nbd.model.Client;
import pl.lodz.nbd.repository.AbstractMongoRepository;
import pl.lodz.nbd.repository.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ClientRepository extends AbstractMongoRepository implements Repository<Client> {

    MongoCollection<Client> clientCollection = mongoDatabase.getCollection("clients", Client.class);

    public Client add(Client client) {
        MongoCollection<Client> clientCollection = mongoDatabase.getCollection("clients", Client.class);
        clientCollection.insertOne(client);
        return client;
    }

    public List<Client> getAll() {
        return clientCollection.find().into(new ArrayList<>());
    }


    public void remove(Client client) {
        Bson filter = Filters.eq("_id", client.getUuid());
        clientCollection.findOneAndDelete(filter);
    }


    public Client getById(UUID id) {
        Bson filter = Filters.eq("_id", id);
        return mongoDatabase.getCollection("clients", Client.class).find(filter).first();
    }


    public Client getClientByPersonalId(String personalId) {
        Bson filter = Filters.eq("personal_id", personalId);
        return mongoDatabase.getCollection("clients", Client.class).find(filter).first();
    }


//
//    @Override
//    public Client update(Client client) {
//        try (EntityManager em = EntityManagerCreator.getEntityManager()) {
//            em.getTransaction().begin();
//            Client newClient = em.merge(client);
//            em.getTransaction().commit();
//            return newClient;
//        } catch (Exception e) {
//            return null;
//        }
//    }
}
