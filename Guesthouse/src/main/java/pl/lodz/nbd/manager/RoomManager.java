package pl.lodz.nbd.manager;

import jakarta.persistence.EntityManager;
import jakarta.persistence.RollbackException;
import lombok.AllArgsConstructor;
import pl.lodz.nbd.model.Room;
import pl.lodz.nbd.repository.EntityManagerCreator;
import pl.lodz.nbd.repository.RoomRepository;

@AllArgsConstructor
public class RoomManager {

    private RoomRepository roomRepository;

    public Room addRoom(double price, int size, int number) {
        Room room = new Room(number, price, size );

        EntityManager em = EntityManagerCreator.getEntityManager();

        try {
            em.getTransaction().begin();
            roomRepository.add(room, em);
            em.getTransaction().commit();
        } catch(RollbackException e) {
            em.getTransaction().rollback();
            em.close();
            return null;
        }

        return room;
    }

    public Room getByRoomNumber(int number) {
        EntityManager em = EntityManagerCreator.getEntityManager();
        return roomRepository.getByRoomNumber(number, em);
    }

}
