package pl.lodz.nbd;

import jakarta.validation.ValidationException;
import org.junit.jupiter.api.Test;
import pl.lodz.nbd.manager.ClientManager;
import pl.lodz.nbd.manager.RentManager;
import pl.lodz.nbd.manager.RoomManager;
import pl.lodz.nbd.model.Address;
import pl.lodz.nbd.model.Client;
import pl.lodz.nbd.model.ClientTypes.Bronze;
import pl.lodz.nbd.model.ClientTypes.Gold;
import pl.lodz.nbd.model.ClientTypes.Silver;
import pl.lodz.nbd.model.Rent;
import pl.lodz.nbd.model.Room;
import pl.lodz.nbd.repository.impl.ClientRepository;
import pl.lodz.nbd.repository.impl.ClientTypeRepository;
import pl.lodz.nbd.repository.impl.RentRepository;
import pl.lodz.nbd.repository.impl.RoomRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

public class TestClass {

    //TODO divide test into specific test classes

    private static final RoomRepository roomRepository = new RoomRepository();
    private static final ClientRepository clientRepository = new ClientRepository();
    private static final RentRepository rentRepository = new RentRepository();
    private static final ClientTypeRepository clientTypeRepository = new ClientTypeRepository();


    @Test
    void registerAndUpdateClientTest() {
        ClientManager clientManager = new ClientManager(clientRepository, clientTypeRepository);

        //Check if clients are persisted
        assertNotNull(clientManager.registerClient("Jakub", "Konieczny", "000333", "Warszawa", "Gorna", 16));
        assertNotNull(clientManager.registerClient("Anna", "Matejko", "000222", "Łódź", "Wesoła", 32));

        //Check if client is not persisted (existing personalId)
        assertNull(clientManager.registerClient("Mateusz", "Polak", "000222", "Kraków", "Słoneczna", 133));

        //Check if getByPersonalId works properly
        assertNotNull(clientManager.getByPersonalId("000333"));
        assertNotNull(clientManager.getByPersonalId("000222"));
        assertNull(clientManager.getByPersonalId("000444"));
    }

    @Test
    void updateClientTest() {
        ClientManager clientManager = new ClientManager(clientRepository, clientTypeRepository);

        clientManager.registerClient("Jan", "Matejko", "000211", "Łódź", "Wesoła", 32);
        Client client = clientManager.getByPersonalId("000211");
        assertEquals(client.getVersion(), 0);

        client.setFirstName("Marcin");
        client = clientManager.updateClient(client);

        //Check if version field incremented
        assertEquals(client.getVersion(), 1);
        assertEquals(client.getFirstName(), "Marcin");
    }

    @Test
    void addRoomTest() {
        RoomManager roomManager = new RoomManager(roomRepository);

        //Check if rooms are persisted
        assertNotNull(roomManager.addRoom(100.0, 2, 100));
        assertNotNull(roomManager.addRoom(200.0, 3, 101));

        //Check if room is not persisted(existing room number)
        assertNull(roomManager.addRoom(1000.0, 5, 100));

        //Check if getRoomByNumber works properly
        assertNotNull(roomManager.getByRoomNumber(100));
        assertNotNull(roomManager.getByRoomNumber(101));
        assertNull(roomManager.getByRoomNumber(300));
    }

    @Test
    void rentRoomTest() {
        RoomManager roomManager = new RoomManager(roomRepository);
        RentManager rentManager = new RentManager(clientRepository, roomRepository, rentRepository);
        ClientManager clientManager = new ClientManager(clientRepository, clientTypeRepository);

        Client client = clientManager.registerClient("Marek", "Kowalski", "000566", "Warszawa", "Astronautów", 1);
        Room room = roomManager.addRoom(100.0, 2, 400);

        //Rent create success, check if it is persisted and total cost is calculated properly(add 50 to costPerDay, because of board option)
        Rent rentThreeDays = rentManager.rentRoom(LocalDateTime.now(), LocalDateTime.now().plusDays(3), true, client.getPersonalId(), room.getRoomNumber());
        assertNotNull(rentThreeDays);
        assertEquals(rentThreeDays.getFinalCost(), (100.0 + 50.0) * 3);

        //Rent create success, check if it persisted and days are rounded up(no board option)
        Rent rentThirtyHours = rentManager.rentRoom(LocalDateTime.now().plusDays(4), LocalDateTime.now().plusDays(4).plusHours(30), false, client.getPersonalId(), room.getRoomNumber());
        assertNotNull(rentThirtyHours);
        assertEquals(rentThirtyHours.getFinalCost(), 100.0 * 2);

        //Rent create fail, dates are colliding with other rent of the room
        assertNull(rentManager.rentRoom(LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(5), true, client.getPersonalId(), room.getRoomNumber()));

        //Rent create fail, client doesn't exist
        assertNull(rentManager.rentRoom(LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(5), true, "111111", room.getRoomNumber()));

        //Rent create fail, room doesn't exist
        assertNull(rentManager.rentRoom(LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(5), true, client.getPersonalId(), 999));
    }


    @Test
    void optimisticLockTestSameDay() throws BrokenBarrierException, InterruptedException {
        RoomManager roomManager = new RoomManager(roomRepository);
        RentManager rentManager = new RentManager(clientRepository, roomRepository, rentRepository);
        ClientManager clientManager = new ClientManager(clientRepository, clientTypeRepository);

        Client client = clientManager.registerClient("Marek", "Kowalski", "055566", "Warszawa", "Astronautów", 1);
        Room room = roomManager.addRoom(100.0, 2, 405);

        CyclicBarrier cyclicBarrier = new CyclicBarrier(11);
        List<Thread> threads = new ArrayList<>(10);
        AtomicInteger numberFinished = new AtomicInteger();

        for (int i = 0; i < 10; i++) {
            threads.add(new Thread(() -> {
                try {
                    cyclicBarrier.await();
                } catch (InterruptedException | BrokenBarrierException e) {
                    throw new RuntimeException(e);
                }
                rentManager.rentRoom(LocalDateTime.now().plusDays(40), LocalDateTime.now().plusDays(41), false, client.getPersonalId(), room.getRoomNumber());
                numberFinished.getAndIncrement();
            }));
        }

        threads.forEach(Thread::start);
        cyclicBarrier.await();
        while (numberFinished.get() != 10) {
        }
        assertEquals(rentManager.getAllRentsOfRoom(room.getRoomNumber()).size(), 1);
    }

    @Test
    void optimisticLockTestOverlap() throws BrokenBarrierException, InterruptedException {
        RoomManager roomManager = new RoomManager(roomRepository);
        RentManager rentManager = new RentManager(clientRepository, roomRepository, rentRepository);
        ClientManager clientManager = new ClientManager(clientRepository, clientTypeRepository);

        Client client = clientManager.registerClient("Marek", "Kowalski", "050566", "Warszawa", "Astronautów", 1);
        Room room = roomManager.addRoom(100.0, 2, 404);

        int threadNumber = 4;
        CyclicBarrier cyclicBarrier = new CyclicBarrier(threadNumber + 1);
        List<Thread> threads = new ArrayList<>(threadNumber);
        AtomicInteger numberFinished = new AtomicInteger();

        LocalDateTime localDateTime = LocalDateTime.now();
        for (int i = 0; i < threadNumber; i++) {
            int finalI = i;
            threads.add(new Thread(() -> {
                try {
                    cyclicBarrier.await();
                } catch (InterruptedException | BrokenBarrierException e) {
                    throw new RuntimeException(e);
                }
                rentManager.rentRoom(localDateTime.plusDays(100 + finalI), localDateTime.plusDays(100 + finalI + 2).minusHours(1), false, client.getPersonalId(), room.getRoomNumber());
                numberFinished.getAndIncrement();
            }));
        }

        threads.forEach(Thread::start);
        cyclicBarrier.await();
        while (numberFinished.get() != threadNumber) {
        }
        assertEquals(rentManager.getAllRentsOfRoom(room.getRoomNumber()).size(), 2);
    }


    @Test
    void validatorTest() {
        //Null city
        assertThrows(ValidationException.class, () -> new Address(null, "Gorna", 11));

        //Null address and client type
        assertThrows(ValidationException.class, () -> new Client("Rafał", "Strzałkowski", "0003334", null, null));

        //Null client and room
        assertThrows(ValidationException.class, () -> new Rent(LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(5), true, 1000.0, null, null));
    }

    @Test
    void clientTypeDiscountTest() {
        RoomManager roomManager = new RoomManager(roomRepository);
        RentManager rentManager = new RentManager(clientRepository, roomRepository, rentRepository);
        ClientManager clientManager = new ClientManager(clientRepository, clientTypeRepository);

        Room room = roomManager.addRoom(100.0, 2, 10);
        Client client = clientManager.registerClient("Jarosław", "Jaki", "004566", "Wadowice", "Przybyszewskiego", 1);

        Rent defaultRent = rentManager.rentRoom(LocalDateTime.now(), LocalDateTime.now().plusDays(1), false, client.getPersonalId(), room.getRoomNumber());

        client = clientManager.changeTypeTo(Bronze.class, client);
        Rent bronzeRent = rentManager.rentRoom(LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2), false, client.getPersonalId(), room.getRoomNumber());

        client = clientManager.changeTypeTo(Silver.class, client);
        Rent silverRent = rentManager.rentRoom(LocalDateTime.now().plusDays(2), LocalDateTime.now().plusDays(3), false, client.getPersonalId(), room.getRoomNumber());

        client = clientManager.changeTypeTo(Gold.class, client);
        Rent goldRent = rentManager.rentRoom(LocalDateTime.now().plusDays(4), LocalDateTime.now().plusDays(5), false, client.getPersonalId(), room.getRoomNumber());

        assertEquals(defaultRent.getFinalCost(), 100);
        assertEquals(bronzeRent.getFinalCost(), 100 - (0.05 * 100));
        assertEquals(silverRent.getFinalCost(), 100 - (0.10 * 100));
        assertEquals(goldRent.getFinalCost(), 100 - (0.15 * 100));
    }
}
