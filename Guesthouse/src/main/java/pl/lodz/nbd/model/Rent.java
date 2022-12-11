package pl.lodz.nbd.model;


import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;


@Data
@NoArgsConstructor
public class Rent extends AbstractEntity {

    private LocalDateTime beginTime;
    private LocalDateTime endTime;
    private boolean board;
    private double finalCost;
    private Client client;
    private Room room;


    public Rent(UUID uuid,
                LocalDateTime beginTime,
                LocalDateTime endTime,
                boolean board,
                double finalCost,
                Client client,
                Room room) {
        super(uuid);
        this.beginTime = beginTime;
        this.endTime = endTime;
        this.board = board;
        this.finalCost = finalCost;
        this.client = client;
        this.room = room;
    }

    public Rent(LocalDateTime beginTime, LocalDateTime endTime, boolean board, double finalCost, Client client, Room room) {
        super(UUID.randomUUID());
        if (beginTime.isAfter(endTime)) throw new RuntimeException("Wrong chronological order");
        this.beginTime = beginTime;
        this.endTime = endTime;
        this.board = board;
        this.finalCost = finalCost;
        this.client = client;
        this.room = room;
    }
}
