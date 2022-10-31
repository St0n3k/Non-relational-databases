package pl.lodz.nbd;

import org.junit.jupiter.api.Test;
import pl.lodz.nbd.manager.ClientManager;
import pl.lodz.nbd.model.Client;
import pl.lodz.nbd.model.ClientTypes.Bronze;
import pl.lodz.nbd.model.ClientTypes.Default;
import pl.lodz.nbd.model.ClientTypes.Gold;
import pl.lodz.nbd.model.ClientTypes.Silver;
import pl.lodz.nbd.repository.impl.ClientRepository;
import pl.lodz.nbd.repository.impl.ClientTypeRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class ClientTest {

    private static final ClientRepository clientRepository = new ClientRepository();
    private static final ClientTypeRepository clientTypeRepository = new ClientTypeRepository();

    @Test
    void positiveRegisterClientTest() {
        ClientManager clientManager = new ClientManager(clientRepository, clientTypeRepository);

        assertNotNull(clientManager.registerClient("Aleksander",
                "Wichrzyński", "12345", "Warszawa", "Smutna", 7));

    }

    @Test
    void getAllClientsTest() {
        ClientManager clientManager = new ClientManager(clientRepository, clientTypeRepository);

        List<Client> clients = clientManager.getAllClients();
        assertNotNull(clients);
        assertTrue(clients.size() > 0);
        System.out.println(clients);
        clients.forEach((client -> System.out.println(client.getClientType().getClass())));

    }

    @Test
    void addClientTypes() {
        clientTypeRepository.add(new Default());
        clientTypeRepository.add(new Bronze());
        clientTypeRepository.add(new Silver());
        clientTypeRepository.add(new Gold());
    }

    @Test
    void getClientTypes() {
        assertTrue(clientTypeRepository.getByType(Default.class) instanceof Default);
        assertTrue(clientTypeRepository.getByType(Bronze.class) instanceof Bronze);
        assertTrue(clientTypeRepository.getByType(Silver.class) instanceof Silver);
        assertTrue(clientTypeRepository.getByType(Gold.class) instanceof Gold);
    }


    @Test
    void clientTypeTest() {
        System.out.println(clientTypeRepository.getGoldClientType().getClass());
    }

//    @Test
//    void registerAndUpdateClientTest() {
//        ClientManager clientManager = new ClientManager(clientRepository, clientTypeRepository);
//
//        //Check if clients are persisted
//        assertNotNull(clientManager.registerClient("Jakub", "Konieczny", "000333", "Warszawa", "Gorna", 16));
//        assertNotNull(clientManager.registerClient("Anna", "Matejko", "000222", "Łódź", "Wesoła", 32));
//
//        //Check if client is not persisted (existing personalId)
//        assertNull(clientManager.registerClient("Mateusz", "Polak", "000222", "Kraków", "Słoneczna", 133));
//
//        //Check if getByPersonalId works properly
//        assertNotNull(clientManager.getByPersonalId("000333"));
//        assertNotNull(clientManager.getByPersonalId("000222"));
//        assertNull(clientManager.getByPersonalId("000444"));
//    }
//
//    @Test
//    void updateClientTest() {
//        ClientManager clientManager = new ClientManager(clientRepository, clientTypeRepository);
//
//        clientManager.registerClient("Jan", "Matejko", "000211", "Łódź", "Wesoła", 32);
//        Client client = clientManager.getByPersonalId("000211");
//        assertEquals(client.getVersion(), 0);
//
//        client.setFirstName("Marcin");
//        Client client2 = clientManager.updateClient(client);
//
//        //Check if version field incremented
//        assertEquals(client2.getVersion(), 1);
//        assertEquals(client2.getFirstName(), "Marcin");
//    }


}
