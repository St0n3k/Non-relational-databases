package pl.lodz.nbd.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import pl.lodz.nbd.model.Address;

public class AddressRepository implements Repository<Address> {

    private final EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");
    private final EntityManager em = emf.createEntityManager();

    @Override
    public void add(Address item) {
        EntityTransaction transaction = em.getTransaction();

        transaction.begin();
        em.persist(item);
        transaction.commit();
    }
}
