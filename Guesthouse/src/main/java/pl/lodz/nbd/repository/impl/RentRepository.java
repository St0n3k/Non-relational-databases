package pl.lodz.nbd.repository.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.RollbackException;
import pl.lodz.nbd.common.EntityManagerCreator;
import pl.lodz.nbd.model.Client;
import pl.lodz.nbd.model.Rent;
import pl.lodz.nbd.model.Room;
import pl.lodz.nbd.repository.Repository;

import java.time.LocalDateTime;
import java.util.List;

public class RentRepository implements Repository<Rent> {

    private Rent repeatableTransaction(Rent rent) {
        try (EntityManager em = EntityManagerCreator.getEntityManager()) {
            em.getTransaction().begin();

            Room room = em.find(Room.class, rent.getRoom().getId());

            em.lock(room, LockModeType.OPTIMISTIC_FORCE_INCREMENT);

            boolean isColliding = isColliding(
                    rent.getBeginTime(),
                    rent.getEndTime(),
                    room.getRoomNumber());

            if (isColliding) return null;

            em.persist(rent);

            em.getTransaction().commit();
            return rent;
        }
    }

    @Override
    public Rent add(Rent rent) {
        try {
            return repeatableTransaction(rent);

        } catch (RollbackException re) {
            System.out.println("Repeating");
            return repeatableTransaction(rent);

        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public boolean remove(Rent rent) {
        try (EntityManager em = EntityManagerCreator.getEntityManager()) {
            em.getTransaction().begin();
            em.remove(em.find(Rent.class, rent.getId()));
            em.getTransaction().commit();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Rent getById(Long id) {
        try (EntityManager em = EntityManagerCreator.getEntityManager()) {
            return em.find(Rent.class, id);
        }
    }

    @Override
    public List<Rent> getAll() {
        try (EntityManager em = EntityManagerCreator.getEntityManager()) {
            return em.createNamedQuery("Rent.getAll", Rent.class).getResultList();
        }
    }

    public List<Rent> getByRoomNumber(int roomNumber) {
        try (EntityManager em = EntityManagerCreator.getEntityManager()) {
            return em.createNamedQuery("Rent.getByRoomNumber", Rent.class).setParameter("roomNumber", roomNumber).getResultList();
        }
    }

    public List<Rent> getByClientPersonalId(String personalId) {
        try (EntityManager em = EntityManagerCreator.getEntityManager()) {
            return em.createNamedQuery("Rent.getByClientPersonalId", Rent.class).setParameter("personalId", personalId).getResultList();
        }
    }

    public Rent update(Rent rent) {
        try (EntityManager em = EntityManagerCreator.getEntityManager()) {
            em.getTransaction().begin();
            Rent newRent = em.merge(rent);
            em.getTransaction().commit();
            return newRent;
        } catch (Exception e) {
            return null;
        }
    }

    public boolean isColliding(LocalDateTime beginDate, LocalDateTime endDate, int roomNumber) {
        try (EntityManager em = EntityManagerCreator.getEntityManager()) {
            List<Rent> rentsColliding =
                    em.createNamedQuery("Rent.getRentsColliding", Rent.class)
                    .setParameter("beginDate", beginDate)
                    .setParameter("endDate", endDate)
                    .setParameter("roomNumber", roomNumber)
                    .getResultList();

            return !rentsColliding.isEmpty();
        } catch (Exception e) {
            return true;
        }
    }

}
