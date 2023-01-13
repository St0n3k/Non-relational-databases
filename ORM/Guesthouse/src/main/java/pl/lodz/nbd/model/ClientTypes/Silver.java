package pl.lodz.nbd.model.ClientTypes;

import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.Entity;
import pl.lodz.nbd.common.MyValidator;

import java.util.UUID;

@Entity
@Access(AccessType.FIELD)
public class Silver extends ClientType {
    public Silver() {
        this.setDiscount(0.1);
        this.setUuid(UUID.randomUUID());
        MyValidator.validate(this);
    }
}
