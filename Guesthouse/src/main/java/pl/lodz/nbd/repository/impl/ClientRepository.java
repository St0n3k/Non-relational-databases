package pl.lodz.nbd.repository.impl;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import pl.lodz.nbd.model.Client;
import pl.lodz.nbd.model.Room;
import pl.lodz.nbd.repository.Repository;
import pl.lodz.nbd.repository.AbstractMongoRepository;

import java.util.ArrayList;
import java.util.List;

public class ClientRepository extends AbstractMongoRepository implements Repository<Client> {


    public Client add(Client client) {
        MongoCollection<Client> clientCollection = mongoDatabase.getCollection("clients", Client.class);
        clientCollection.insertOne(client);
        return client;
    }

    public List<Client> getAll() {
        MongoCollection<Client> clientCollection = mongoDatabase.getCollection("clients", Client.class);
        return clientCollection.find().into(new ArrayList<Client>());
    }
    //    @Override
//    public Client add(Client client) {
//        try (EntityManager em = EntityManagerCreator.getEntityManager()) {
//            em.getTransaction().begin();
//            em.persist(client);
//            em.getTransaction().commit();
//            return client;
//        } catch (Exception e) {
//            return null;
//        }
//    }
//
//    @Override
//    public boolean remove(Client client) {
//        try (EntityManager em = EntityManagerCreator.getEntityManager()) {
//            em.getTransaction().begin();
//            em.remove(em.merge(client));
//            em.getTransaction().commit();
//            return true;
//        } catch (Exception e) {
//            return false;
//        }
//    }
//
//    @Override
//    public Client getById(Long id) {
//        try (EntityManager em = EntityManagerCreator.getEntityManager()) {
//            return em.find(Client.class, id);
//        }
//    }
//
//    @Override
//    public List<Client> getAll() {
//        try (EntityManager em = EntityManagerCreator.getEntityManager()) {
//            return em.createNamedQuery("Client.getAll", Client.class).getResultList();
//        }
//    }
//
//    public Client getClientByPersonalId(String personalId) {
//        try (EntityManager em = EntityManagerCreator.getEntityManager()) {
//            List<Client> result = em.createNamedQuery("Client.getByPersonalId", Client.class).setParameter("personalId", personalId).getResultList();
//
//            if (result.isEmpty()) {
//                return null;
//            } else {
//                return result.get(0);
//            }
//        } catch (Exception e) {
//            return null;
//        }
//
//    }
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
