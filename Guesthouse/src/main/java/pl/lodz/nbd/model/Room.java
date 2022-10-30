package pl.lodz.nbd.model;


import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.util.UUID;


@Data
@NoArgsConstructor
public class Room extends AbstractEntity {

    @BsonProperty("number")
    private int roomNumber;

    @BsonProperty("price")
    private double price;

    @BsonProperty("size")
    private int size;

    public Room(@BsonProperty("_id") UUID id,
                @BsonProperty("number") int roomNumber,
                @BsonProperty("price") double price,
                @BsonProperty("size") int size) {
        super(id);
        this.roomNumber = roomNumber;
        this.price = price;
        this.size = size;
    }

    public Room(int roomNumber, double price, int size) {
        super(UUID.randomUUID());
        this.roomNumber = roomNumber;
        this.price = price;
        this.size = size;
    }
}
