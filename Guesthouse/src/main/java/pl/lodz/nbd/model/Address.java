package pl.lodz.nbd.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Address {

    @Id
    @GeneratedValue
    @Column(name = "address_id")
    private Long addressId;

    @Column
    private String city;

    @Column
    private String street;

    @Column
    private int number;

    public Address(String city, String street, int number) {
        this.city = city;
        this.street = street;
        this.number = number;
    }
}
