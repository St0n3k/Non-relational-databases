package pl.lodz.nbd.model.ClientTypes;


import com.datastax.oss.driver.api.mapper.annotations.CqlName;
import com.datastax.oss.driver.api.mapper.annotations.Entity;
import com.datastax.oss.driver.api.mapper.annotations.PartitionKey;
import com.datastax.oss.driver.api.mapper.annotations.PropertyStrategy;
import lombok.EqualsAndHashCode;

@Entity(defaultKeyspace = "guesthouse")
@CqlName("client_types")
@PropertyStrategy(mutable = false)
@EqualsAndHashCode(callSuper = true)
public class Default extends ClientType {

    @PartitionKey
    private String discriminator;

    public Default(String discriminator, double discount) {
        super(discount);
        this.discriminator = discriminator;
    }

    public Default() {
        super(0.00);
        this.discriminator = "Default";
    }

    @Override
    public String getDiscriminator() {
        return discriminator;
    }

    public void setDiscriminator(String discriminator) {
        this.discriminator = discriminator;
    }
}
