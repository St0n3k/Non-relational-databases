package pl.lodz.nbd.model.ClientTypes;

import lombok.Data;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonProperty;
import pl.lodz.nbd.model.AbstractEntity;

import java.util.UUID;


@Data
@BsonDiscriminator(key = "_clazz", value = "pl.lodz.nbd.model.ClientTypes.ClientType")
public class ClientType extends AbstractEntity {
    @BsonProperty("discount")
    private double discount;


    public ClientType(double discount) {
        super(UUID.randomUUID());
        this.discount = discount;
    }


    @BsonCreator
    public ClientType(@BsonProperty("_id") UUID id,
                      @BsonProperty("discount") double discount) {
        super(id);
        this.discount = discount;
    }

    public double applyDiscount(double price) {
        return price * (1.0 - discount);
    }

}
