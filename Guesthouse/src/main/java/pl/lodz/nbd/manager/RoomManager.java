package pl.lodz.nbd.manager;

import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import pl.lodz.nbd.common.EntityManagerCreator;
import pl.lodz.nbd.model.Room;
import pl.lodz.nbd.repository.impl.RoomRepository;

@AllArgsConstructor
public class RoomManager {

    private RoomRepository roomRepository;

    public Room addRoom(double price, int size, int number) {

        try (EntityManager em = EntityManagerCreator.getEntityManager()) {
            Room room = new Room(number, price, size);

            em.getTransaction().begin();
            roomRepository.add(room, em);
            em.getTransaction().commit();
            return room;

        } catch (Exception e) {
            return null;
        }
    }

    public Room updateRoom(Room room) {
        try (EntityManager em = EntityManagerCreator.getEntityManager()) {
            em.getTransaction().begin();
            Room roomSaved = roomRepository.update(room, em);
            em.getTransaction().commit();
            return roomSaved;
        } catch (Exception e) {
            return null;
        }
    }

    public Room getByRoomNumber(int number) {
        try (EntityManager em = EntityManagerCreator.getEntityManager()) {
            return roomRepository.getByRoomNumber(number, em);
        } catch (Exception e) {
            return null;
        }
    }

    public boolean removeRoom(Room room) {
        try (EntityManager em = EntityManagerCreator.getEntityManager()) {
            em.getTransaction().begin();
            roomRepository.remove(room, em);
            em.getTransaction().commit();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
