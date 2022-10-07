package pl.lodz.nbd;


import jakarta.validation.ValidationException;
import org.junit.jupiter.api.Test;
import pl.lodz.nbd.manager.ClientManager;
import pl.lodz.nbd.manager.RentManager;
import pl.lodz.nbd.manager.RoomManager;
import pl.lodz.nbd.model.Address;
import pl.lodz.nbd.model.Client;
import pl.lodz.nbd.model.Rent;
import pl.lodz.nbd.model.Room;
import pl.lodz.nbd.repository.impl.ClientRepository;
import pl.lodz.nbd.repository.impl.RentRepository;
import pl.lodz.nbd.repository.impl.RoomRepository;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class TestClass {

    //TODO divide test into specific test classes

    private static final RoomRepository roomRepository = new RoomRepository();
    private static final ClientRepository clientRepository = new ClientRepository();
    private static final RentRepository rentRepository = new RentRepository();


    @Test
    void registerClientTest() {
        ClientManager clientManager = new ClientManager(clientRepository);

        //Check if clients are persisted
        assertNotNull(clientManager.registerClient("Marek", "Kowalski", "000333", "Warszawa", "Astronautów", 1));
        assertNotNull(clientManager.registerClient("Jan", "Matejko", "000222", "Łódź", "Wesoła", 32));

        //Check if client is not persisted (existing personalId)
        assertNull(clientManager.registerClient("Jakub", "Polak", "000222", "Kraków", "Słoneczna", 133));

        //Check if getByPersonalId works properly
        assertNotNull(clientManager.getByPersonalId("000333"));
        assertNotNull(clientManager.getByPersonalId("000222"));
        assertNull(clientManager.getByPersonalId("000444"));

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
        ClientManager clientManager = new ClientManager(clientRepository);

        Client client = clientManager.registerClient("Marek", "Kowalski", "000566", "Warszawa", "Astronautów", 1);
        Room room = roomManager.addRoom(100.0, 2, 400);

        //Rent create success, check if it is persisted and total cost is calculated properly(add 50 to costPerDay, because of board option)
        Rent rentThreeDays = rentManager.rentRoom(LocalDateTime.now(), LocalDateTime.now().plusDays(3), true, client.getPersonalId(), room.getRoomNumber());
        assertNotNull(rentThreeDays);
        assertEquals(rentThreeDays.getFinalCost(), (100.0 + 50.0) * 3);

        //Rent create success, check if it persisted and days are rounded up(no board option)
        Rent rentThirtyHours = rentManager.rentRoom(LocalDateTime.now().plusDays(4), LocalDateTime.now().plusDays(4).plusHours(30), false, client.getPersonalId(), room.getRoomNumber());
        assertNotNull(rentThirtyHours);
        assertEquals(rentThirtyHours.getFinalCost(), 100.0 * 2);

        //Rent create fail, dates are colliding with other rent of the room
        assertNull(rentManager.rentRoom(LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(5), true, client.getPersonalId(), room.getRoomNumber()));

        //Rent create fail, client doesn't exist
        assertNull(rentManager.rentRoom(LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(5), true, "111111", room.getRoomNumber()));

        //Rent create fail, room doesn't exist
        assertNull(rentManager.rentRoom(LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(5), true, client.getPersonalId(), 999));
    }

    @Test
    void validatorTest() {
        //Null city
        assertThrows(ValidationException.class, () -> new Address(null, "Gorna", 11));

        //Null address
        assertThrows(ValidationException.class, () -> new Client("Rafał", "Strzałkowski", "0003334", null));

        //Null client and room
        assertThrows(ValidationException.class, () -> new Rent(LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(5), true, 1000.0, null, null));
    }
}
