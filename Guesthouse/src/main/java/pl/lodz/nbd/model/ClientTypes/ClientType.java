package pl.lodz.nbd.model.ClientTypes;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public abstract class ClientType {


    private double discount;


    public ClientType(double discount) {
        this.discount = discount;
    }


    public double applyDiscount(double price) {
        return price * (1.0 - discount);
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public String getDiscriminator() {
        return "CLIENT_TYPE";
    }
}
