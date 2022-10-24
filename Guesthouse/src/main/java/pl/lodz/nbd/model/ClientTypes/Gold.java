package pl.lodz.nbd.model.ClientTypes;

import pl.lodz.nbd.common.MyValidator;

import java.util.UUID;


public class Gold extends ClientType {
    public Gold() {
        this.setDiscount(0.15);
        this.setUuid(UUID.randomUUID());
        MyValidator.validate(this);
    }
}
