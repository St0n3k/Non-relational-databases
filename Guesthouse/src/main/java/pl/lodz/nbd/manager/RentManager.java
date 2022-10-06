package pl.lodz.nbd.manager;

import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import pl.lodz.nbd.model.Client;
import pl.lodz.nbd.model.Rent;
import pl.lodz.nbd.model.Room;
import pl.lodz.nbd.repository.ClientRepository;
import pl.lodz.nbd.repository.EntityManagerCreator;
import pl.lodz.nbd.repository.RentRepository;
import pl.lodz.nbd.repository.RoomRepository;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
public class RentManager {

    private ClientRepository clientRepository;
    private RoomRepository roomRepository;
    private RentRepository rentRepository;

    public Rent rentRoom(Date beginTime, Date endTime, boolean board, String clientPersonalId, int roomNumber) {

        EntityManager em = EntityManagerCreator.getEntityManager();

        Client client = clientRepository.getClientByPersonalId(clientPersonalId, em);
        Room room = roomRepository.getByRoomNumber(roomNumber, em);
        List<Rent> roomRentList = rentRepository.getByRoomNumber(roomNumber, em);

        System.out.println(roomRentList);

        //TODO calculate cost
        double finalCost = 1000.0;

        //TODO check if dates are not colliding with actual rents of room
        Rent rent = new Rent(beginTime, endTime, board, finalCost, client, room);

        em.getTransaction().begin();
        rentRepository.add(rent, em);
        em.getTransaction().commit();

        return rent;


    }

}
