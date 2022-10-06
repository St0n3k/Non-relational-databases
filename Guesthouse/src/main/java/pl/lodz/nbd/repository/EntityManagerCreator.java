package pl.lodz.nbd.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.PersistenceUnit;

public class EntityManagerCreator {
    
    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("guesthouse");

    public static EntityManager getEntityManager() {
        return emf.createEntityManager();
    }
}
