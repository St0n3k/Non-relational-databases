package pl.lodz.nbd.model;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonProperty("number")
    private int roomNumber;

    @BsonProperty("price")
    @JsonProperty("price")
    private double price;

    @BsonProperty("size")
    @JsonProperty("size")
    private int size;

    @BsonProperty("is_being_rented")
    @JsonProperty("is_being_rented")
    private int isBeingRented = 0;

    @JsonCreator
    public Room(@BsonProperty("_id") @JsonProperty("_id") UUID id,
                @BsonProperty("number") @JsonProperty("number") int roomNumber,
                @BsonProperty("price") @JsonProperty("price") double price,
                @BsonProperty("size") @JsonProperty("size") int size,
                @BsonProperty("is_being_rented") @JsonProperty("is_being_rented") int isBeingRented) {
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
