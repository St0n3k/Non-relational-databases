package pl.lodz.nbd.model;

import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.util.UUID;

@MappedSuperclass
@Data
public abstract class AbstractEntity implements Serializable {

    @NotNull
    private UUID uuid;

//    @Version
//    private long version;

    //TODO read about version (used to optimistic barrier) and think if we need UUID(to replace primary keys?)
}
