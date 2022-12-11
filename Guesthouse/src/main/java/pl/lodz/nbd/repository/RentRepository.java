package pl.lodz.nbd.repository;

public class RentRepository {

//    MongoCollection<Rent> rentCollection = mongoDatabase.getCollection("rents", Rent.class);
//    MongoCollection<Room> roomCollection = mongoDatabase.getCollection("rooms", Room.class);
//
//    public RentRepository() {
//        rentCollection.drop();
//        rentCollection = mongoDatabase.getCollection("rents", Rent.class);
//    }
//
//    public Optional<Rent> add(Rent rent) {
//
//        int roomNumber = rent.getRoom().getRoomNumber();
//
//        if (!updateIsBeingRented(roomNumber, true)) {
//            return add(rent);
//        }
//        boolean colliding = isColliding(
//                rent.getBeginTime(),
//                rent.getEndTime(),
//                roomNumber);
//
//        if (colliding) {
//            updateIsBeingRented(roomNumber, false);
//            return Optional.empty();
//        }
//        rentCollection.insertOne(rent);
//        updateIsBeingRented(roomNumber, false);
//
//        return Optional.of(rent);
//    }
//
//    public void remove(Rent rent) {
//        Bson filter = Filters.eq("_id", rent.getUuid());
//        rentCollection.findOneAndDelete(filter);
//    }
//
//
//    public Optional<Rent> getById(UUID id) {
//        Bson filter = Filters.eq("_id", id);
//        return Optional.ofNullable(rentCollection.find(filter).first());
//    }
//
//    public List<Rent> getAll() {
//        return rentCollection.find().into(new ArrayList<>());
//    }
//
//    public List<Rent> getByRoomNumber(int roomNumber) {
//        Bson filter = Filters.eq("room.number", roomNumber);
//        return rentCollection.find(filter).into(new ArrayList<>());
//    }
//
//    public List<Rent> getByClientPersonalId(String personalId) {
//        Bson filter = Filters.eq("client.personal_id", personalId);
//        return rentCollection.find(filter).into(new ArrayList<>());
//    }
//
//
//    public boolean update(Rent rent) {
//
//        Bson filter = Filters.eq("_id", rent.getUuid());
//
//        Bson updateBoard = Updates.set("board", rent.isBoard());
//        Bson updateCost = Updates.set("final_cost", rent.getFinalCost());
//        Bson update = Updates.combine(updateBoard, updateCost);
//
//
//        return rentCollection.updateOne(filter, update).wasAcknowledged();
//    }
//
//    public boolean isColliding(LocalDateTime beginDate, LocalDateTime endDate, int roomNumber) {
//
//        Bson filterRoomNumber = Filters.eq("room.number", roomNumber);
//
//        Bson filterBeginDateBetweenExistingLeft = Filters.gte("begin_time", beginDate);
//        Bson filterBeginDateBetweenExistingRight = Filters.lte("begin_time", endDate);
//
//        Bson filterEndDateBetweenExistingRight = Filters.gte("end_time", beginDate);
//        Bson filterEndDateBetweenExistingLeft = Filters.lte("end_time", endDate);
//
//        Bson filterDateContainingExistingLeft = Filters.lte("begin_time", beginDate);
//        Bson filterDateContainingExistingRight = Filters.gte("end_time", endDate);
//
//        Bson dateFilter1 = Filters.and(filterBeginDateBetweenExistingLeft, filterBeginDateBetweenExistingRight);
//        Bson dateFilter2 = Filters.and(filterEndDateBetweenExistingLeft, filterEndDateBetweenExistingRight);
//        Bson dateFilter3 = Filters.and(filterDateContainingExistingLeft, filterDateContainingExistingRight);
//
//        Bson dateFilters = Filters.or(dateFilter1, dateFilter2, dateFilter3);
//
//        Bson finalFilter = Filters.and(filterRoomNumber, dateFilters);
//
//        List<Rent> rentsColliding = rentCollection.find(finalFilter).into(new ArrayList<>());
//
//        return !rentsColliding.isEmpty();
//    }
//
//    public boolean updateIsBeingRented(int roomNumber, boolean increment) {
//        Bson filter = Filters.eq("number", roomNumber);
//        Bson updateIsBeingRented;
//        if (increment) {
//            updateIsBeingRented = Updates.inc("is_being_rented", 1);
//        } else {
//            updateIsBeingRented = Updates.set("is_being_rented", 0);
//        }
//        try {
//            return roomCollection.updateOne(filter, updateIsBeingRented).wasAcknowledged();
//        } catch (MongoWriteException e) {
//            return false;
//        }
//    }

}
