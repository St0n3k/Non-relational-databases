package pl.lodz.nbd.common;


import pl.lodz.nbd.repository.ClientRepository;
import pl.lodz.nbd.repository.ClientTypeRepository;
import pl.lodz.nbd.repository.RentRepository;
import pl.lodz.nbd.repository.RoomRepository;

public class RepositoryCreator {

    private static final ClientRepository clientRepository = new ClientRepository();
    private static final ClientTypeRepository clientTypeRepository = new ClientTypeRepository();
    private static final RoomRepository roomRepository = new RoomRepository();
    private static final RentRepository rentRepository = new RentRepository();

    public static ClientRepository getClientRepository() {
        return clientRepository;
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

}
