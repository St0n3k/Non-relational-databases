package pl.lodz.nbd.model.ClientTypes;

import lombok.Data;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonProperty;
import pl.lodz.nbd.model.AbstractEntity;


@Data
public abstract class ClientType extends AbstractEntity {
    @BsonProperty("discount")
    private double discount;

    @BsonProperty("type_name")
    private String typeName;


    public ClientType() {
        this.typeName = this.getClass().getSimpleName();
    }

    @BsonCreator
    public ClientType(@BsonProperty("type_name") String name, @BsonProperty("discount") double discount) {
        this.typeName = name;
        this.discount = discount;
    }

    public double applyDiscount(double price) {
        return price * (1.0 - discount);
    }

}
