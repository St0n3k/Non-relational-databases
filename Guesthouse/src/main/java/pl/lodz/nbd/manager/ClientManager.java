package pl.lodz.nbd.manager;

import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import pl.lodz.nbd.common.EntityManagerCreator;
import pl.lodz.nbd.model.Address;
import pl.lodz.nbd.model.Client;
import pl.lodz.nbd.repository.impl.ClientRepository;

@AllArgsConstructor
public class ClientManager {

    private ClientRepository clientRepository;

    public Client registerClient(String firstName, String lastName, String personalId, String city, String street, int number) {

        try (EntityManager em = EntityManagerCreator.getEntityManager()) {
            //Values are validated in constructors
            Address address = new Address(city, street, number);
            Client client = new Client(firstName, lastName, personalId, address);

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

    public Client updateClient(Client client) {
        try (EntityManager em = EntityManagerCreator.getEntityManager()) {
            em.getTransaction().begin();
            Client clientSaved = clientRepository.updateClient(client, em);
            em.getTransaction().commit();
            return clientSaved;
        } catch (Exception e) {
            return null;
        }
    }
}
