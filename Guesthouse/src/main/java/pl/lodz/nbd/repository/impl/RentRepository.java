package pl.lodz.nbd.repository.impl;

import jakarta.persistence.EntityManager;
import pl.lodz.nbd.model.Rent;
import pl.lodz.nbd.repository.Repository;

import java.util.List;

public class RentRepository implements Repository<Rent> {
    @Override
    public void add(Rent rent, EntityManager em) {
        em.persist(rent);
    }

    @Override
    public void remove(Rent rent, EntityManager em) {
        em.remove(getById(rent.getId(), em));
    }

    @Override
    public Rent getById(Long id, EntityManager em) {
        return em.find(Rent.class, id);
    }

    @Override
    public List<Rent> getAll(EntityManager em) {
        return em.createNamedQuery("Rent.getAll", Rent.class).getResultList();
    }

    public List<Rent> getByRoomNumber(int roomNumber, EntityManager em) {
        return em.createNamedQuery("Rent.getByRoomNumber", Rent.class).setParameter("roomNumber", roomNumber).getResultList();
    }
}
