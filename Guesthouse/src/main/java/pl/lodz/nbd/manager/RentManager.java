package pl.lodz.nbd.manager;

import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.RollbackException;
import lombok.AllArgsConstructor;
import pl.lodz.nbd.common.EntityManagerCreator;
import pl.lodz.nbd.model.Client;
import pl.lodz.nbd.model.ClientTypes.ClientType;
import pl.lodz.nbd.model.Rent;
import pl.lodz.nbd.model.Room;
import pl.lodz.nbd.repository.impl.ClientRepository;
import pl.lodz.nbd.repository.impl.RentRepository;
import pl.lodz.nbd.repository.impl.RoomRepository;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
public class RentManager {

    private ClientRepository clientRepository;
    private RoomRepository roomRepository;
    private RentRepository rentRepository;

    public List<Rent> getAllRentsOfRoom(int roomNumber) {
        try (EntityManager em = EntityManagerCreator.getEntityManager()) {
            return rentRepository.getByRoomNumber(roomNumber, em);
        } catch (Exception e) {
            return null;
        }
    }

    public Rent rentRoom(LocalDateTime beginTime, LocalDateTime endTime, boolean board, String clientPersonalId, int roomNumber) {

        //Guard clause
        if (beginTime.isAfter(endTime)) return null;

        return repeatableTransaction(beginTime, endTime, board, clientPersonalId, roomNumber);
    }

    private Rent repeatableTransaction(LocalDateTime beginTime, LocalDateTime endTime, boolean board, String clientPersonalId, int roomNumber) {
        try (EntityManager em = EntityManagerCreator.getEntityManager()) {

            em.getTransaction().begin();

            Client client = clientRepository.getClientByPersonalId(clientPersonalId, em);
            Room room = roomRepository.getByRoomNumber(roomNumber, em);
            em.lock(room, LockModeType.OPTIMISTIC_FORCE_INCREMENT);

            if (client == null || room == null) return null;

            List<Rent> roomRentList = rentRepository.getByRoomNumber(roomNumber, em);

            boolean isColliding = roomRentList.stream()
                    .anyMatch(rent -> isColliding(rent, beginTime, endTime));

            if (isColliding) return null;

            double finalCost = calculateTotalCost(beginTime, endTime, room.getPrice(), board, client.getClientType());

            Rent rent = new Rent(beginTime, endTime, board, finalCost, client, room);
            rentRepository.add(rent, em);

            em.getTransaction().commit();

            return rent;

        } catch (RollbackException e) {
//            System.out.println("Repeating transaction");
//            e.printStackTrace();5
            return repeatableTransaction(beginTime, endTime, board, clientPersonalId, roomNumber);
        } catch (Exception e) {
            return null;
        }
    }

    private boolean isColliding(Rent rent, LocalDateTime beginTime, LocalDateTime endTime) {
        return !rent.getBeginTime().isAfter(endTime) && !rent.getEndTime().isBefore(beginTime);
    }

    private double calculateTotalCost(LocalDateTime beginTime, LocalDateTime endTime, double costPerDay, boolean board, ClientType clientType) {
        Duration duration = Duration.between(beginTime, endTime);
        if (board) costPerDay += 50; //Daily board is worth 50
        return clientType.applyDiscount(Math.ceil(duration.toHours() / 24.0) * costPerDay);
    }

}
