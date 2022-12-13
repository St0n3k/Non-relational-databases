package pl.lodz.nbd;

import org.junit.jupiter.api.Test;
import pl.lodz.nbd.common.RepositoryCreator;
import pl.lodz.nbd.model.Client;
import pl.lodz.nbd.model.ClientTypes.*;
import pl.lodz.nbd.repository.ClientRepository;
import pl.lodz.nbd.repository.ClientTypeRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class ClientTest {

    private static final ClientRepository clientRepository = RepositoryCreator.getClientRepository();
    private static final ClientTypeRepository clientTypeRepository = RepositoryCreator.getClientTypeRepository();

    @Test
    void clientTypesTest() {
        Default defaultCt = new Default();
        Bronze bronzeCt = new Bronze();
        Silver silverCt = new Silver();
        Gold goldCt = new Gold();

        clientTypeRepository.add(defaultCt);
        clientTypeRepository.add(bronzeCt);
        clientTypeRepository.add(silverCt);
        clientTypeRepository.add(goldCt);

        List<ClientType> clientTypes = clientTypeRepository.getAll();
        assertEquals(4, clientTypes.size());

        Optional<ClientType> optionalClientType = clientTypeRepository.getByType("Bronze");
        assertTrue(optionalClientType.isPresent());

        ClientType clientType = optionalClientType.get();
        assertEquals(0.05, clientType.getDiscount());

        clientType.setDiscount(0.07);
        clientTypeRepository.update(clientType);

        optionalClientType = clientTypeRepository.getByType("Bronze");
        assertTrue(optionalClientType.isPresent());

        clientType = optionalClientType.get();
        assertEquals(0.07, clientType.getDiscount());

        clientTypeRepository.remove(clientType);
        optionalClientType = clientTypeRepository.getByType("Bronze");
        assertTrue(optionalClientType.isEmpty());
    }


    @Test
    void positiveRegisterClientTest() {
        Client client = new Client("1234", "Wojciech", "Szczęsny", new Default());
        clientRepository.add(client);
        Optional<Client> optionalClient = clientRepository.getClientByPersonalId("1234");
        assertTrue(optionalClient.isPresent());

        Client repoClient = optionalClient.get();
        assertEquals(client, repoClient);
    }

    @Test
    void negativeRegisterClientTest() {
        Client client1 = new Client("2234", "Kamil", "Glik", new Default());
        Client client2 = new Client("2234", "Wojciech", "Szczęsny", new Default());

        assertTrue(clientRepository.add(client1));
        assertFalse(clientRepository.add(client2));
    }

    @Test
    void getAllClientsTest() {
        Client client = new Client("32234", "Mariusz", "Pudzianowski", new Default());
        assertTrue(clientRepository.add(client));

        List<Client> clients = clientRepository.getAll();

        assertTrue(clients.stream().anyMatch(c -> c.equals(client)));
    }

    @Test
    void updateClientTest() {
        Client client = new Client("432234", "Rafał", "Strzałkowski", new Silver());
        assertTrue(clientRepository.add(client));

        Optional<Client> optionalClient = clientRepository.getClientByPersonalId("432234");
        assertTrue(optionalClient.isPresent());

        Client repoClient = optionalClient.get();
        assertEquals(repoClient.getClientType(), new Silver());

        repoClient.setClientType(new Gold());
        clientRepository.update(repoClient);

        optionalClient = clientRepository.getClientByPersonalId("432234");
        assertTrue(optionalClient.isPresent());

        repoClient = optionalClient.get();
        assertEquals(repoClient.getClientType(), new Gold());
    }

    @Test
    void removeClientTest() {
        Client client = new Client("434", "Kamil", "Graczyk", new Bronze());
        assertTrue(clientRepository.add(client));

        Optional<Client> optionalClient = clientRepository.getClientByPersonalId("434");
        assertTrue(optionalClient.isPresent());

        clientRepository.remove(client);
        optionalClient = clientRepository.getClientByPersonalId("434");
        assertTrue(optionalClient.isEmpty());
    }
}