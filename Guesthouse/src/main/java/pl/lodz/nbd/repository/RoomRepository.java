package pl.lodz.nbd.repository;


import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.datastax.oss.driver.api.core.type.DataTypes;
import com.datastax.oss.driver.api.querybuilder.schema.CreateKeyspace;
import pl.lodz.nbd.dao.RoomDao;
import pl.lodz.nbd.mapper.RoomMapper;
import pl.lodz.nbd.mapper.RoomMapperBuilder;
import pl.lodz.nbd.model.Room;

import java.net.InetSocketAddress;

import static com.datastax.oss.driver.api.querybuilder.SchemaBuilder.createKeyspace;
import static com.datastax.oss.driver.api.querybuilder.SchemaBuilder.createTable;

public class RoomRepository {

    private static CqlSession session;

    private final RoomDao roomDao;


    public RoomRepository() {
        session = CqlSession.builder()
                .addContactPoint(new InetSocketAddress("localhost", 9042))
                .addContactPoint(new InetSocketAddress("localhost", 9043))
                .withLocalDatacenter("dc1")
                .withKeyspace(CqlIdentifier.fromCql("guesthouse"))
                .withAuthCredentials("cassandra", "cassandra")
                .build();

        session.execute(createKeyspaceStatement());
        session.execute(createRoomTableStatement());

        RoomMapper roomMapper = new RoomMapperBuilder(session).build();
        roomDao = roomMapper.roomDao();
    }

    private SimpleStatement createKeyspaceStatement() {
        CreateKeyspace keyspace = createKeyspace(CqlIdentifier.fromCql("guesthouse"))
                .ifNotExists()
                .withSimpleStrategy(2)
                .withDurableWrites(true);
        return keyspace.build();
    }

    private SimpleStatement createRoomTableStatement() {
        return createTable(CqlIdentifier.fromCql("rooms"))
                .ifNotExists()
                .withPartitionKey(CqlIdentifier.fromCql("number"), DataTypes.INT)
                .withColumn(CqlIdentifier.fromCql("size"), DataTypes.INT)
                .withColumn(CqlIdentifier.fromCql("price"), DataTypes.DOUBLE)
                .build();
    }

    public void addRoom(Room room) {
        roomDao.create(room);
    }


//
//
//    public void remove(Room room) {
//        Bson filter = Filters.eq("_id", room.getUuid());
//        roomCollection.findOneAndDelete(filter);
//    }
//
//
//    public Optional<Room> getById(UUID id) {
//        Bson filter = Filters.eq("_id", id);
//        return Optional.ofNullable(roomCollection.find(filter).first());
//    }
//
//
//    public boolean update(Room room) {
//        Bson filter = Filters.eq("_id", room.getUuid());
//
//        Bson updatePrice = Updates.set("price", room.getPrice());
//        Bson updateSize = Updates.set("size", room.getSize());
//        Bson updateIsBeingRented = Updates.set("is_being_rented", room.getIsBeingRented());
//        Bson update;
//
//        if (getByRoomNumber(room.getRoomNumber()).isEmpty()) {
//            Bson updateRoomNumber = Updates.set("number", room.getRoomNumber());
//            update = Updates.combine(updatePrice, updateSize, updateRoomNumber, updateIsBeingRented);
//        } else {
//            update = Updates.combine(updatePrice, updateSize, updateIsBeingRented);
//        }
//
//        try {
//            return roomCollection.updateOne(filter, update).wasAcknowledged();
//        } catch (MongoWriteException e) {
//            return false;
//        }
//    }
//
//
//    public List<Room> getAll() {
//        MongoCollection<Room> roomCollection = mongoDatabase.getCollection("rooms", Room.class);
//        return roomCollection.find().into(new ArrayList<>());
//    }
//
//
//    public Optional<Room> getByRoomNumber(int roomNumber) {
//        Bson filter = Filters.eq("number", roomNumber);
//        return Optional.ofNullable(roomCollection.find(filter).first());
//    }
}
