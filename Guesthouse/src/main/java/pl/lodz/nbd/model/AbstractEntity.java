package pl.lodz.nbd.model;

import lombok.Data;

import java.io.Serializable;
import java.util.UUID;


@Data
public abstract class AbstractEntity implements Serializable {


    private UUID uuid;


    private long version;
}
