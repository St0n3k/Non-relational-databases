package pl.lodz.nbd.repository.impl;

import jakarta.persistence.EntityManager;
import pl.lodz.nbd.model.ClientTypes.ClientType;
import pl.lodz.nbd.repository.Repository;

import java.util.List;

public class ClientTypeRepository implements Repository<ClientType> {
    @Override
    public void add(ClientType item, EntityManager em) {
        em.persist(item);
    }

    @Override
    public void remove(ClientType item, EntityManager em) {
        em.remove(getById(item.getId(), em));
    }

    @Override
    public ClientType getById(Long id, EntityManager em) {
        return em.find(ClientType.class, id);
    }

    @Override
    public ClientType update(ClientType item, EntityManager em) {
        return em.merge(item);
    }

    @Override
    public List<ClientType> getAll(EntityManager em) {
        return em.createNamedQuery("ClientType.getAll", ClientType.class).getResultList();
    }

    public ClientType getByType(Class type, EntityManager em) {
        List<ClientType> result = em.createNamedQuery("ClientType.getByType", ClientType.class).setParameter("type", type.getSimpleName()).getResultList();

        if (result.isEmpty()) {
            return null;
        } else {
            return result.get(0);
        }
    }


}
