package pl.lodz.nbd.model;

import com.sun.istack.NotNull;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Entity
@NamedQueries({
        @NamedQuery(name="Client.getAll",
                query="SELECT c FROM Client c")
})
@Data
@NoArgsConstructor
public class Client {

    @Id
    @GeneratedValue(generator = "clientId")
    @NotNull
    @Column(name = "client_id")
    private Long clientId;

    @NotNull
    @Column(name = "first_name")
    private String firstName;

    @NotNull
    @Column(name = "last_name")
    private String lastName;

    @NotNull
    @Column(name = "personal_id", unique = true)
    private String personalId;

    @ManyToOne
    @JoinColumn(name = "address_id")
    private Address address;

    public Client(String firstName, String lastName, String personalId, Address address) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.personalId = personalId;
        this.address = address;
    }
}
