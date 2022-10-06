package pl.lodz.nbd.repository;

import jakarta.persistence.EntityManager;
import pl.lodz.nbd.model.Address;

public class AddressRepository implements Repository<Address> {
    @Override
    public void add(Address address, EntityManager em) {
        em.persist(address);
    }
}
