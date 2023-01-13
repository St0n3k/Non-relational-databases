package pl.lodz.nbd.common;


import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.config.DefaultDriverOption;
import com.datastax.oss.driver.api.core.config.DriverConfigLoader;
import pl.lodz.nbd.repository.ClientRepository;
import pl.lodz.nbd.repository.ClientTypeRepository;
import pl.lodz.nbd.repository.RentRepository;
import pl.lodz.nbd.repository.RoomRepository;

import java.net.InetSocketAddress;
import java.time.Duration;

public class RepositoryCreator {

    private static final CqlSession session = CqlSession.builder()
            .addContactPoint(new InetSocketAddress("localhost", 9042))
            .addContactPoint(new InetSocketAddress("localhost", 9043))
            .withLocalDatacenter("dc1")
            .withKeyspace(GuesthouseFinals.GUESTHOUSE_NAMESPACE)
            .withAuthCredentials("cassandra", "cassandra")
            .withConfigLoader(DriverConfigLoader.programmaticBuilder()
                    .withDuration(DefaultDriverOption.METADATA_SCHEMA_REQUEST_TIMEOUT, Duration.ofMillis(60000))
                    .withDuration(DefaultDriverOption.CONNECTION_INIT_QUERY_TIMEOUT, Duration.ofMillis(60000))
                    .withDuration(DefaultDriverOption.REQUEST_TIMEOUT, Duration.ofMillis(15000))
                    .build())
            .build();
    private static final ClientRepository clientRepository = new ClientRepository(session);
    private static final RoomRepository roomRepository = new RoomRepository(session);
    private static final ClientTypeRepository clientTypeRepository = new ClientTypeRepository(session);
    private static final RentRepository rentRepository = new RentRepository(session);

    public static ClientRepository getClientRepository() {
        return clientRepository;
    }


    public static ClientTypeRepository getClientTypeRepository() {
        return clientTypeRepository;
    }

    public static RoomRepository getRoomRepository() {
        return roomRepository;
    }

    public static RentRepository getRentRepository() {
        return rentRepository;
    }

}
