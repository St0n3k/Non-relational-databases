package pl.lodz.nbd.repository;

import jakarta.persistence.EntityManager;
import pl.lodz.nbd.model.Address;
import pl.lodz.nbd.model.Room;

import java.util.List;

public class RoomRepository implements Repository<Room> {
    @Override
    public void add(Room room, EntityManager em) {
        em.persist(room);
    }

    @Override
    public void remove(Room room, EntityManager em) {
        em.remove(room);
    }

    @Override
    public Room getById(Long id, EntityManager em) {
        return em.find(Room.class, id);
    }

    @Override
    public List<Room> getAll(EntityManager em) {
        return em.createNamedQuery("Room.getAll", Room.class).getResultList();
    }

    public Room getByRoomNumber(int roomNumber, EntityManager em) {
        List<Room> result = em.createNamedQuery("Room.getByRoomNumber", Room.class).setParameter("roomNumber", roomNumber).getResultList();

        if (result.isEmpty()) {
            return null;
        } else {
            return result.get(0);
        }
    }
}
