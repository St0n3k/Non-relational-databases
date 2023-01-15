package pl.lodz.nbd.manager;

import lombok.AllArgsConstructor;
import pl.lodz.nbd.model.Room;
import pl.lodz.nbd.repository.impl.RoomRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
public class RoomManager {

    private RoomRepository roomRepository;

    public Optional<Room> addRoom(double price, int size, int number) {
        Room room = new Room(number, price, size);
        return roomRepository.add(room);
    }

    public List<Room> getAllRooms() {
        return roomRepository.getAll();
    }

    public boolean updateRoom(Room room) {
        return roomRepository.update(room);
    }

    public Optional<Room> getByRoomNumber(int number) {
        return roomRepository.getByRoomNumber(number);
    }

    public void removeRoom(Room room) {
        roomRepository.remove(room);
    }

    public Optional<Room> getById(UUID id) {
        return roomRepository.getById(id);
    }
}
