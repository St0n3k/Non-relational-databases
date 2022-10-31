package pl.lodz.nbd.repository.impl;


import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.conversions.Bson;
import pl.lodz.nbd.model.Room;
import pl.lodz.nbd.repository.AbstractMongoRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class RoomRepository extends AbstractMongoRepository {

    MongoCollection<Room> roomCollection = mongoDatabase.getCollection("rooms", Room.class);

    public Optional<Room> add(Room room) {
        if (getByRoomNumber(room.getRoomNumber()).isEmpty()) {
            if (roomCollection.insertOne(room).wasAcknowledged()) {
                return Optional.of(room);
            }
        }
        return Optional.empty();
    }


    public void remove(Room room) {
        Bson filter = Filters.eq("_id", room.getUuid());
        roomCollection.findOneAndDelete(filter);
    }


    public Optional<Room> getById(UUID id) {
        Bson filter = Filters.eq("_id", id);
        return Optional.ofNullable(roomCollection.find(filter).first());
    }


    public boolean update(Room room) {
        Bson filter = Filters.eq("_id", room.getUuid());

        Bson updatePrice = Updates.set("price", room.getPrice());
        Bson updateSize = Updates.set("size", room.getSize());
        Bson update;

        if (getByRoomNumber(room.getRoomNumber()).isEmpty()) {
            Bson updateRoomNumber = Updates.set("number", room.getRoomNumber());
            update = Updates.combine(updatePrice, updateSize, updateRoomNumber);
        } else {
            update = Updates.combine(updatePrice, updateSize);
        }

        return roomCollection.updateOne(filter, update).wasAcknowledged();
    }


    public List<Room> getAll() {
        MongoCollection<Room> roomCollection = mongoDatabase.getCollection("rooms", Room.class);
        return roomCollection.find().into(new ArrayList<>());
    }


    public Optional<Room> getByRoomNumber(int roomNumber) {
        Bson filter = Filters.eq("number", roomNumber);
        return Optional.ofNullable(roomCollection.find(filter).first());
    }
}
