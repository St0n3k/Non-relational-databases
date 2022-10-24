package pl.lodz.nbd.repository.impl;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import pl.lodz.nbd.model.Room;
import pl.lodz.nbd.repository.AbstractMongoRepository;

public class RoomRepository extends AbstractMongoRepository {

    private final MongoClient mongoClient = MongoClients.create(clientSettings);
    private final MongoDatabase mongoDatabase = mongoClient.getDatabase("nbd");


    public Room add(Room room) {
        Document document = new Document("id", room.getUuid())
                .append("price", room.getPrice())
                .append("size", room.getSize())
                .append("number", room.getRoomNumber());
        System.out.println(mongoDatabase.getCollection("rooms").insertOne(document));
        return room;
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
//    @Override
//    public List<Room> getAll() {
//        try (EntityManager em = EntityManagerCreator.getEntityManager()) {
//            return em.createNamedQuery("Room.getAll", Room.class).getResultList();
//        }
//    }
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
