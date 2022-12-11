package pl.lodz.nbd.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import pl.lodz.nbd.model.ClientTypes.ClientType;

import java.util.UUID;


@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class Client extends AbstractEntity {

    private String firstName;
    private String lastName;
    private String personalId;
    private ClientType clientType;
    private Address address;


    public Client(UUID id,
                  String firstName,
                  String lastName,
                  String personalId,
                  Address address,
                  ClientType clientType) {
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
