package pl.lodz.nbd.model.ClientTypes;

import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.Entity;
import pl.lodz.nbd.common.MyValidator;

@Entity
@Access(AccessType.FIELD)
public class Gold extends ClientType {
    public Gold() {
        this.setDiscount(0.15);
        MyValidator.validate(this);
    }
}
