package pl.lodz.nbd.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Client {

    @Id
    @GeneratedValue
    @Column(name = "client_id")
    private Long clientId;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "personal_id", unique = true)
    private String personalId;

    @Column
    private boolean archived;

    @ManyToOne
    @JoinColumn(name = "address_id")
    private Address address;

    public Client(String firstName, String lastName, String personalId, boolean archived, Address address) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.personalId = personalId;
        this.archived = archived;
        this.address = address;
    }
}
