package pl.lodz.nbd.repository;

import jakarta.persistence.EntityManager;
import pl.lodz.nbd.model.Rent;

public class RentRepository implements Repository<Rent> {
    @Override
    public void add(Rent rent, EntityManager em) {
        em.persist(rent);
    }
}
