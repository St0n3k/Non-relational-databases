package pl.lodz.nbd.repository.impl;


import com.mongodb.MongoCommandException;
import com.mongodb.MongoWriteException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.*;
import org.bson.Document;
import org.bson.conversions.Bson;
import pl.lodz.nbd.model.Room;
import pl.lodz.nbd.repository.AbstractMongoRepository;
import pl.lodz.nbd.repository.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class RoomRepository extends AbstractMongoRepository implements Repository<Room> {

    MongoCollection<Room> roomCollection = mongoDatabase.getCollection("rooms", Room.class);

    public RoomRepository() {
        roomCollection.drop();
        ValidationOptions validationOptions = new ValidationOptions().validator(
                Document.parse("""
                        {
                            $jsonSchema: {
                                "properties" : {
                                    "is_being_rented" : {
                                        "bsonType": "int",
                                        "minimum": 0,
                                        "maximum": 1
                                    }
                                }
                            }
                        }                 
                        """));

        Document index = new Document("number", 1);
        IndexOptions options = new IndexOptions().unique(true);
        CreateCollectionOptions createCollectionOptions = new CreateCollectionOptions().validationOptions(validationOptions);

        try {
            mongoDatabase.createCollection("rooms", createCollectionOptions);
        } catch (MongoCommandException ignored) {
        }
        roomCollection = mongoDatabase.getCollection("rooms", Room.class);
        roomCollection.createIndex(index, options);
    }

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
        Bson updateIsBeingRented = Updates.set("is_being_rented", room.getIsBeingRented());
        Bson update;

        if (getByRoomNumber(room.getRoomNumber()).isEmpty()) {
            Bson updateRoomNumber = Updates.set("number", room.getRoomNumber());
            update = Updates.combine(updatePrice, updateSize, updateRoomNumber, updateIsBeingRented);
        } else {
            update = Updates.combine(updatePrice, updateSize, updateIsBeingRented);
        }

        try {
            return roomCollection.updateOne(filter, update).wasAcknowledged();
        } catch (MongoWriteException e) {
            return false;
        }
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
