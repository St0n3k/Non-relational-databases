package pl.lodz.nbd.model.ClientTypes;


import org.bson.codecs.pojo.annotations.BsonDiscriminator;

@BsonDiscriminator(key = "_clazz", value = "pl.lodz.nbd.model.ClientTypes.Silver")
public class Silver extends ClientType {

    public Silver() {
        super(0.10);
    }
}
