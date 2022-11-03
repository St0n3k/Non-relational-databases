package pl.lodz.nbd.manager;

import lombok.AllArgsConstructor;
import pl.lodz.nbd.model.Address;
import pl.lodz.nbd.model.Client;
import pl.lodz.nbd.model.ClientTypes.ClientType;
import pl.lodz.nbd.model.ClientTypes.Default;
import pl.lodz.nbd.repository.impl.ClientRepository;
import pl.lodz.nbd.repository.impl.ClientTypeRepository;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public class ClientManager {

    private ClientRepository clientRepository;
    private ClientTypeRepository clientTypeRepository;

    public Optional<Client> registerClient(String firstName, String lastName, String personalId, String city, String street, int number) {

        Address address = new Address(city, street, number);
        ClientType defaultClientType = clientTypeRepository.getByType(Default.class);
        Client client = new Client(firstName, lastName, personalId, address, defaultClientType);
        
        return clientRepository.add(client);
    }

    public List<Client> getAllClients() {
        return clientRepository.getAll();
    }

    public Optional<Client> getByPersonalId(String personalId) {
        return clientRepository.getClientByPersonalId(personalId);
    }

    public void removeClient(Client client) {
        clientRepository.remove(client);
    }

    public boolean updateClient(Client client) {
        return clientRepository.update(client);
    }

    public void changeTypeTo(Client client, Class newType) {
        ClientType ct = clientTypeRepository.getByType(newType);
        client.setClientType(ct);
        updateClient(client);
    }
}
