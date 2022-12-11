package pl.lodz.nbd.dao;

import com.datastax.oss.driver.api.mapper.annotations.Dao;
import com.datastax.oss.driver.api.mapper.annotations.Insert;
import pl.lodz.nbd.model.Room;

@Dao
public interface RoomDao {

    @Insert
    void create(Room room);


}
