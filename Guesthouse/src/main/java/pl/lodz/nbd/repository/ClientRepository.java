package pl.lodz.nbd.repository;

import jakarta.persistence.EntityManager;
import pl.lodz.nbd.model.Client;

public class ClientRepository implements Repository<Client> {
    @Override
    public void add(Client item, EntityManager em) {

    }
}
