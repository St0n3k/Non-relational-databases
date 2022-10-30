package pl.lodz.nbd.manager;

import lombok.AllArgsConstructor;
import pl.lodz.nbd.model.Room;
import pl.lodz.nbd.repository.impl.RoomRepository;

import java.util.List;

@AllArgsConstructor
public class RoomManager {

    private RoomRepository roomRepository;

    public Room addRoom(double price, int size, int number) {
        Room room = new Room(number, price, size);
        roomRepository.add(room);
        return room;
    }

    public List<Room> getAllRooms() {
        return roomRepository.getAll();
    }

    public Room updateRoom(Room room) {
        return roomRepository.update(room);
    }

    public Room getByRoomNumber(int number) {
        return roomRepository.getByRoomNumber(number);
    }

    public void removeRoom(Room room) {
        roomRepository.remove(room);
    }
}
