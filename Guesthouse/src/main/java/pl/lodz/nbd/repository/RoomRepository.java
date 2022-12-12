package pl.lodz.nbd.repository;


import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.datastax.oss.driver.api.core.type.DataTypes;
import pl.lodz.nbd.common.GuesthouseFinals;
import pl.lodz.nbd.dao.RoomDao;
import pl.lodz.nbd.mapper.RoomMapper;
import pl.lodz.nbd.mapper.RoomMapperBuilder;
import pl.lodz.nbd.model.Room;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.Optional;

import static com.datastax.oss.driver.api.querybuilder.SchemaBuilder.createTable;
import static com.datastax.oss.driver.api.querybuilder.SchemaBuilder.dropTable;

public class RoomRepository {

    private static CqlSession session;

    private final RoomDao roomDao;


    public RoomRepository() {
        session = CqlSession.builder()
                .addContactPoint(new InetSocketAddress("localhost", 9042))
                .addContactPoint(new InetSocketAddress("localhost", 9043))
                .withLocalDatacenter("dc1")
                .withKeyspace(GuesthouseFinals.GUESTHOUSE_NAMESPACE)
                .withAuthCredentials("cassandra", "cassandra")
                .build();

        session.execute(dropTable(GuesthouseFinals.ROOMS).build());
        session.execute(createRoomTableStatement());

        RoomMapper roomMapper = new RoomMapperBuilder(session).build();
        roomDao = roomMapper.roomDao();
    }

    private SimpleStatement createRoomTableStatement() {
        return createTable(GuesthouseFinals.ROOMS)
                .ifNotExists()
                .withPartitionKey(GuesthouseFinals.ROOM_NUMBER, DataTypes.INT)
                .withColumn(GuesthouseFinals.ROOM_SIZE, DataTypes.INT)
                .withColumn(GuesthouseFinals.ROOM_PRICE, DataTypes.DOUBLE)
                .build();
    }

    public boolean addRoom(Room room) {
        if (getByRoomNumber(room.getRoomNumber()).isPresent()) {
            return false;
        }
        roomDao.create(room);
        return true;
    }


    public void remove(Room room) {
        roomDao.remove(room);
    }

    public Optional<Room> getByRoomNumber(int roomNumber) {
        try {
            return Optional.ofNullable(roomDao.findByRoomNumber(roomNumber));
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }

    public void update(Room room) {
        roomDao.update(room);
    }


    public List<Room> getAll() {
        return roomDao.findAll();
    }
}
