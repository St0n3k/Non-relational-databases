package pl.lodz.nbd.manager;

import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import pl.lodz.nbd.common.EntityManagerCreator;
import pl.lodz.nbd.model.Address;
import pl.lodz.nbd.model.Client;
import pl.lodz.nbd.model.ClientTypes.ClientType;
import pl.lodz.nbd.model.ClientTypes.Default;
import pl.lodz.nbd.repository.impl.ClientRepository;
import pl.lodz.nbd.repository.impl.ClientTypeRepository;

@AllArgsConstructor
public class ClientManager {

    private ClientRepository clientRepository;
    private ClientTypeRepository clientTypeRepository;

    public Client registerClient(String firstName, String lastName, String personalId, String city, String street, int number) {

        try (EntityManager em = EntityManagerCreator.getEntityManager()) {
            //Values are validated in constructors
            Address address = new Address(city, street, number);
            ClientType defaultClientType = clientTypeRepository.getByType(Default.class, em);
            Client client = new Client(firstName, lastName, personalId, address, defaultClientType);

            em.getTransaction().begin();
            clientRepository.add(client, em);
            em.getTransaction().commit();

            return client;

        } catch (Exception e) {
            return null;
        }
    }

    public Client getByPersonalId(String personalId) {
        try (EntityManager em = EntityManagerCreator.getEntityManager()) {
            return clientRepository.getClientByPersonalId(personalId, em);
        } catch (Exception e) {
            return null;
        }
    }

    public boolean removeClient(Client client) {
        try (EntityManager em = EntityManagerCreator.getEntityManager()) {
            em.getTransaction().begin();
            clientRepository.remove(client, em);
            em.getTransaction().commit();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Client updateClient(Client client) {
        try (EntityManager em = EntityManagerCreator.getEntityManager()) {
            em.getTransaction().begin();
            Client clientSaved = clientRepository.update(client, em);
            em.getTransaction().commit();
            return clientSaved;
        } catch (Exception e) {
            return null;
        }
    }

    public Client changeTypeTo(Class type, Client client) {
        try (EntityManager em = EntityManagerCreator.getEntityManager()) {
            client.setClientType(clientTypeRepository.getByType(type, em));
            return updateClient(client);
        } catch (Exception e) {
            return null;
        }
    }
}
