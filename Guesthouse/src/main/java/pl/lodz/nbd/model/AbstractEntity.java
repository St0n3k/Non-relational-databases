package pl.lodz.nbd.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;


@Data
@NoArgsConstructor
public abstract class AbstractEntity {

    private UUID uuid;


    public AbstractEntity(UUID uuid) {
        this.uuid = uuid;
    }
}
