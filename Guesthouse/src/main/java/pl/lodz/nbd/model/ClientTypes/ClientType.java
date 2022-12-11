package pl.lodz.nbd.model.ClientTypes;

import lombok.Data;
import pl.lodz.nbd.model.AbstractEntity;

import java.util.UUID;


@Data
public abstract class ClientType extends AbstractEntity {

    private double discount;


    public ClientType(double discount) {
        super(UUID.randomUUID());
        this.discount = discount;
    }


    public ClientType(UUID id,
                      double discount) {
        super(id);
        this.discount = discount;
    }

    public double applyDiscount(double price) {
        return price * (1.0 - discount);
    }

}
