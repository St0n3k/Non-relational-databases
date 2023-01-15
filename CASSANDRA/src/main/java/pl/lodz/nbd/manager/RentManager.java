package pl.lodz.nbd.manager;

import lombok.AllArgsConstructor;
import pl.lodz.nbd.model.Client;
import pl.lodz.nbd.model.ClientTypes.ClientType;
import pl.lodz.nbd.model.Rent;
import pl.lodz.nbd.model.Room;
import pl.lodz.nbd.repository.ClientRepository;
import pl.lodz.nbd.repository.RentRepository;
import pl.lodz.nbd.repository.RoomRepository;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public class RentManager {

    private ClientRepository clientRepository;
    private RoomRepository roomRepository;
    private RentRepository rentRepository;

    public List<Rent> getAllRentsOfRoom(int roomNumber) {
        return rentRepository.getByRoomNumber(roomNumber);
    }

    public List<Rent> getAllRentsOfClient(String personalId) {
        return rentRepository.getByClientPersonalId(personalId);
    }

    public void removeRent(Rent rent) {
        rentRepository.remove(rent);
    }

    public boolean rentRoom(LocalDate beginTime, LocalDate endTime, boolean board, String clientPersonalId, int roomNumber) {

        if (beginTime.isAfter(endTime)) return false;

        Optional<Client> client = clientRepository.getClientByPersonalId(clientPersonalId);
        Optional<Room> room = roomRepository.getByRoomNumber(roomNumber);


        if (client.isEmpty() || room.isEmpty()) return false;

        double finalCost = calculateTotalCost(beginTime, endTime, room.get().getPrice(), board, client.get().getClientType());
        Rent rent = new Rent(beginTime, endTime, board, finalCost, client.get(), room.get());

        return rentRepository.add(rent);
    }


    public void update(Rent rent) {
        double newCost = calculateTotalCost(
                rent.getBeginTime(),
                rent.getEndTime(),
                rent.getRoom().getPrice(),
                rent.isBoard(),
                rent.getClient().getClientType()
        );
        rent.setFinalCost(newCost);

        rentRepository.update(rent);
    }

    private double calculateTotalCost(LocalDate beginTime, LocalDate endTime, double costPerDay, boolean board, ClientType clientType) {
        Duration duration = Duration.between(beginTime, endTime);
        if (board) costPerDay += 50; //Daily board is worth 50
        return clientType.applyDiscount(Math.ceil(duration.toHours() / 24.0) * costPerDay);
    }

}
