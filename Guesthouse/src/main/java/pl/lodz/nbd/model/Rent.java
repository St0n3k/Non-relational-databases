package pl.lodz.nbd.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

import pl.lodz.nbd.model.Room;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Rent {
    @Id
    @GeneratedValue
    @Column(name = "rent_id")
    private Long rentId;

    @Column
    private Date beginTime;

    @Column
    private Date endTime;

    @Column
    private Boolean board;

    @Column
    private Double finalCost;

    @ManyToOne
    @JoinColumn
    private Client client;

    @JoinColumn
    @ManyToOne
    private Room room;
}
