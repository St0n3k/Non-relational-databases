package pl.lodz.nbd;

import org.junit.jupiter.api.Test;
import pl.lodz.nbd.manager.RoomManager;
import pl.lodz.nbd.repository.impl.RoomRepository;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class RoomTest {

    private static final RoomRepository roomRepository = new RoomRepository();


    @Test
    void addRoomTest() {
        RoomManager roomManager = new RoomManager(roomRepository);

        //Check if rooms are persisted
        assertNotNull(roomManager.addRoom(100.0, 2, 100));
        assertNotNull(roomManager.addRoom(200.0, 3, 101));

        //Check if room is not persisted(existing room number)
        assertNull(roomManager.addRoom(1000.0, 5, 100));

        //Check if getRoomByNumber works properly
        assertNotNull(roomManager.getByRoomNumber(100));
        assertNotNull(roomManager.getByRoomNumber(101));
        assertNull(roomManager.getByRoomNumber(300));
    }

}
