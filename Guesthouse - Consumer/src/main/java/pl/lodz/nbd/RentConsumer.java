package pl.lodz.nbd;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.kafka.clients.admin.*;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.KafkaFuture;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.UUIDDeserializer;
import pl.lodz.nbd.common.ClientTypeInstanceCreator;
import pl.lodz.nbd.manager.RentManager;
import pl.lodz.nbd.model.ClientTypes.ClientType;
import pl.lodz.nbd.model.Rent;
import pl.lodz.nbd.repository.impl.ClientRepository;
import pl.lodz.nbd.repository.impl.ClientTypeRepository;
import pl.lodz.nbd.repository.impl.RentRepository;
import pl.lodz.nbd.repository.impl.RoomRepository;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;


public class RentConsumer {
    private RentRepository rentRepository = new RentRepository();

    private Properties adminProperties;
    private final String consumerGroupName = "rent-consumer";

    public RentConsumer() {
        this.adminProperties = new Properties();
        this.adminProperties.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka1:9192,kafka1:9292,kafka3:9392");
    }

    private List<ConsumerGroupDescription> getConsumerGroupInfo(){
        List<ConsumerGroupDescription> descriptions = new ArrayList<ConsumerGroupDescription>();
        try(Admin admin = Admin.create(this.adminProperties)){
            DescribeConsumerGroupsResult describeConsumerGroupsResult =
                    admin.describeConsumerGroups(List.of(consumerGroupName));
            Map<String, KafkaFuture<ConsumerGroupDescription>> describedGroups = describeConsumerGroupsResult.describedGroups();
            for (Future<ConsumerGroupDescription> group : describedGroups.values()) {
                ConsumerGroupDescription consumerGroupDescription = null;
                try {
                    consumerGroupDescription = group.get();
                    descriptions.add(consumerGroupDescription);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } catch (ExecutionException e) {
                    throw new RuntimeException(e);
                }
            }

        }
        return descriptions;

    }
    public void consume(){
        try(Admin admin = Admin.create(this.adminProperties)) {
            admin.deleteConsumerGroups(List.of(consumerGroupName));
        }

        Properties consumerConfig = new Properties();
        consumerConfig.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, UUIDDeserializer.class.getName());
        consumerConfig.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        consumerConfig.put(ConsumerConfig.GROUP_ID_CONFIG, consumerGroupName);//dynamiczny przydział
        consumerConfig.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka1:9192,kafka2:9292,kafka3:9392");



        try (KafkaConsumer consumer = new KafkaConsumer(consumerConfig)) {
            consumer.subscribe(List.of("rents"));

            for(ConsumerGroupDescription desc : getConsumerGroupInfo()){
                System.out.println(desc);
                for(MemberDescription member : desc.members()){
                    System.out.println(member);
                }
            }

            while (true) {
                ConsumerRecords<UUID, String> records = consumer.poll(Duration.ofMillis(100));
                for (ConsumerRecord<UUID, String> record : records) {
                    UUID key = record.key();
                    String value = record.value();
                    Gson mapper = new GsonBuilder()
                            .registerTypeAdapter(ClientType.class, new ClientTypeInstanceCreator())
                            .create();
                    Rent rent = mapper.fromJson(value, Rent.class);
                    System.out.println(rent);
                    rentRepository.add(rent);
                    consumer.commitSync();
                }
            }
        }
    }
}
