package pl.lodz.nbd;

import jakarta.persistence.EntityManager;
import jakarta.persistence.RollbackException;
import org.junit.jupiter.api.Test;
import pl.lodz.nbd.manager.ClientManager;
import pl.lodz.nbd.manager.RoomManager;
import pl.lodz.nbd.model.Address;
import pl.lodz.nbd.model.Client;
import pl.lodz.nbd.model.Rent;
import pl.lodz.nbd.model.Room;
import pl.lodz.nbd.repository.*;

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
        assertNotNull(clientManager.registerClient("Marek", "Kowalski", "000333", false, "Warszawa", "Astronautów", 1));
        assertNotNull(clientManager.registerClient("Jan", "Matejko", "000222", false, "Łódź", "Wesoła", 32));

        //TODO getByPersonalId returns not null

        //Check if client is not persisted (existing personalId)
        assertNull(clientManager.registerClient("Jakub", "Polak", "000222", false, "Kraków", "Słoneczna", 133));
    }


    @Test
    void addRoomTest() {
        RoomManager roomManager = new RoomManager(roomRepository);

        //Check if rooms are persisted
        assertNotNull(roomManager.addRoom(100.0, 2, 100));
        assertNotNull(roomManager.addRoom(200.0, 3, 101));

        //TODO getByRoomNumber returns not null

        //Check if room is not persisted(existing room number)
        assertNull(roomManager.addRoom(1000.0, 5, 100));
    }

}
