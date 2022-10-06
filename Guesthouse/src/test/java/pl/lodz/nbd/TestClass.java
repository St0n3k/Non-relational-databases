package pl.lodz.nbd;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import pl.lodz.nbd.model.Address;
import pl.lodz.nbd.repository.AddressRepository;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestClass {

    private static AddressRepository addressRepository;

    @BeforeAll
    static void beforeAll() {
        addressRepository = new AddressRepository();
    }

    @Test
    void test1() {
        addressRepository = new AddressRepository();
        Address address = new Address("Łódź", "Astronautów", 41);
        addressRepository.add(address);

    }

}
