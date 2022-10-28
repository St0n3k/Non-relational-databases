package pl.lodz.nbd.model.ClientTypes;

import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonProperty;

@BsonDiscriminator(key = "name", value = "Default")
public class Default extends ClientType {
    public Default() {
        this.setDiscount(0.0);
    }

    @BsonCreator
    public Default(@BsonProperty("type_name") String name, @BsonProperty("discount") double discount) {
        super(name,discount);
    }


}
