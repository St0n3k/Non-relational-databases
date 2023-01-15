package pl.lodz.nbd.model.ClientTypes;


import org.bson.codecs.pojo.annotations.BsonDiscriminator;

@BsonDiscriminator(key = "_clazz", value = "pl.lodz.nbd.model.ClientTypes.Bronze")
public class Bronze extends ClientType {

    public Bronze() {
        super(0.05);
    }
}
