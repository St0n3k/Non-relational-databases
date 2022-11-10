package pl.lodz.nbd.model;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.codecs.pojo.annotations.BsonProperty;
import pl.lodz.nbd.common.LocalDateTimeDeserializer;
import pl.lodz.nbd.common.LocalDateTimeSerializer;

import java.time.LocalDateTime;
import java.util.UUID;


@Data
@NoArgsConstructor
public class Rent extends AbstractEntity {

    @BsonProperty("begin_time")
    @JsonProperty("begin_time")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime beginTime;


    @BsonProperty("end_time")
    @JsonProperty("end_time")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime endTime;


    @BsonProperty("board")
    @JsonProperty("board")
    private boolean board;


    @BsonProperty("final_cost")
    @JsonProperty("final_cost")
    private double finalCost;


    @BsonProperty("client")
    @JsonProperty("client")
    private Client client;


    @BsonProperty("room")
    @JsonProperty("room")
    private Room room;


    @JsonCreator
    public Rent(@BsonProperty("_id") @JsonProperty("_id") UUID uuid,
                @BsonProperty("begin_time") @JsonProperty("begin_time") LocalDateTime beginTime,
                @BsonProperty("end_time") @JsonProperty("end_time") LocalDateTime endTime,
                @BsonProperty("board") @JsonProperty("board") boolean board,
                @BsonProperty("final_cost") @JsonProperty("final_cost") double finalCost,
                @BsonProperty("client") @JsonProperty("client") Client client,
                @BsonProperty("room") @JsonProperty("room") Room room) {
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
