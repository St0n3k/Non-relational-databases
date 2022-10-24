package pl.lodz.nbd.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import pl.lodz.nbd.common.MyValidator;
import pl.lodz.nbd.model.ClientTypes.ClientType;

import java.util.UUID;


@Data
@NoArgsConstructor
public class Client extends AbstractEntity {


    private Long id;


    private String firstName;


    private String lastName;


    private String personalId;
    

    private ClientType clientType;


    private Address address;

    public Client(String firstName, String lastName, String personalId, Address address, ClientType clientType) {
        this.setUuid(UUID.randomUUID());
        this.firstName = firstName;
        this.lastName = lastName;
        this.personalId = personalId;
        this.address = address;
        this.clientType = clientType;
        MyValidator.validate(this);
    }
}
