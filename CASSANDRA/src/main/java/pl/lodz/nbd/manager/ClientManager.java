package pl.lodz.nbd.manager;

import lombok.AllArgsConstructor;
import pl.lodz.nbd.model.Client;
import pl.lodz.nbd.model.ClientTypes.Default;
import pl.lodz.nbd.repository.ClientRepository;
import pl.lodz.nbd.repository.ClientTypeRepository;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public class ClientManager {

    private ClientRepository clientRepository;
    private ClientTypeRepository clientTypeRepository;

    public boolean registerClient(String firstName, String lastName, String personalId, String city, String street, int number) {
        Client client = new Client(personalId, firstName, lastName, new Default());
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

    public void updateClient(Client client) {
        clientRepository.update(client);
    }
}
