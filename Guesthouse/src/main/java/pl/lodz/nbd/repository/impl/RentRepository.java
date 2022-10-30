package pl.lodz.nbd.repository.impl;

import com.mongodb.client.ClientSession;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import org.bson.conversions.Bson;
import pl.lodz.nbd.model.Rent;
import pl.lodz.nbd.repository.AbstractMongoRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RentRepository extends AbstractMongoRepository {

    MongoCollection<Rent> rentCollection = mongoDatabase.getCollection("rents", Rent.class);

    public Rent add(Rent rent) {
        try (ClientSession clientSession = mongoClient.startSession()) {
            clientSession.startTransaction();
            //TODO add transaciton logic
            rentCollection.insertOne(rent);
            clientSession.commitTransaction();
            return rent;
        }


//        try (EntityManager em = EntityManagerCreator.getEntityManager()) {
//            em.getTransaction().begin();
//
//            Room room = em.find(Room.class, rent.getRoom().getId());
//
//            em.lock(room, LockModeType.OPTIMISTIC_FORCE_INCREMENT);
//
//            boolean isColliding = isColliding(
//                    rent.getBeginTime(),
//                    rent.getEndTime(),
//                    room.getRoomNumber());
//
//            if (isColliding) return null;
//
//            em.persist(rent);
//
//            em.getTransaction().commit();
//            return rent;
//        } catch (RollbackException e) {
//            System.out.println("Repeating transaction");
//            //We have to set id to null, because in transaction,
//            //rent gets id on persist, but on failed transaction it not gets back to null
//            rent.setId(null);
//            return add(rent);
//        } catch (Exception e) {
//            return null;
//        }
    }

    public void remove(Rent rent) {
        Bson filter = Filters.eq("_id", rent.getUuid());
        rentCollection.findOneAndDelete(filter);
    }


    public Rent getById(UUID id) {
        Bson filter = Filters.eq("_id", id);
        return rentCollection.find(filter).first();
    }

    public List<Rent> getAll() {
        return rentCollection.find().into(new ArrayList<>());
    }

    public List<Rent> getByRoomNumber(int roomNumber) {
        Bson filter = Filters.eq("room.number", roomNumber);
        return rentCollection.find(filter).into(new ArrayList<>());
    }

    public List<Rent> getByClientPersonalId(String personalId) {
        Bson filter = Filters.eq("client.personal_id", personalId);
        return rentCollection.find(filter).into(new ArrayList<>());
    }
//
//    public Rent update(Rent rent) {
//        try (EntityManager em = EntityManagerCreator.getEntityManager()) {
//            em.getTransaction().begin();
//            Rent newRent = em.merge(rent);
//            em.getTransaction().commit();
//            return newRent;
//        } catch (Exception e) {
//            return null;
//        }
//    }
//
//    public boolean isColliding(LocalDateTime beginDate, LocalDateTime endDate, int roomNumber) {
//        try (EntityManager em = EntityManagerCreator.getEntityManager()) {
//            List<Rent> rentsColliding =
//                    em.createNamedQuery("Rent.getRentsColliding", Rent.class)
//                            .setParameter("beginDate", beginDate)
//                            .setParameter("endDate", endDate)
//                            .setParameter("roomNumber", roomNumber)
//                            .getResultList();
//
//            return !rentsColliding.isEmpty();
//        } catch (Exception e) {
//            System.out.println("Unexpected exc");
//            return true;
//        }
//    }

}
