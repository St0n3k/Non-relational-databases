package pl.lodz.nbd.model.ClientTypes;


import org.bson.codecs.pojo.annotations.BsonDiscriminator;

@BsonDiscriminator(key = "type", value = "Bronze")
public class Bronze extends ClientType {
    public Bronze() {
        this.setDiscount(0.05);
    }
}
