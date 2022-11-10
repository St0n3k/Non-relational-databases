package pl.lodz.nbd.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonProperty;
import pl.lodz.nbd.model.ClientTypes.ClientType;

import java.util.UUID;


@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class Client extends AbstractEntity {

    @BsonProperty("first_name")
    @JsonProperty("first_name")
    private String firstName;

    @BsonProperty("last_name")
    @JsonProperty("last_name")
    private String lastName;

    @BsonProperty("personal_id")
    @JsonProperty("personal_id")
    private String personalId;

    @BsonProperty(useDiscriminator = true)
    @JsonProperty
    private ClientType clientType;

    @BsonProperty("address")
    @JsonProperty("address")
    private Address address;

    @BsonCreator
    @JsonCreator
    public Client(@BsonProperty("_id") @JsonProperty("_id") UUID id,
                  @BsonProperty("first_name") @JsonProperty("first_name") String firstName,
                  @BsonProperty("last_name") @JsonProperty("last_name") String lastName,
                  @BsonProperty("personal_id") @JsonProperty("personal_id") String personalId,
                  @BsonProperty("address") @JsonProperty("address") Address address,
                  @BsonProperty("client_type") @JsonProperty("client_type") ClientType clientType) {
        super(id);
        this.firstName = firstName;
        this.lastName = lastName;
        this.personalId = personalId;
        this.address = address;
        this.clientType = clientType;
    }

    public Client(String firstName, String lastName, String personalId, Address address, ClientType clientType) {
        super(UUID.randomUUID());
        this.firstName = firstName;
        this.lastName = lastName;
        this.personalId = personalId;
        this.clientType = clientType;
        this.address = address;
    }
}
