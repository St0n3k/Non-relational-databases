package pl.lodz.nbd.manager;

import lombok.AllArgsConstructor;
import pl.lodz.nbd.model.Address;
import pl.lodz.nbd.model.Client;
import pl.lodz.nbd.model.ClientTypes.ClientType;
import pl.lodz.nbd.model.ClientTypes.Default;
import pl.lodz.nbd.repository.impl.ClientRepository;
import pl.lodz.nbd.repository.impl.ClientTypeRepository;

import java.util.List;

@AllArgsConstructor
public class ClientManager {

    private ClientRepository clientRepository;
    private ClientTypeRepository clientTypeRepository;

    public Client registerClient(String firstName, String lastName, String personalId, String city, String street, int number) {

        Address address = new Address(city, street, number);
        ClientType defaultClientType = new Default();//clientTypeRepository.getByType(Default.class);
        Client client = new Client(firstName, lastName, personalId, address, defaultClientType);

        System.out.println(client.getUuid());
        return clientRepository.add(client);
    }

    public List<Client> getAllClients() {
        return clientRepository.getAll();
    }

    public Client getByPersonalId(String personalId) {
        return clientRepository.getClientByPersonalId(personalId);
    }

    public void removeClient(Client client) {
        clientRepository.remove(client);
    }

//    public Client updateClient(Client client) {
//        return clientRepository.update(client);
//    }

//    public Client changeTypeTo(Class type, Client client) {
//        client.setClientType(clientTypeRepository.getByType(type));
//
//        client = updateClient(client);
//        return client;
//    }
}
