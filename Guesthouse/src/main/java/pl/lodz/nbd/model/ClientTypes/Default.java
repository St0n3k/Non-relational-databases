package pl.lodz.nbd.model.ClientTypes;

import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.Entity;
import pl.lodz.nbd.common.MyValidator;

@Entity
@Access(AccessType.FIELD)
public class Default extends ClientType {
    public Default() {
        this.setDiscount(0.0);
        MyValidator.validate(this);
    }
}
