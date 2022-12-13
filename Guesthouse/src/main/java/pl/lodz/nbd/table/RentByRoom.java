package pl.lodz.nbd.table;

import com.datastax.oss.driver.api.mapper.annotations.*;
import lombok.*;
import pl.lodz.nbd.model.Client;
import pl.lodz.nbd.model.Room;

import java.time.LocalDateTime;

@NoArgsConstructor
@Entity(defaultKeyspace = "guesthouse")
@CqlName("rents_by_room")
@PropertyStrategy(mutable = false)
@Builder
@ToString
@EqualsAndHashCode
public class RentByRoom {

    @ClusteringColumn
    private LocalDateTime beginTime;

    @ClusteringColumn(value = 1)
    private LocalDateTime endTime;

    private boolean board;
    private double finalCost;

    private String clientPersonalId;

    @PartitionKey
    private int roomNumber;

    public LocalDateTime getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(LocalDateTime beginTime) {
        this.beginTime = beginTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public boolean isBoard() {
        return board;
    }

    public void setBoard(boolean board) {
        this.board = board;
    }

    public double getFinalCost() {
        return finalCost;
    }

    public void setFinalCost(double finalCost) {
        this.finalCost = finalCost;
    }

    public String getClientPersonalId() {
        return clientPersonalId;
    }

    public void setClientPersonalId(String clientPersonalId) {
        this.clientPersonalId = clientPersonalId;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(int roomNumber) {
        this.roomNumber = roomNumber;
    }

    public RentByRoom(int roomNumber, LocalDateTime endTime, LocalDateTime beginTime, boolean board, double finalCost, String clientPersonalId) {
        if (beginTime.isAfter(endTime)) throw new RuntimeException("Wrong chronological order");
        this.beginTime = beginTime;
        this.endTime = endTime;
        this.board = board;
        this.finalCost = finalCost;
        this.clientPersonalId = clientPersonalId;
        this.roomNumber = roomNumber;
    }
}
