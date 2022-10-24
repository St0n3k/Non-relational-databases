package pl.lodz.nbd.model;


import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class Room extends AbstractEntity {

    private int roomNumber;


    private double price;


    private int size;

    public Room(int roomNumber, double price, int size) {
        this.roomNumber = roomNumber;
        this.price = price;
        this.size = size;
    }
}
