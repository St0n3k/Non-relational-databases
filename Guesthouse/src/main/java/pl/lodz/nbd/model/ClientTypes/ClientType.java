package pl.lodz.nbd.model.ClientTypes;

import com.datastax.oss.driver.api.mapper.annotations.PartitionKey;
import lombok.Data;


@Data
public abstract class ClientType {

    @PartitionKey
    private double discount;


    public ClientType(double discount) {
        this.discount = discount;
    }


    public double applyDiscount(double price) {
        return price * (1.0 - discount);
    }

}
