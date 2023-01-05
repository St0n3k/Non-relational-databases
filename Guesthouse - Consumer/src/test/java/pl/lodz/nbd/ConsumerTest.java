package pl.lodz.nbd;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ConsumerTest {
    @Test
    void consumerTest(){
        RentConsumer rentConsumer = new RentConsumer();
        rentConsumer.consume();
    }

    @Test
    void consumerTwoTest(){
        RentConsumer rentConsumer = new RentConsumer();
        rentConsumer.consume();
    }

}
