package pl.lodz.nbd;

import org.junit.jupiter.api.Test;
import pl.lodz.nbd.manager.RoomManager;
import pl.lodz.nbd.model.Room;
import pl.lodz.nbd.repository.impl.RoomRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class RoomTest {

    private static final RoomRepository roomRepository = new RoomRepository();

    private static final RoomManager roomManager = new RoomManager(roomRepository);


    @Test
    void roomMapperTest() {
        Room room1 = new Room(9, 9, 9);
        roomRepository.add(room1);
        Optional<Room> optionalRoom = roomRepository.getById(room1.getUuid());
        assertTrue(optionalRoom.isPresent());
        Room room2 = optionalRoom.get();
        assertEquals(room1, room2);
    }

    @Test
    void getAllRoomsTest() {
        roomManager.addRoom(1500.0, 5, 987);

        List<Room> rooms = roomManager.getAllRooms();
        assertNotNull(rooms);
        assertTrue(rooms.size() > 0);
    }

    @Test
    void addRoomTest() {
        roomManager.addRoom(100.0, 2, 100);
        Optional<Room> optionalRoom = (roomManager.addRoom(200.0, 3, 101));
        assertTrue(optionalRoom.isPresent());

        assertTrue(roomManager.addRoom(1000.0, 5, 100).isEmpty());

        //Check if getRoomByNumber works properly
        assertTrue(roomManager.getByRoomNumber(100).isPresent());
        assertTrue(roomManager.getByRoomNumber(101).isPresent());
        assertTrue(roomManager.getByRoomNumber(300).isEmpty());
    }

    @Test
    void updateRoomTest() {
        roomManager.addRoom(300.0, 3, 1040);
        Optional<Room> optionalRoom = roomManager.getByRoomNumber(1040);
        assertTrue(optionalRoom.isPresent());
        Room room = optionalRoom.get();
        assertEquals(3, room.getSize());
        room.setSize(10);
        roomManager.updateRoom(room);
        optionalRoom = roomManager.getByRoomNumber(1040);
        assertTrue(optionalRoom.isPresent());
        Room newRoom = optionalRoom.get();
        assertEquals(10, newRoom.getSize());

        roomManager.addRoom(600.0, 14, 900);
        optionalRoom = roomManager.getByRoomNumber(900);
        assertTrue(optionalRoom.isPresent());
        Room roomTwo = optionalRoom.get();
        roomTwo.setRoomNumber(1040);
        roomTwo.setSize(3);

        assertTrue(roomManager.updateRoom(roomTwo));
        optionalRoom = roomManager.getById(roomTwo.getUuid());
        assertTrue(optionalRoom.isPresent());
        Room notChangedRoomNumber = optionalRoom.get();
        assertEquals(notChangedRoomNumber.getRoomNumber(), 900); //room number didn't change due to conflict with other room
        assertEquals(notChangedRoomNumber.getSize(), 3); //size changed
        assertEquals(notChangedRoomNumber.getUuid(), roomTwo.getUuid()); //proof that it is still the same room
        assertNotNull(roomManager.getByRoomNumber(900)); //room number is still 900

        roomTwo.setRoomNumber(1041);
        roomTwo.setPrice(999.0);
        assertNotNull(roomManager.updateRoom(roomTwo));
        optionalRoom = roomManager.getById(roomTwo.getUuid());
        assertTrue(optionalRoom.isPresent());

        Room updatedRoom = optionalRoom.get();
        assertEquals(updatedRoom.getRoomNumber(), 1041); //room number changed to 1041
        assertEquals(updatedRoom.getSize(), 3);
        assertEquals(updatedRoom.getPrice(), 999.0);
        assertEquals(updatedRoom.getUuid(), roomTwo.getUuid()); //proof that it is still the same room
    }

    @Test
    void removeRoomTest() {
        roomManager.addRoom(500, 500, 500);
        Optional<Room> optionalRoom = roomManager.getByRoomNumber(500);
        assertTrue(optionalRoom.isPresent());
        Room room = optionalRoom.get();
        roomManager.removeRoom(room);
        assertTrue(roomManager.getByRoomNumber(500).isEmpty());
    }

}
