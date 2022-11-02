package pl.lodz.nbd;

import org.junit.jupiter.api.Test;
import pl.lodz.nbd.manager.ClientManager;
import pl.lodz.nbd.manager.RentManager;
import pl.lodz.nbd.manager.RoomManager;
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
import java.util.Optional;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

public class RentTest {

    private static final RoomRepository roomRepository = new RoomRepository();
    private static final RentRepository rentRepository = new RentRepository();
    private static final ClientRepository clientRepository = new ClientRepository();
    private static final ClientTypeRepository clientTypeRepository = new ClientTypeRepository();
    private static final ClientManager clientManager = new ClientManager(clientRepository, clientTypeRepository);
    private static final RoomManager roomManager = new RoomManager(roomRepository);
    private static final RentManager rentManager = new RentManager(clientRepository, roomRepository, rentRepository);

    void initializeData() {
        clientManager.registerClient("Jerzy", "Dudek", "999777", "Wisła", "Karpacka", 22);
        clientManager.registerClient("Kamil", "Stoch", "999888", "Odra", "Wiślana", 32);
        clientManager.registerClient("Remigiusz", "Dudek", "999999", "Wrocław", "Łódzka", 44);

        roomManager.addRoom(120, 2, 2137);
        roomManager.addRoom(210, 3, 999);
        roomManager.addRoom(102, 1, 998);

        rentManager.rentRoom(LocalDateTime.now(), LocalDateTime.now().plusDays(2), true, "999999", 999);
        rentManager.rentRoom(LocalDateTime.now().plusDays(2), LocalDateTime.now().plusDays(4), true, "999777", 999);
        rentManager.rentRoom(LocalDateTime.now().plusDays(6), LocalDateTime.now().plusDays(10), true, "999888", 998);
        rentManager.rentRoom(LocalDateTime.now(), LocalDateTime.now().plusDays(4), true, "999888", 998);
        rentManager.rentRoom(LocalDateTime.now().plusDays(1300), LocalDateTime.now().plusDays(1400), true, "999777", 2137);
    }

    @Test
    void rentRoomTest() {
        Optional<Client> optClient = clientManager.registerClient("Marek", "Kowalski", "000566", "Warszawa", "Astronautów", 1);
        assertTrue(optClient.isPresent());
        Client client = optClient.get();
        Optional<Room> optionalRoom = roomManager.addRoom(100, 2, 400);
        assertTrue(optionalRoom.isPresent());
        Room room = optionalRoom.get();

        //Rent create success, check if it is persisted and total cost is calculated properly(add 50 to costPerDay, because of board option)
        Optional<Rent> optionalRent = rentManager.rentRoom(LocalDateTime.now().plusDays(17), LocalDateTime.now().plusDays(20), true, client.getPersonalId(), room.getRoomNumber());
        assertTrue(optionalRent.isPresent());
        Rent rentThreeDays = optionalRent.get();
        assertEquals(rentThreeDays.getFinalCost(), (100.0 + 50.0) * 3);

        //Rent create success, check if it persisted and days are rounded up(no board option)
        optionalRent = rentManager.rentRoom(LocalDateTime.now().plusDays(4), LocalDateTime.now().plusDays(4).plusHours(30), false, client.getPersonalId(), room.getRoomNumber());
        assertTrue(optionalRent.isPresent());
        assertEquals(optionalRent.get().getFinalCost(), 100.0 * 2);

        //Rent create fail, dates are colliding with other rent of the room
        optionalRent = rentManager.rentRoom(LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(5), true, client.getPersonalId(), room.getRoomNumber());
        assertTrue(optionalRent.isEmpty());

        //Rent create fail, client doesn't exist
        optionalRent = rentManager.rentRoom(LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(5), true, "111111", room.getRoomNumber());
        assertTrue(optionalRent.isEmpty());

        //Rent create fail, room doesn't exist
        optionalRent = rentManager.rentRoom(LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(5), true, client.getPersonalId(), 999);
        assertTrue(optionalRent.isEmpty());
    }


    @Test
    void optimisticLockTestSameDay() throws BrokenBarrierException, InterruptedException {

        clientManager.registerClient("Marek", "Kowalski", "065566", "Warszawa", "Astronautów", 1);
        roomManager.addRoom(100.0, 2, 405);
        Optional<Room> optionalRoom = roomManager.getByRoomNumber(405);
        assertTrue(optionalRoom.isPresent());
        Room room = optionalRoom.get();

        int threadNumber = 10;
        CyclicBarrier cyclicBarrier = new CyclicBarrier(threadNumber + 1);
        List<Thread> threads = new ArrayList<>(threadNumber);
        AtomicInteger numberFinished = new AtomicInteger();

        for (int i = 0; i < threadNumber; i++) {
            threads.add(new Thread(() -> {
                try {
                    cyclicBarrier.await();
                } catch (InterruptedException | BrokenBarrierException e) {
                    throw new RuntimeException(e);
                }
                rentManager.rentRoom(LocalDateTime.now().plusDays(40), LocalDateTime.now().plusDays(41), false, "065566", room.getRoomNumber());
                numberFinished.getAndIncrement();
            }));
        }

        threads.forEach(Thread::start);
        cyclicBarrier.await();
        while (numberFinished.get() != threadNumber) {
        }
        assertEquals(rentManager.getAllRentsOfRoom(room.getRoomNumber()).size(), 1);
    }

    @Test
    void optimisticLockTestOverlap() throws BrokenBarrierException, InterruptedException {

        Optional<Client> optionalClient = clientManager.registerClient("Marek", "Kowalski", "050566", "Warszawa", "Astronautów", 1);
        Optional<Room> optionalRoom = roomManager.addRoom(100.0, 2, 404);

        assertTrue(optionalClient.isPresent());
        assertTrue(optionalRoom.isPresent());

        Client client = optionalClient.get();
        Room room = optionalRoom.get();


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
    void clientTypeDiscountTest() {

        Optional<Room> optionalRoom = roomManager.addRoom(100.0, 2, 10);
        Optional<Client> optionalClient = clientManager.registerClient("Jarosław", "Jaki", "604566", "Wadowice", "Przybyszewskiego", 1);

        assertTrue(optionalClient.isPresent());
        assertTrue(optionalRoom.isPresent());

        Room room = optionalRoom.get();
        Client client = optionalClient.get();

        Optional<Rent> optionalRent = rentManager.rentRoom(LocalDateTime.now(), LocalDateTime.now().plusDays(1), false, client.getPersonalId(), room.getRoomNumber());

        Rent defaultRent = optionalRent.get();

        clientManager.changeTypeTo(client, Bronze.class);
        optionalClient = clientManager.getByPersonalId(client.getPersonalId());
        client = optionalClient.get();
        optionalRent = rentManager.rentRoom(LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2), false, client.getPersonalId(), room.getRoomNumber());

        Rent bronzeRent = optionalRent.get();
        clientManager.changeTypeTo(client, Silver.class);
        optionalRent = rentManager.rentRoom(LocalDateTime.now().plusDays(2), LocalDateTime.now().plusDays(3), false, client.getPersonalId(), room.getRoomNumber());

        Rent silverRent = optionalRent.get();

        clientManager.changeTypeTo(client, Gold.class);
        optionalRent = rentManager.rentRoom(LocalDateTime.now().plusDays(4), LocalDateTime.now().plusDays(5), false, client.getPersonalId(), room.getRoomNumber());

        Rent goldRent = optionalRent.get();

        assertEquals(defaultRent.getFinalCost(), 100);
        assertEquals(bronzeRent.getFinalCost(), 100 - (0.05 * 100));
        assertEquals(silverRent.getFinalCost(), 100 - (0.10 * 100));
        assertEquals(goldRent.getFinalCost(), 100 - (0.15 * 100));
    }

    @Test
    void getAllRentsOfRoomAndClientTest() {
        Optional<Room> optionalRoom = roomManager.addRoom(100.0, 2, 2428);
        Optional<Client> optionalClient = clientManager.registerClient("Jarek", "Taki", "604577", "Wadowice", "Przybyszewskiego", 1);

        assertTrue(optionalClient.isPresent());
        assertTrue(optionalRoom.isPresent());

        Room room = optionalRoom.get();
        Client client = optionalClient.get();

        Optional<Rent> optionalRent = rentManager.rentRoom(LocalDateTime.now(), LocalDateTime.now().plusDays(1), false, client.getPersonalId(), room.getRoomNumber());

        assertTrue(optionalRent.isPresent());

        Rent defaultRent = optionalRent.get();

        List<Rent> rents = rentManager.getAllRentsOfRoom(room.getRoomNumber());
        assertNotNull(rents);
        assertTrue(rents.size()>0);

        rents = rentManager.getAllRentsOfClient(client.getPersonalId());
        assertNotNull(rents);
        assertTrue(rents.size()>0);
    }

    @Test
    void removeRentTest(){
        Optional<Room> optionalRoom = roomManager.addRoom(100.0, 2, 2824);
        Optional<Client> optionalClient = clientManager.registerClient("Krzysiek", "Jemzupe", "644577", "Wadowice", "Przybyszewskiego", 1);

        assertTrue(optionalClient.isPresent());
        assertTrue(optionalRoom.isPresent());

        Room room = optionalRoom.get();
        Client client = optionalClient.get();

        Optional<Rent> optionalRent = rentManager.rentRoom(LocalDateTime.now(), LocalDateTime.now().plusDays(1), false, client.getPersonalId(), room.getRoomNumber());

        assertTrue(optionalRent.isPresent());

        Rent defaultRent = optionalRent.get();

        rentManager.removeRent(defaultRent);
        assertTrue(rentManager.getRentById(defaultRent.getUuid()).isEmpty());
    }

    @Test
    void updateRentTest() {
        clientManager.registerClient("Marek", "Kowalski", "140566", "Warszawa", "Astronautów", 1);
        roomManager.addRoom(100.0, 2, 1400);

        Optional<Client> optionalClient = clientManager.getByPersonalId("140566");
        Optional<Room> optionalRoom = roomManager.getByRoomNumber(1400);
        assertTrue(optionalClient.isPresent());
        assertTrue(optionalRoom.isPresent());
        Client client = optionalClient.get();
        Room room = optionalRoom.get();

        Optional<Rent> optionalRent = rentManager.rentRoom(LocalDateTime.now().plusDays(300), LocalDateTime.now().plusDays(320), false, client.getPersonalId(), room.getRoomNumber());
        assertTrue(optionalRent.isPresent());

        Rent rent = optionalRent.get();
        assertEquals(20 * 100, rent.getFinalCost());

        rent.setBoard(true);

        rentManager.update(rent);

        optionalRent = rentManager.getRentById(rent.getUuid());

        assertTrue(optionalRent.isPresent());
        rent = optionalRent.get();
        //Plus 50 per day, because of board option
        assertEquals(20 * (100 + 50), rent.getFinalCost());
    }
}
