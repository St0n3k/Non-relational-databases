package pl.lodz.nbd.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.lodz.nbd.common.MyValidator;

@Entity
@NamedQueries({
        @NamedQuery(name = "Address.getAll",
                query = "SELECT a FROM Address a")
})
@Data
@NoArgsConstructor
public class Address {

    @Id
    @GeneratedValue(generator = "addressId")
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
        MyValidator.validate(this);
    }
}
