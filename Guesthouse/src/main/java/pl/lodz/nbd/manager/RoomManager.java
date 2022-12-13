package pl.lodz.nbd.manager;

import lombok.AllArgsConstructor;
import pl.lodz.nbd.model.Room;
import pl.lodz.nbd.repository.RoomRepository;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public class RoomManager {

    private RoomRepository roomRepository;

    public boolean addRoom(double price, int size, int number) {
        Room room = new Room(number, price, size);
        return roomRepository.addRoom(room);
    }

    public List<Room> getAllRooms() {
        return roomRepository.getAll();
    }

    public void updateRoom(Room room) {
        roomRepository.update(room);
    }

    public Optional<Room> getByRoomNumber(int number) {
        return roomRepository.getByRoomNumber(number);
    }

    public void removeRoom(Room room) {
        roomRepository.remove(room);
    }
}
