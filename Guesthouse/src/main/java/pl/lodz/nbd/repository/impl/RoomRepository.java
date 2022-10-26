package pl.lodz.nbd.repository.impl;


import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.InsertOneResult;
import pl.lodz.nbd.model.Client;
import pl.lodz.nbd.model.Room;
import pl.lodz.nbd.repository.AbstractMongoRepository;

import java.util.ArrayList;
import java.util.List;

public class RoomRepository extends AbstractMongoRepository {

    public boolean add(Room room) {
        MongoCollection<Room> roomCollection = mongoDatabase.getCollection("rooms", Room.class);
        InsertOneResult result = roomCollection.insertOne(room);
        return result.wasAcknowledged();
    }
//
//    @Override
//    public boolean remove(Room room) {
//        try (EntityManager em = EntityManagerCreator.getEntityManager()) {
//            em.getTransaction().begin();
//            em.remove(em.merge(room));
//            em.getTransaction().commit();
//            return true;
//        } catch (Exception e) {
//            return false;
//        }
//    }
//
//    @Override
//    public Room getById(Long id) {
//        try (EntityManager em = EntityManagerCreator.getEntityManager()) {
//            return em.find(Room.class, id);
//        }
//    }
//
//    @Override
//    public Room update(Room room) {
//        try (EntityManager em = EntityManagerCreator.getEntityManager()) {
//            em.getTransaction().begin();
//            Room newRoom = em.merge(room);
//            em.getTransaction().commit();
//            return newRoom;
//        } catch (Exception e) {
//            return null;
//        }
//    }
//

    public List<Room> getAll() {
        MongoCollection<Room> roomCollection = mongoDatabase.getCollection("rooms", Room.class);
        return roomCollection.find().into(new ArrayList<>());
    }
//
//    public Room getByRoomNumber(int roomNumber) {
//        try (EntityManager em = EntityManagerCreator.getEntityManager()) {
//            List<Room> result = em
//                    .createNamedQuery("Room.getByRoomNumber", Room.class)
//                    .setParameter("roomNumber", roomNumber)
//                    .getResultList();
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
}
