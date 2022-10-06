package pl.lodz.nbd.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
public class Rent {

    @Id
    @GeneratedValue
    @Column(name = "rent_id")
    private Long id;

    @Column(name = "begin_time")
    private Date beginTime;

    @Column(name = "end_time")
    private Date endTime;

    @Column
    private boolean board;

    @Column(name = "final_cost")
    private double finalCost;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;

    public Rent(Date beginTime, Date endTime, boolean board, double finalCost, Client client, Room room) {
        this.beginTime = beginTime;
        this.endTime = endTime;
        this.board = board;
        this.finalCost = finalCost;
        this.client = client;
        this.room = room;
    }
}
