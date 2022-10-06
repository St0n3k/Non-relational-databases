package pl.lodz.nbd;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import pl.lodz.nbd.model.Address;
import pl.lodz.nbd.repository.AddressRepository;
import pl.lodz.nbd.repository.EntityManagerCreator;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestClass {

    private static AddressRepository addressRepository = new AddressRepository();

    @BeforeAll
    static void beforeAll() {
        addressRepository = new AddressRepository();
    }

    @Test
    void test1() {
        EntityManager em = EntityManagerCreator.getEntityManager();

        Address address = new Address("Łódź", "Astronautów", 41);

        em.getTransaction().begin();
        addressRepository.add(address, em);
        //We can save other entities there in the same transaction (Atomic)
        em.getTransaction().commit();
    }

}
