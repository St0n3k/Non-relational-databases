package pl.lodz.nbd;


import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import pl.lodz.nbd.manager.ClientManager;
import pl.lodz.nbd.manager.RentManager;
import pl.lodz.nbd.manager.RoomManager;
import pl.lodz.nbd.model.Address;
import pl.lodz.nbd.model.Client;
import pl.lodz.nbd.model.Rent;
import pl.lodz.nbd.model.Room;
import pl.lodz.nbd.repository.*;


import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class TestClass {

    private static final AddressRepository addressRepository = new AddressRepository();
    private static final RoomRepository roomRepository = new RoomRepository();
    private static final ClientRepository clientRepository = new ClientRepository();
    private static final RentRepository rentRepository = new RentRepository();


    @Test
    void registerClientTest() {
        ClientManager clientManager = new ClientManager(addressRepository, clientRepository);

        //Check if clients are persisted
        assertNotNull(clientManager.registerClient("Marek", "Kowalski", "000333", "Warszawa", "Astronautów", 1));
        assertNotNull(clientManager.registerClient("Jan", "Matejko", "000222", "Łódź", "Wesoła", 32));

        //Check if client is not persisted (existing personalId)
        assertNull(clientManager.registerClient("Jakub", "Polak", "000222", "Kraków", "Słoneczna", 133));

        //Check if getByPersonalId works properly
        assertNotNull(clientManager.getByPersonalId("000333"));
        assertNotNull(clientManager.getByPersonalId("000222"));
        assertNull(clientManager.getByPersonalId("000444"));

        //Check if address was persisted
        EntityManager em = EntityManagerCreator.getEntityManager();
        Client client = clientManager.getByPersonalId("000333");
        Address address = client.getAddress();

        assertNotNull(address.getId());
        assertNotNull(addressRepository.getById(address.getId(), em));
        assertNull(addressRepository.getById(10L, em));

    }

    @Test
    void addRoomTest() {
        RoomManager roomManager = new RoomManager(roomRepository);

        //Check if rooms are persisted
        assertNotNull(roomManager.addRoom(100.0, 2, 100));
        assertNotNull(roomManager.addRoom(200.0, 3, 101));

        //Check if room is not persisted(existing room number)
        assertNull(roomManager.addRoom(1000.0, 5, 100));

        //Check if getRoomByNumber works properly
        assertNotNull(roomManager.getByRoomNumber(100));
        assertNotNull(roomManager.getByRoomNumber(101));
        assertNull(roomManager.getByRoomNumber(300));
    }

    @Test
    void rentRoomTest() {
        RoomManager roomManager = new RoomManager(roomRepository);
        RentManager rentManager = new RentManager(clientRepository, roomRepository, rentRepository);
        ClientManager clientManager = new ClientManager(addressRepository, clientRepository);

        Client client = clientManager.registerClient("Marek", "Kowalski", "000566", "Warszawa", "Astronautów", 1);
        Room room = roomManager.addRoom(100.0, 2, 400);
        Rent rent = rentManager.rentRoom(LocalDateTime.now(), LocalDateTime.of(LocalDate.ofYearDay(2022, 282), LocalTime.NOON), true, client.getPersonalId(), room.getRoomNumber());
        //Rent rent2 = rentManager.rentRoom(LocalDateTime.now(), LocalDateTime.of(LocalDate.ofYearDay(2022, 283), LocalTime.NOON), true, client.getPersonalId(), room.getRoomNumber());
        System.out.println(rent);
    }

}
