package pl.lodz.nbd.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Room {

    @Id
    @GeneratedValue
    @Column(name = "room_id")
    private Long id;

    @Column(name = "room_number")
    private int roomNumber;

    @Column
    private double price;

    @Column(name = "is_rented")
    private boolean isRented;

    @Column
    private int size;

    public Room(int roomNumber, double price, boolean isRented, int size) {
        this.roomNumber = roomNumber;
        this.price = price;
        this.isRented = isRented;
        this.size = size;
    }
}
