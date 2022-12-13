package pl.lodz.nbd.model;


import com.datastax.oss.driver.api.mapper.annotations.CqlName;
import com.datastax.oss.driver.api.mapper.annotations.Entity;
import com.datastax.oss.driver.api.mapper.annotations.PartitionKey;
import com.datastax.oss.driver.api.mapper.annotations.PropertyStrategy;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.ToString;


@Entity(defaultKeyspace = "guesthouse")
@CqlName("rooms")
@PropertyStrategy(mutable = false)
@Builder
@ToString
@EqualsAndHashCode
public class Room {

    @PartitionKey
    @CqlName("room_number")
    private int roomNumber;

    private double price;

    private int size;

    public Room(int roomNumber, double price, int size) {
        this.roomNumber = roomNumber;
        this.price = price;
        this.size = size;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(int roomNumber) {
        this.roomNumber = roomNumber;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
