package pl.lodz.nbd.model;


import lombok.Data;
import lombok.NoArgsConstructor;
import pl.lodz.nbd.common.MyValidator;

import java.util.UUID;


@Data
@NoArgsConstructor
public class Room extends AbstractEntity {


    private Long id;


    private int roomNumber;


    private double price;


    private int size;

    public Room(int roomNumber, double price, int size) {
        this.setUuid(UUID.randomUUID());
        this.roomNumber = roomNumber;
        this.price = price;
        this.size = size;
        MyValidator.validate(this);
    }
}
