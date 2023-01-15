package pl.lodz.nbd;

import org.junit.jupiter.api.Test;
import pl.lodz.nbd.common.RepositoryCreator;
import pl.lodz.nbd.model.Client;
import pl.lodz.nbd.model.ClientTypes.Bronze;
import pl.lodz.nbd.model.ClientTypes.Gold;
import pl.lodz.nbd.model.Rent;
import pl.lodz.nbd.model.Room;
import pl.lodz.nbd.repository.ClientRepository;
import pl.lodz.nbd.repository.RentRepository;
import pl.lodz.nbd.repository.RoomRepository;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class RentTest {


    private static final RoomRepository roomRepository = RepositoryCreator.getRoomRepository();
    private static final RentRepository rentRepository = RepositoryCreator.getRentRepository();
    private static final ClientRepository clientRepository = RepositoryCreator.getClientRepository();

    @Test
    void rentRoomTest() {
        Room room = new Room(144, 2, 3);
        Client client = new Client("12323", "Kuba", "Kowalski", new Gold());
        Rent rent = new Rent(LocalDate.now().plusDays(1), LocalDate.now().plusDays(3), true, 10000, client, room);

        roomRepository.addRoom(room);
        clientRepository.add(client);

        assertTrue(rentRepository.add(rent));

        List<Rent> rents = rentRepository.getByRoomNumber(144);
        assertEquals(1, rents.size());

        rentRepository.remove(rent);
        rents = rentRepository.getByRoomNumber(144);
        assertEquals(0, rents.size());

        rent = new Rent(LocalDate.now().plusDays(10), LocalDate.now().plusDays(13), true, 10000, client, room);
        assertTrue(rentRepository.add(rent));

        rent = new Rent(LocalDate.now().plusDays(9), LocalDate.now().plusDays(11), true, 10000, client, room);
        assertFalse(rentRepository.add(rent));

        rent = new Rent(LocalDate.now(), LocalDate.now().plusDays(12), true, 10000, client, room);
        assertFalse(rentRepository.add(rent));
    }


    @Test
    void getAllRentsOfRoomAndClientTest() {
        Room room = new Room(154, 12, 3);
        Client client = new Client("12356", "Kuba", "Kowalski", new Gold());
        Rent rent = new Rent(LocalDate.now().plusDays(1), LocalDate.now().plusDays(3), true, 200, client, room);
        Rent rent2 = new Rent(LocalDate.now().plusDays(3), LocalDate.now().plusDays(5), true, 200, client, room);

        roomRepository.addRoom(room);
        clientRepository.add(client);

        assertTrue(rentRepository.add(rent));
        assertTrue(rentRepository.add(rent2));

        List<Rent> rents = rentRepository.getByRoomNumber(room.getRoomNumber());
        assertEquals(2, rents.size());

        rents = rentRepository.getByClientPersonalId(client.getPersonalId());
        assertEquals(2, rents.size());
    }

    @Test
    void removeRentTest() {
        Room room = new Room(1477, 12, 8);
        Client client = new Client("123123", "Kuba", "Kowalski", new Bronze());
        Rent rent = new Rent(LocalDate.now().plusDays(1), LocalDate.now().plusDays(3), true, 10000, client, room);
        Rent rent2 = new Rent(LocalDate.now().plusDays(2), LocalDate.now().plusDays(3), true, 10000, client, room);

        roomRepository.addRoom(room);
        clientRepository.add(client);

        assertTrue(rentRepository.add(rent));
        assertFalse(rentRepository.add(rent2));

        rentRepository.remove(rent);
        assertTrue(rentRepository.add(rent2));

        rentRepository.remove(rent2);
        assertEquals(0, rentRepository.getByRoomNumber(1477).size());
    }

    @Test
    void updateRentTest() {
        Room room = new Room(71, 11, 5);
        Client client = new Client("2136", "Antoinne", "Griezmann", new Gold());
        Rent rent = new Rent(LocalDate.now().plusDays(1), LocalDate.now().plusDays(3), true, 1000, client, room);

        roomRepository.addRoom(room);
        clientRepository.add(client);

        assertTrue(rentRepository.add(rent));

        Rent repoRent = rentRepository.getByRoomNumber(71).get(0);
        assertEquals(rent, repoRent);
        assertTrue(repoRent.isBoard());
        assertEquals(1000, repoRent.getFinalCost());

        rent.setBoard(false);
        rent.setFinalCost(999);
        rentRepository.update(rent);

        repoRent = rentRepository.getByRoomNumber(71).get(0);
        assertEquals(rent, repoRent);
        assertFalse(repoRent.isBoard());
        assertEquals(999, repoRent.getFinalCost());
    }
}
