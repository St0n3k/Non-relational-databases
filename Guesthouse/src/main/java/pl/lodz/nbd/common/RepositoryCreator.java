package pl.lodz.nbd.common;


import pl.lodz.nbd.repository.cache.ClientCacheRepository;
import pl.lodz.nbd.repository.cache.RentCacheRepository;
import pl.lodz.nbd.repository.impl.ClientRepository;
import pl.lodz.nbd.repository.impl.ClientTypeRepository;
import pl.lodz.nbd.repository.impl.RentRepository;
import pl.lodz.nbd.repository.impl.RoomRepository;

public class RepositoryCreator {

    private static final ClientRepository clientRepository = new ClientRepository();
    private static final ClientRepository clientCacheRepository = new ClientCacheRepository();
    private static final ClientTypeRepository clientTypeRepository = new ClientTypeRepository();
    private static final RoomRepository roomRepository = new RoomRepository();
    private static final RentRepository rentRepository = new RentRepository();
    private static final RentRepository rentCacheRepository = new RentCacheRepository();

    public static ClientRepository getClientRepository() {
        return clientRepository;
    }

    public static ClientRepository getClientCacheRepository() {
        return clientCacheRepository;
    }

    public static ClientTypeRepository getClientTypeRepository() {
        return clientTypeRepository;
    }

    public static RoomRepository getRoomRepository() {
        return roomRepository;
    }

    public static RentRepository getRentRepository() {
        return rentRepository;
    }

    public static RentRepository getRentCacheRepository() {
        return rentCacheRepository;
    }

}
