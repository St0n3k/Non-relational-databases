package pl.lodz.nbd.repository.impl;

import com.mongodb.client.ClientSession;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.conversions.Bson;
import pl.lodz.nbd.model.Rent;
import pl.lodz.nbd.model.Room;
import pl.lodz.nbd.repository.AbstractMongoRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class RentRepository extends AbstractMongoRepository {

    MongoCollection<Rent> rentCollection = mongoDatabase.getCollection("rents", Rent.class);

    public Optional<Rent> add(Rent rent) {
        try (ClientSession clientSession = mongoClient.startSession()) {
            clientSession.startTransaction();

            Room room = rent.getRoom();

            boolean isColliding = isColliding(
                    rent.getBeginTime(),
                    rent.getEndTime(),
                    room.getRoomNumber());

            if (isColliding) {
                clientSession.abortTransaction();
                return Optional.empty();
            }

            rentCollection.insertOne(rent);
            clientSession.commitTransaction();
            return Optional.of(rent);
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


    public Rent update(Rent rent) {

        Bson filter = Filters.eq("_id", rent.getUuid());

        Bson updateBoard = Updates.set("board", rent.isBoard());
        Bson updateCost = Updates.set("final_cost", rent.getFinalCost());
        Bson update = Updates.combine(updateBoard, updateCost);


        return rentCollection.findOneAndUpdate(filter, update);
    }

    public boolean isColliding(LocalDateTime beginDate, LocalDateTime endDate, int roomNumber) {

        Bson filterRoomNumber = Filters.eq("room.number", roomNumber);

        Bson filterBeginDateBetweenExistingLeft = Filters.gte("begin_time", beginDate);
        Bson filterBeginDateBetweenExistingRight = Filters.lte("begin_time", endDate);

        Bson filterEndDateBetweenExistingRight = Filters.gte("end_time", beginDate);
        Bson filterEndDateBetweenExistingLeft = Filters.lte("end_time", endDate);

        Bson filterDateContainingExistingLeft = Filters.lte("begin_time", beginDate);
        Bson filterDateContainingExistingRight = Filters.gte("end_time", endDate);

        Bson dateFilter1 = Filters.and(filterBeginDateBetweenExistingLeft, filterBeginDateBetweenExistingRight);
        Bson dateFilter2 = Filters.and(filterEndDateBetweenExistingLeft, filterEndDateBetweenExistingRight);
        Bson dateFilter3 = Filters.and(filterDateContainingExistingLeft, filterDateContainingExistingRight);

        Bson dateFilters = Filters.or(dateFilter1, dateFilter2, dateFilter3);

        Bson finalFilter = Filters.and(filterRoomNumber, dateFilters);

        List<Rent> rentsColliding = rentCollection.find(finalFilter).into(new ArrayList<>());

        return !rentsColliding.isEmpty();
    }
}
