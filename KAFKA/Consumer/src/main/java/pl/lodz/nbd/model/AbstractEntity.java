package pl.lodz.nbd.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.util.UUID;


@Data
@NoArgsConstructor
public abstract class AbstractEntity {

    @BsonProperty("_id")
    private UUID uuid;


    public AbstractEntity(UUID uuid) {
        this.uuid = uuid;
    }
}
