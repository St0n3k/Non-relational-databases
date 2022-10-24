package pl.lodz.nbd.model.ClientTypes;

import pl.lodz.nbd.common.MyValidator;

import java.util.UUID;


public class Default extends ClientType {
    public Default() {
        this.setDiscount(0.0);
        this.setUuid(UUID.randomUUID());
        MyValidator.validate(this);
    }
}
