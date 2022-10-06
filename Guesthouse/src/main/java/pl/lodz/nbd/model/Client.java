package pl.lodz.nbd.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Client {

    @Id
    @GeneratedValue
    @Column(name = "client_id")
    private Long clientId;

    @Column
    private String firstName;

    @Column
    private String lastName;

    @Column
    private String personalId;

    @Column
    private Boolean archived;

    @ManyToOne
    @JoinColumn
    private Address address;

    public Client(String firstName, String lastName, String personalId, Boolean archived) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.personalId = personalId;
        this.archived = archived;
    }
}
