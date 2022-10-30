package pl.lodz.nbd.model.ClientTypes;

import org.bson.codecs.pojo.annotations.BsonDiscriminator;

@BsonDiscriminator(key = "_clazz", value = "pl.lodz.nbd.model.ClientTypes.Default")
public class Default extends ClientType {

    public Default() {
        super(0.0);
    }
}
