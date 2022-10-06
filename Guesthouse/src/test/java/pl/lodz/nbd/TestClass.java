package pl.lodz.nbd;

import jakarta.persistence.EntityManager;
import jakarta.persistence.RollbackException;
import org.junit.jupiter.api.Test;
import pl.lodz.nbd.manager.ClientManager;
import pl.lodz.nbd.model.Address;
import pl.lodz.nbd.model.Client;
import pl.lodz.nbd.model.Rent;
import pl.lodz.nbd.model.Room;
import pl.lodz.nbd.repository.*;

import java.util.Date;

public class TestClass {

    private static final AddressRepository addressRepository = new AddressRepository();
    private static final RoomRepository roomRepository = new RoomRepository();
    private static final ClientRepository clientRepository = new ClientRepository();
    private static final RentRepository rentRepository = new RentRepository();


    @Test
    void registerClientTest() {
        ClientManager clientManager = new ClientManager(addressRepository, clientRepository);
        clientManager.registerClient("Marek", "Kowalski", "000333", false, "Warszawa", "Astronautów", 1);

    }

    @Test
    void test1() {
        EntityManager em = EntityManagerCreator.getEntityManager();

        Address address = new Address("Łódź", "Astronautów", 42);
        Room room = new Room(100, 100.0, false, 1);
        Client client = new Client("Jan", "Kowalski", "000333", false, address);
        Rent rent = new Rent(new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis()), false, 100.0, client, room);

        em.getTransaction().begin();
        try{
            addressRepository.add(address, em);
            roomRepository.add(room, em);
            clientRepository.add(client, em);
            rentRepository.add(rent, em);
            em.getTransaction().commit();
        }catch(RollbackException e){
            em.getTransaction().rollback();
        }
        em.close();
    }

}
