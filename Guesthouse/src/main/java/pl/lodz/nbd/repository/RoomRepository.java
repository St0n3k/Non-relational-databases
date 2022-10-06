package pl.lodz.nbd.repository;

import jakarta.persistence.EntityManager;
import pl.lodz.nbd.model.Room;

public class RoomRepository implements Repository<Room> {
    @Override
    public void add(Room room, EntityManager em) {
        em.persist(room);
    }
}
