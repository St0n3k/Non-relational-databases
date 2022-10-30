package pl.lodz.nbd.model.ClientTypes;


import org.bson.codecs.pojo.annotations.BsonDiscriminator;

@BsonDiscriminator(key = "_clazz", value = "pl.lodz.nbd.model.ClientTypes.Gold")
public class Gold extends ClientType {


    public Gold() {
        super(0.15);
    }
}
