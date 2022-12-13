package pl.lodz.nbd.repository;


import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.datastax.oss.driver.api.core.type.DataTypes;
import pl.lodz.nbd.common.GuesthouseFinals;
import pl.lodz.nbd.dao.RoomDao;
import pl.lodz.nbd.mapper.RoomMapper;
import pl.lodz.nbd.mapper.RoomMapperBuilder;
import pl.lodz.nbd.model.Room;

import java.util.List;
import java.util.Optional;

import static com.datastax.oss.driver.api.querybuilder.SchemaBuilder.createTable;
import static com.datastax.oss.driver.api.querybuilder.SchemaBuilder.dropTable;

public class RoomRepository {

    private final RoomDao roomDao;

    public RoomRepository(CqlSession session) {
        session.execute(dropTable(GuesthouseFinals.ROOMS).ifExists().build());
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
        return roomDao.create(room);
    }


    public void remove(Room room) {
        roomDao.remove(room);
    }

    public Optional<Room> getByRoomNumber(int roomNumber) {
        return Optional.ofNullable(roomDao.findByRoomNumber(roomNumber));
    }

    public void update(Room room) {
        roomDao.update(room);
    }


    public List<Room> getAll() {
        return roomDao.findAll();
    }
}
