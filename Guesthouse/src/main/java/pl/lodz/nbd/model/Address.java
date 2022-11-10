package pl.lodz.nbd.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonProperty;


@Data
@NoArgsConstructor
public class Address {

    @BsonProperty("city")
    @JsonProperty("city")
    private String city;

    @BsonProperty("street")
    @JsonProperty("street")
    private String street;

    @BsonProperty("house_number")
    @JsonProperty("house_number")
    private int houseNumber;

    @BsonCreator
    @JsonCreator
    public Address(@BsonProperty("city") @JsonProperty("city") String city,
                   @BsonProperty("street") @JsonProperty("street") String street,
                   @BsonProperty("house_number") @JsonProperty("house_number") int houseNumber) {
        this.city = city;
        this.street = street;
        this.houseNumber = houseNumber;
    }
}
