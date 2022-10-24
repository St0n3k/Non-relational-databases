package pl.lodz.nbd.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import pl.lodz.nbd.model.ClientTypes.ClientType;


@Data
@NoArgsConstructor
public class Client extends AbstractEntity {

    private String firstName;


    private String lastName;


    private String personalId;
    

    private ClientType clientType;


    private Address address;

    public Client(String firstName, String lastName, String personalId, Address address, ClientType clientType) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.personalId = personalId;
        this.address = address;
        this.clientType = clientType;
    }
}
