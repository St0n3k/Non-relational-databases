package pl.lodz.nbd.table;

import com.datastax.oss.driver.api.mapper.annotations.*;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;

@NoArgsConstructor
@Entity(defaultKeyspace = "guesthouse")
@CqlName("rents_by_room")
@PropertyStrategy(mutable = false)
@ToString
@EqualsAndHashCode
public class RentByRoom {

    @ClusteringColumn
    private LocalDate beginTime;

    @ClusteringColumn(value = 1)
    private LocalDate endTime;

    private boolean board;
    private double finalCost;

    private String clientPersonalId;

    @PartitionKey
    private int roomNumber;

    public RentByRoom(int roomNumber, LocalDate endTime, LocalDate beginTime, boolean board, double finalCost, String clientPersonalId) {
        if (beginTime.isAfter(endTime)) throw new RuntimeException("Wrong chronological order");
        this.beginTime = beginTime;
        this.endTime = endTime;
        this.board = board;
        this.finalCost = finalCost;
        this.clientPersonalId = clientPersonalId;
        this.roomNumber = roomNumber;
    }

    public LocalDate getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(LocalDate beginTime) {
        this.beginTime = beginTime;
    }

    public LocalDate getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDate endTime) {
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
}
