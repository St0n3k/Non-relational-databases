package pl.lodz.nbd.model;


import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.time.LocalDateTime;
import java.util.UUID;


@Data
@NoArgsConstructor
public class Rent extends AbstractEntity {

    @BsonProperty("begin_time")
    private LocalDateTime beginTime;


    @BsonProperty("end_time")
    private LocalDateTime endTime;


    @BsonProperty("board")
    private boolean board;


    @BsonProperty("final_cost")
    private double finalCost;


    @BsonProperty("client")
    private Client client;


    @BsonProperty("room")
    private Room room;

    public Rent(@BsonProperty("_id") UUID uuid,
                @BsonProperty("begin_time") LocalDateTime beginTime,
                @BsonProperty("end_time") LocalDateTime endTime,
                @BsonProperty("board") boolean board,
                @BsonProperty("final_cost") double finalCost,
                @BsonProperty("client") Client client,
                @BsonProperty("room") Room room) {
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
