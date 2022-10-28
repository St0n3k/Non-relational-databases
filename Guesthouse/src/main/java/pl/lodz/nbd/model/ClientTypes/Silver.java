package pl.lodz.nbd.model.ClientTypes;


import org.bson.codecs.pojo.annotations.BsonDiscriminator;

@BsonDiscriminator(key = "type", value = "Silver")
public class Silver extends ClientType {
    public Silver() {
        this.setDiscount(0.1);
    }
}
