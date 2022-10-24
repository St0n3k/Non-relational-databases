package pl.lodz.nbd.model.ClientTypes;

import lombok.Data;
import pl.lodz.nbd.model.AbstractEntity;


@Data
public abstract class ClientType extends AbstractEntity {


    private Long id;


    private String name;


    private double discount;

    public ClientType() {
        this.name = this.getClass().getSimpleName();
    }

    public double applyDiscount(double price) {
        return price * (1.0 - discount);
    }

}
