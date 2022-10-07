package pl.lodz.nbd.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.lodz.nbd.common.MyValidator;

import java.time.LocalDateTime;

@Entity
@NamedQueries({
        @NamedQuery(name = "Rent.getAll",
                query = "SELECT r FROM Rent r"),
        @NamedQuery(name = "Rent.getByRoomNumber",
                query = "SELECT r FROM Rent r WHERE r.room.roomNumber = :roomNumber")
})
@Data
@NoArgsConstructor
public class Rent {

    @Id
    @GeneratedValue(generator = "rentId")
    @Column(name = "rent_id")
    private Long id;

    @NotNull
    @Column(name = "begin_time")
    private LocalDateTime beginTime;

    @NotNull
    @Column(name = "end_time")
    private LocalDateTime endTime;

    @NotNull
    @Column
    private boolean board;

    @NotNull
    @Column(name = "final_cost")
    private double finalCost;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;

    public Rent(LocalDateTime beginTime, LocalDateTime endTime, boolean board, double finalCost, Client client, Room room) {
        if (beginTime.isAfter(endTime)) throw new RuntimeException("Wrong chronological order");
        this.beginTime = beginTime;
        this.endTime = endTime;
        this.board = board;
        this.finalCost = finalCost;
        this.client = client;
        this.room = room;
        MyValidator.validate(this);
    }
}
