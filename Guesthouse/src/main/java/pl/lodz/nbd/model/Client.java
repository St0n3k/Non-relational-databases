package pl.lodz.nbd.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonProperty;
import pl.lodz.nbd.model.ClientTypes.ClientType;


@Data
@NoArgsConstructor
public class Client extends AbstractEntity {

    @BsonProperty("first_name")
    private String firstName;

    @BsonProperty("last_name")
    private String lastName;

    @BsonProperty("personal_id")
    private String personalId;

    @BsonProperty("client_type")
    private ClientType clientType;

    @BsonProperty("address")
    private Address address;

    @BsonCreator
    public Client(@BsonProperty("first_name") String firstName,
                  @BsonProperty("last_name") String lastName,
                  @BsonProperty("personal_id") String personalId,
                  @BsonProperty("address") Address address,
                  @BsonProperty("client_type") ClientType clientType) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.personalId = personalId;
        this.address = address;
        this.clientType = clientType;
    }
}
