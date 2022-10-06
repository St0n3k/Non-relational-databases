package pl.lodz.nbd.repository;

import jakarta.persistence.EntityManager;
import pl.lodz.nbd.model.Address;

import java.util.List;

public class AddressRepository implements Repository<Address> {
    @Override
    public void add(Address address, EntityManager em) {
        em.persist(address);
    }

    @Override
    public void remove(Address address, EntityManager em) {
        em.remove(address);
    }

    @Override
    public Address getById(Long id, EntityManager em) {
        return em.find(Address.class, id);
    }

    @Override
    public List<Address> getAll(EntityManager em) {
        return em.createNamedQuery("Address.getAll", Address.class).getResultList();
    }
}
