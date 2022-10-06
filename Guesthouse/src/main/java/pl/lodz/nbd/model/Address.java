package pl.lodz.nbd.model;

import com.sun.istack.NotNull;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NamedQueries({
        @NamedQuery(name="Address.getAll",
                query="SELECT a FROM Address a")
})
@Data
@NoArgsConstructor
public class Address {

    @Id
    @GeneratedValue(generator = "addressId")
    @NotNull
    @Column(name = "address_id")
    private Long id;

    @NotNull
    @Column
    private String city;

    @NotNull
    @Column
    private String street;

    @NotNull
    @Column
    private int number;

    public Address(String city, String street, int number) {
        this.city = city;
        this.street = street;
        this.number = number;
    }
}
