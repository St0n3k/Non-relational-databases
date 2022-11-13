package pl.lodz.nbd.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.codecs.pojo.annotations.BsonProperty;


@Data
@NoArgsConstructor
public class Address {

    @BsonProperty("city")
    private String city;

    @BsonProperty("street")
    private String street;

    @BsonProperty("house_number")
    private int houseNumber;


    public Address(@BsonProperty("city") String city,
                   @BsonProperty("street") String street,
                   @BsonProperty("house_number") int houseNumber) {
        this.city = city;
        this.street = street;
        this.houseNumber = houseNumber;
    }
}
