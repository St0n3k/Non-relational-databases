package pl.lodz.nbd.table;

import com.datastax.oss.driver.api.mapper.annotations.ClusteringColumn;
import com.datastax.oss.driver.api.mapper.annotations.PartitionKey;

import java.time.LocalDate;

public class RentByClient {
    @ClusteringColumn
    private LocalDate beginTime;

    @ClusteringColumn(value = 1)
    private LocalDate endTime;

    private boolean board;
    private double finalCost;

    @PartitionKey
    private String clientPersonalId;

    private int roomNumber;

    public RentByClient(int roomNumber, LocalDate endTime, LocalDate beginTime, boolean board, double finalCost, String clientPersonalId) {
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
