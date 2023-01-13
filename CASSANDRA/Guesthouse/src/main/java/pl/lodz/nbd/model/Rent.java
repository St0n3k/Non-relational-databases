package pl.lodz.nbd.model;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Data
@NoArgsConstructor
@EqualsAndHashCode
public class Rent {

    private LocalDate beginTime;
    private LocalDate endTime;
    private boolean board;
    private double finalCost;
    private Client client;
    private Room room;

    public Rent(LocalDate beginTime, LocalDate endTime, boolean board, double finalCost, Client client, Room room) {
        if (beginTime.isAfter(endTime)) throw new RuntimeException("Wrong chronological order");
        this.beginTime = beginTime;
        this.endTime = endTime;
        this.board = board;
        this.finalCost = finalCost;
        this.client = client;
        this.room = room;
    }

}
