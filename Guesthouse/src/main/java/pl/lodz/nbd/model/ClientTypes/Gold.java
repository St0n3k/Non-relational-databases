package pl.lodz.nbd.model.ClientTypes;


import org.bson.codecs.pojo.annotations.BsonDiscriminator;

@BsonDiscriminator(key = "type", value = "Gold")
public class Gold extends ClientType {
    public Gold() {
        this.setDiscount(0.15);
    }
}
