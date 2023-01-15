package pl.lodz.nbd.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import pl.lodz.nbd.model.ClientTypes.ClientType;
import pl.lodz.nbd.table.ClientEntity;


@Builder
@ToString
@EqualsAndHashCode
public class Client {

    private String firstName;

    private String lastName;

    private String personalId;

    private ClientType clientType;


    public Client(String personalId,
                  String firstName,
                  String lastName,
                  ClientType clientType) {
        this.personalId = personalId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.clientType = clientType;
    }

    public Client(String personalId,
                  String firstName,
                  String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.personalId = personalId;
    }

    public ClientEntity toClientEntity() {
        return new ClientEntity(personalId, firstName, lastName, clientType.getDiscriminator());
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPersonalId() {
        return personalId;
    }

    public void setPersonalId(String personalId) {
        this.personalId = personalId;
    }

    public ClientType getClientType() {
        return clientType;
    }

    public void setClientType(ClientType clientType) {
        this.clientType = clientType;
    }
}
