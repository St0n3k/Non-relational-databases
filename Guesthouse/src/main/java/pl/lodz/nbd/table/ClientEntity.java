package pl.lodz.nbd.table;

import com.datastax.oss.driver.api.mapper.annotations.CqlName;
import com.datastax.oss.driver.api.mapper.annotations.Entity;
import com.datastax.oss.driver.api.mapper.annotations.PartitionKey;
import com.datastax.oss.driver.api.mapper.annotations.PropertyStrategy;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import pl.lodz.nbd.model.Client;
import pl.lodz.nbd.model.ClientTypes.*;

@Entity(defaultKeyspace = "guesthouse")
@CqlName("clients")
@PropertyStrategy(mutable = false)
@Builder
@ToString
@EqualsAndHashCode
public class ClientEntity {

    @CqlName("first_name")
    private String firstName;

    @CqlName("last_name")
    private String lastName;

    @PartitionKey
    @CqlName("personal_id")
    private String personalId;


    @CqlName("client_type")
    private String clientType;


    public ClientEntity(String personalId,
                        String firstName,
                        String lastName,
                        String clientType) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.personalId = personalId;
        this.clientType = clientType;
    }

    public Client toClient() {
        ClientType ct;
        switch (clientType) {
            case "Bronze" -> ct = new Bronze();
            case "Silver" -> ct = new Silver();
            case "Gold" -> ct = new Gold();
            default -> ct = new Default();
        }
        return new Client(personalId, firstName, lastName, ct);
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

    public String getClientType() {
        return clientType;
    }

    public void setClientType(String clientType) {
        this.clientType = clientType;
    }
}
