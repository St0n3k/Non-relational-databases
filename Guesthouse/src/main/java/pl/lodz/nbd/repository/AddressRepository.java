package pl.lodz.nbd.repository;

import jakarta.persistence.*;
import pl.lodz.nbd.model.Address;

public class AddressRepository implements Repository<Address> {
    @Override
    public void add(Address item, EntityManager em) {
        em.persist(item);
    }
}
