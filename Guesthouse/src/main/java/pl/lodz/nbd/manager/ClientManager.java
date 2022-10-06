package pl.lodz.nbd.manager;

import jakarta.persistence.EntityManager;
import jakarta.persistence.RollbackException;
import lombok.AllArgsConstructor;
import pl.lodz.nbd.model.Address;
import pl.lodz.nbd.model.Client;
import pl.lodz.nbd.repository.AddressRepository;
import pl.lodz.nbd.repository.ClientRepository;
import pl.lodz.nbd.repository.EntityManagerCreator;

@AllArgsConstructor
public class ClientManager {

    private AddressRepository addressRepository;
    private ClientRepository clientRepository;

    public Client registerClient(String firstName, String lastName, String personalId, boolean archived, String city, String street, int number) {
        Address address = new Address(city, street, number);
        Client client = new Client(firstName, lastName, personalId, archived, address);

        EntityManager em = EntityManagerCreator.getEntityManager();

        try {
            em.getTransaction().begin();
            em.persist(address);
            em.persist(client);
            em.getTransaction().commit();
        } catch (RollbackException e){
            em.getTransaction().rollback();
            em.close();
            return null;
        }
        em.close();

        return client;
    }
}
