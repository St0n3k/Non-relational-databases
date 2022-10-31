package pl.lodz.nbd;

import com.mongodb.MongoWriteException;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import pl.lodz.nbd.manager.ClientManager;
import pl.lodz.nbd.model.Address;
import pl.lodz.nbd.model.Client;
import pl.lodz.nbd.model.ClientTypes.Bronze;
import pl.lodz.nbd.model.ClientTypes.Default;
import pl.lodz.nbd.model.ClientTypes.Gold;
import pl.lodz.nbd.model.ClientTypes.Silver;
import pl.lodz.nbd.repository.impl.ClientRepository;
import pl.lodz.nbd.repository.impl.ClientTypeRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ClientTest {

    private static final ClientRepository clientRepository = new ClientRepository();
    private static final ClientTypeRepository clientTypeRepository = new ClientTypeRepository();
    private static final ClientManager clientManager = new ClientManager(clientRepository, clientTypeRepository);

    @Test
    void positiveRegisterClientTest() {
        assertTrue(clientManager.registerClient("Aleksander",
                "Wicher", "123456", "Warszawa", "Smutna", 7));

    }

    @Test
    void negativeRegisterClientTest() {
        assertTrue(clientManager.registerClient("Aleksander",
                "Wichrzyński", "12345", "Warszawa", "Smutna", 7));
        assertThrows(MongoWriteException.class, () -> clientManager.registerClient("Aleksander",
                "Wichrzyński", "12345", "Warszawa", "Smutna", 7));

    }

    @Test
    void getAllClientsTest() {
        assertTrue(clientManager.registerClient("Jurek",
                "Jurkowski", "123321", "Warszawa", "Smutna", 7));
        List<Client> clients = clientManager.getAllClients();
        assertNotNull(clients);
        assertTrue(clients.size() > 0);

    }

    @Test
    void getClientByIdTest() {
        Address address = new Address("Warszawa", "Smutna", 7);
        Default def = new Default();
        Client client = new Client("Artur", "Boruc", "124421", address, def);
        clientRepository.add(client);
        Client client2 = clientManager.getByPersonalId(client.getPersonalId());
        assertEquals(client, client2);

        Client same = new Client("Artur", "Boruc", "124421", address, def);

        assertNotEquals(client, same);
        assertNotEquals(client2, same);
    }

    @Test
    void addClientTypes() {
        assertNotNull(clientTypeRepository.add(new Default()));
        assertNotNull(clientTypeRepository.add(new Bronze()));
        assertNotNull(clientTypeRepository.add(new Silver()));
        assertNotNull(clientTypeRepository.add(new Gold()));
    }

    @Test
    void getClientTypes() {
        assertTrue(clientTypeRepository.getByType(Default.class) instanceof Default);
        assertTrue(clientTypeRepository.getByType(Bronze.class) instanceof Bronze);
        assertTrue(clientTypeRepository.getByType(Silver.class) instanceof Silver);
        assertTrue(clientTypeRepository.getByType(Gold.class) instanceof Gold);
    }

    @Test
    void updateClientTest() {
        clientManager.registerClient("Jan", "Matejko", "000211", "Łódź", "Wesoła", 32);
        Client client = clientManager.getByPersonalId("000211");
        assertEquals(client.getFirstName(), "Jan");

        client.setFirstName("Marcin");
        Address newAddress = new Address("Ozorków", "Wesoła", 32);
        client.setAddress(newAddress);
        client.setClientType(new Silver());
        assertTrue(clientManager.updateClient(client));

        Client client2 = clientManager.getByPersonalId(client.getPersonalId());
        assertEquals(client2.getFirstName(), "Marcin");
        assertEquals(client2.getAddress().getCity(), "Ozorków");
        assertEquals(client2.getClientType().getDiscount(), new Silver().getDiscount());
    }

    @Test
    void removeClientTest() {
        clientManager.registerClient("Janek", "Testowy", "112211", "Łódź", "Wesoła", 32);
        Client client = clientManager.getByPersonalId("112211");
        assertNotNull(client);
        clientManager.removeClient(client);
        client = clientManager.getByPersonalId("112211");
        assertNull(client);
    }


}
