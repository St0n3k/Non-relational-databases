package pl.lodz.nbd.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import pl.lodz.nbd.common.MyValidator;


@Data
@NoArgsConstructor
public class Address {

    private String city;


    private String street;


    private int houseNumber;

    public Address(String city, String street, int houseNumber) {
        this.city = city;
        this.street = street;
        this.houseNumber = houseNumber;
        MyValidator.validate(this);
    }
}
