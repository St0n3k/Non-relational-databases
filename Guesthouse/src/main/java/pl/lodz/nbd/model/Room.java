package pl.lodz.nbd.model;

import com.sun.istack.NotNull;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NamedQueries({
        @NamedQuery(name="Room.getAll",
                query="SELECT r FROM Room r")
})
@Data
@NoArgsConstructor
public class Room {

    @Id
    @GeneratedValue(generator = "roomId")
    @NotNull
    @Column(name = "room_id")
    private Long id;

    @NotNull
    @Column(name = "room_number", unique = true)
    private int roomNumber;

    @NotNull
    @Column
    private double price;

    @NotNull
    @Column
    private int size;

    public Room(int roomNumber, double price, int size) {
        this.roomNumber = roomNumber;
        this.price = price;
        this.size = size;
    }
}
