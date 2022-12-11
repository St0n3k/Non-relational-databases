package pl.lodz.nbd.manager;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class RentManager {

//    private ClientRepository clientRepository;
//    private RoomRepository roomRepository;
//    private RentRepository rentRepository;
//
//    public List<Rent> getAllRentsOfRoom(int roomNumber) {
//        return rentRepository.getByRoomNumber(roomNumber);
//    }
//
//    public Optional<Rent> getRentById(UUID id) {
//        return rentRepository.getById(id);
//    }
//
//    public List<Rent> getAllRentsOfClient(String personalId) {
//        return rentRepository.getByClientPersonalId(personalId);
//    }
//
//    public void removeRent(Rent rent) {
//        rentRepository.remove(rent);
//    }
//
//    public Optional<Rent> rentRoom(LocalDateTime beginTime, LocalDateTime endTime, boolean board, String clientPersonalId, int roomNumber) {
//
//        if (beginTime.isAfter(endTime)) return Optional.empty();
//
//        Optional<Client> client = clientRepository.getClientByPersonalId(clientPersonalId);
//        Optional<Room> room = roomRepository.getByRoomNumber(roomNumber);
//
//
//        if (client.isEmpty() || room.isEmpty()) return Optional.empty();
//
//        double finalCost = calculateTotalCost(beginTime, endTime, room.get().getPrice(), board, client.get().getClientType());
//        Rent rent = new Rent(beginTime, endTime, board, finalCost, client.get(), room.get());
//
//        return rentRepository.add(rent);
//    }
//
//
//    public boolean update(Rent rent) {
//        double newCost = calculateTotalCost(
//                rent.getBeginTime(),
//                rent.getEndTime(),
//                rent.getRoom().getPrice(),
//                rent.isBoard(),
//                rent.getClient().getClientType()
//        );
//        rent.setFinalCost(newCost);
//
//        return rentRepository.update(rent);
//    }
//
//    private double calculateTotalCost(LocalDateTime beginTime, LocalDateTime endTime, double costPerDay, boolean board, ClientType clientType) {
//        Duration duration = Duration.between(beginTime, endTime);
//        if (board) costPerDay += 50; //Daily board is worth 50
//        return clientType.applyDiscount(Math.ceil(duration.toHours() / 24.0) * costPerDay);
//    }

}
