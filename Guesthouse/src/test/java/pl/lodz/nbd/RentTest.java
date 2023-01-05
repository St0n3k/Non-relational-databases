package pl.lodz.nbd;

import org.junit.jupiter.api.Test;
import pl.lodz.nbd.common.RepositoryCreator;
import pl.lodz.nbd.manager.ClientManager;
import pl.lodz.nbd.manager.RentManager;
import pl.lodz.nbd.manager.RoomManager;
import pl.lodz.nbd.model.Client;
import pl.lodz.nbd.model.ClientTypes.Bronze;
import pl.lodz.nbd.model.ClientTypes.Default;
import pl.lodz.nbd.model.ClientTypes.Gold;
import pl.lodz.nbd.model.ClientTypes.Silver;
import pl.lodz.nbd.model.Rent;
import pl.lodz.nbd.model.Room;
import pl.lodz.nbd.repository.impl.ClientRepository;
import pl.lodz.nbd.repository.impl.ClientTypeRepository;
import pl.lodz.nbd.repository.impl.RentRepository;
import pl.lodz.nbd.repository.impl.RoomRepository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

public class RentTest {

    private static final RoomRepository roomRepository = RepositoryCreator.getRoomRepository();
    private static final RentRepository rentRepository = RepositoryCreator.getRentCacheRepository();
    private static final ClientRepository clientRepository = RepositoryCreator.getClientRepository();
    private static final ClientTypeRepository clientTypeRepository = RepositoryCreator.getClientTypeRepository();
    private static final ClientManager clientManager = new ClientManager(clientRepository, clientTypeRepository);
    private static final RoomManager roomManager = new RoomManager(roomRepository);
    private static final RentManager rentManager = new RentManager(clientRepository, roomRepository, rentRepository);


    @Test
    void rentRoomTest() {
        Optional<Client> optClient = clientManager.registerClient("Marek", "Kowalski", "000566", "Warszawa", "Astronautów", 1);
        assertTrue(optClient.isPresent());
        Client client = optClient.get();
        Optional<Room> optionalRoom = roomManager.addRoom(100, 2, 400);
        assertTrue(optionalRoom.isPresent());
        Room room = optionalRoom.get();

        LocalDateTime date = LocalDateTime.now();
        Optional<Rent> optionalRent = rentManager.rentRoom(date.plusSeconds(1), date.plusSeconds(2), true, client.getPersonalId(), room.getRoomNumber());
        date = LocalDateTime.now().plusDays(1);
        optionalRent = rentManager.rentRoom(date.plusSeconds(1), date.plusSeconds(2), false, client.getPersonalId(), room.getRoomNumber());

    }

}
