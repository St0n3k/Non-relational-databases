package pl.lodz.nbd.manager;

import jakarta.persistence.EntityManager;
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


        em.getTransaction().begin();
        em.persist(address);
        em.persist(client);
        em.getTransaction().commit();
        em.close();

        return client;
    }
}
