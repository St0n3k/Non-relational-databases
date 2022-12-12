package pl.lodz.nbd.dao;

import com.datastax.oss.driver.api.mapper.annotations.*;
import pl.lodz.nbd.model.Room;
import pl.lodz.nbd.providers.RoomQueryProvider;

import java.util.List;

@Dao
public interface RoomDao {

    @StatementAttributes(consistencyLevel = "QUORUM")
    @Insert
    void create(Room room);

    @StatementAttributes(consistencyLevel = "QUORUM")
    @Delete
    void remove(Room room);

    @StatementAttributes(consistencyLevel = "QUORUM")
    @Update
    void update(Room room);

    @StatementAttributes(consistencyLevel = "QUORUM")
    @QueryProvider(providerClass = RoomQueryProvider.class)
    Room findByRoomNumber(int number);

    @StatementAttributes(consistencyLevel = "QUORUM")
    @QueryProvider(providerClass = RoomQueryProvider.class)
    List<Room> findAll();
}
