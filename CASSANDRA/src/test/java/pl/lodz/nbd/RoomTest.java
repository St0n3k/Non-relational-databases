package pl.lodz.nbd;

import org.junit.jupiter.api.Test;
import pl.lodz.nbd.common.RepositoryCreator;
import pl.lodz.nbd.model.Room;
import pl.lodz.nbd.repository.RoomRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class RoomTest {

    private final RoomRepository roomRepository = RepositoryCreator.getRoomRepository();


    @Test
    void getAllRoomsTest() {
        Room room = Room.builder().roomNumber(1).size(2).price(3).build();
        roomRepository.addRoom(room);

        List<Room> rooms = roomRepository.getAll();
        assertNotNull(rooms);
        assertTrue(rooms.size() > 0);
    }

    @Test
    void addRoomTest() {
        Room room = Room.builder().roomNumber(2).size(3).price(4).build();
        assertTrue(roomRepository.addRoom(room)); //should add room

        Optional<Room> optionalRoom = roomRepository.getByRoomNumber(2);
        assertTrue(optionalRoom.isPresent());

        assertEquals(room, optionalRoom.get());

        room = Room.builder().roomNumber(2).size(30).price(40).build();
        assertFalse(roomRepository.addRoom(room)); //should not add room with same room number
    }


    @Test
    void updateRoomTest() {
        Room room = Room.builder().roomNumber(3).size(4).price(5).build();
        roomRepository.addRoom(room);

        Optional<Room> optionalRoom = roomRepository.getByRoomNumber(3);
        assertTrue(optionalRoom.isPresent());

        room = optionalRoom.get();
        assertEquals(5, room.getPrice());
        room.setPrice(1000);
        roomRepository.update(room);

        optionalRoom = roomRepository.getByRoomNumber(3);
        assertTrue(optionalRoom.isPresent());

        room = optionalRoom.get();
        assertEquals(1000, room.getPrice());
    }

    @Test
    void removeRoomTest() {
        Room room = Room.builder().roomNumber(4).size(1).price(2).build();
        roomRepository.addRoom(room);

        Optional<Room> optionalRoom = roomRepository.getByRoomNumber(4);
        assertTrue(optionalRoom.isPresent());

        roomRepository.remove(room);

        optionalRoom = roomRepository.getByRoomNumber(4);
        assertTrue(optionalRoom.isEmpty());
    }
}
