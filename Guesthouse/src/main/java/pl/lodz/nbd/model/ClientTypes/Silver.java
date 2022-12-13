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
public class Silver extends ClientType {

    @PartitionKey
    private String discriminator;

    public Silver(String discriminator, double discount) {
        super(discount);
        this.discriminator = discriminator;
    }

    public Silver() {
        super(0.10);
        this.discriminator = "Silver";
    }

    @Override
    public String getDiscriminator() {
        return discriminator;
    }

    public void setDiscriminator(String discriminator) {
        this.discriminator = discriminator;
    }
}
