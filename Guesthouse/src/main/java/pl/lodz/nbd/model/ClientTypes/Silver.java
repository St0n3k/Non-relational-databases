package pl.lodz.nbd.model.ClientTypes;

import pl.lodz.nbd.common.MyValidator;

import java.util.UUID;


public class Silver extends ClientType {
    public Silver() {
        this.setDiscount(0.1);
        this.setUuid(UUID.randomUUID());
        MyValidator.validate(this);
    }
}
