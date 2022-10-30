package pl.lodz.nbd.repository.impl;


import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.conversions.Bson;
import pl.lodz.nbd.model.Room;
import pl.lodz.nbd.repository.AbstractMongoRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RoomRepository extends AbstractMongoRepository {

    MongoCollection<Room> roomCollection = mongoDatabase.getCollection("rooms", Room.class);

    public Room add(Room room) {
        //TODO check if room number is available
        roomCollection.insertOne(room);
        return room;
    }


    public void remove(Room room) {
        Bson filter = Filters.eq("_id", room.getUuid());
        roomCollection.findOneAndDelete(filter);
    }


    public Room getById(UUID id) {
        Bson filter = Filters.eq("_id", id);
        return roomCollection.find(filter).first();
    }


    public Room update(Room room) {
        Bson filter = Filters.eq("_id", room.getUuid());
        Bson updates = Updates.set("number", room.getRoomNumber());
        //TODO implement updating all fields
        Room newRoom = roomCollection.findOneAndUpdate(filter, updates);

        return newRoom;
    }


    public List<Room> getAll() {
        MongoCollection<Room> roomCollection = mongoDatabase.getCollection("rooms", Room.class);
        return roomCollection.find().into(new ArrayList<>());
    }


    public Room getByRoomNumber(int roomNumber) {
        Bson filter = Filters.eq("number", roomNumber);
        return roomCollection.find(filter).first();
    }
}
