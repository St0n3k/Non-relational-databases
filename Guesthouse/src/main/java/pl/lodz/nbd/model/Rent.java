package pl.lodz.nbd.model;


import lombok.Data;
import lombok.NoArgsConstructor;
import pl.lodz.nbd.common.MyValidator;

import java.time.LocalDateTime;
import java.util.UUID;


@Data
@NoArgsConstructor
public class Rent extends AbstractEntity {


    private Long id;


    private LocalDateTime beginTime;


    private LocalDateTime endTime;


    private boolean board;


    private double finalCost;


    private Client client;


    private Room room;

    public Rent(LocalDateTime beginTime, LocalDateTime endTime, boolean board, double finalCost, Client client, Room room) {
        if (beginTime.isAfter(endTime)) throw new RuntimeException("Wrong chronological order");
        this.setUuid(UUID.randomUUID());
        this.beginTime = beginTime;
        this.endTime = endTime;
        this.board = board;
        this.finalCost = finalCost;
        this.client = client;
        this.room = room;
        MyValidator.validate(this);
    }
}
