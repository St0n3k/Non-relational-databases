package pl.lodz.nbd.model;


import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.codecs.pojo.annotations.BsonProperty;


@Data
@NoArgsConstructor
public class Room extends AbstractEntity {

    @BsonProperty("number")
    private int roomNumber;

    @BsonProperty("price")
    private double price;

    @BsonProperty("size")
    private int size;

    public Room(@BsonProperty("number") int roomNumber,
                @BsonProperty("price") double price,
                @BsonProperty("size") int size) {
        this.roomNumber = roomNumber;
        this.price = price;
        this.size = size;
    }
}
