package pl.lodz.nbd.model.ClientTypes;

import lombok.Data;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonProperty;
import pl.lodz.nbd.model.AbstractEntity;


@Data
public abstract class ClientType extends AbstractEntity {

    @BsonProperty("name")
    private String name;

    @BsonProperty("discount")
    private double discount;

    public ClientType() {
        this.name = this.getClass().getSimpleName();
    }

    @BsonCreator
    public ClientType(@BsonProperty("name") String name, @BsonProperty("discount") double discount) {
        this.name = name;
        this.discount = discount;
    }

    public double applyDiscount(double price) {
        return price * (1.0 - discount);
    }

}
