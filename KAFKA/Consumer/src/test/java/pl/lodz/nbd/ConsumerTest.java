package pl.lodz.nbd;

import org.junit.jupiter.api.Test;

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
