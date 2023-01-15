package pl.lodz.nbd.model;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.util.UUID;


@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class Room extends AbstractEntity {


    @BsonProperty("number")
    private int roomNumber;

    @BsonProperty("price")
    private double price;

    @BsonProperty("size")
    private int size;

    @BsonProperty("is_being_rented")
    private int isBeingRented = 0;

    public Room(@BsonProperty("_id") UUID id,
                @BsonProperty("number") int roomNumber,
                @BsonProperty("price") double price,
                @BsonProperty("size") int size,
                @BsonProperty("is_being_rented") int isBeingRented) {
        super(id);
        this.roomNumber = roomNumber;
        this.price = price;
        this.size = size;
        this.isBeingRented = isBeingRented;
    }

    public Room(int roomNumber, double price, int size) {
        super(UUID.randomUUID());
        this.roomNumber = roomNumber;
        this.price = price;
        this.size = size;
    }
}
