package pl.lodz.nbd.manager;

import pl.lodz.nbd.model.Client;
import pl.lodz.nbd.model.Rent;
import pl.lodz.nbd.model.Room;
import pl.lodz.nbd.repository.ClientRepository;
import pl.lodz.nbd.repository.RentRepository;
import pl.lodz.nbd.repository.RoomRepository;

import java.util.Date;

public class RentManager {

    private ClientRepository clientRepository;
    private RoomRepository roomRepository;
    private RentRepository rentRepository;

    public Rent rentRoom(Date beginTime, Date endTime, boolean board, String clientPersonalId, int roomNumber) {
        Client client = null;
        Room room = null;
        //TODO get from repository

        //TODO calculate cost
        double finalCost = 1000.0;

        //TODO check if dates are not colliding with actual rents of room
        Rent rent = new Rent(beginTime, endTime, board, finalCost, client, room);


        return rent;


    }

}
