package pl.lodz.nbd;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import pl.lodz.nbd.common.RepositoryCreator;
import pl.lodz.nbd.manager.ClientManager;
import pl.lodz.nbd.model.Client;
import pl.lodz.nbd.repository.impl.ClientRepository;
import pl.lodz.nbd.repository.impl.ClientTypeRepository;

import java.sql.Time;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class PerformanceTest {
    private static final ClientRepository clientRepository = RepositoryCreator.getClientRepository();
    private static final ClientRepository clientCacheRepository = RepositoryCreator.getClientCacheRepository();
    private static final ClientTypeRepository clientTypeRepository = RepositoryCreator.getClientTypeRepository();
    private static final ClientManager clientManager = new ClientManager(clientRepository, clientTypeRepository);
    private static final ClientManager clientManagerCache = new ClientManager(clientCacheRepository, clientTypeRepository);

    @BeforeAll
    static void initialize(){
        clientManagerCache.registerClient("Test", "Test","123","Lodz", "Piotrkowska", 1);
        clientManagerCache.registerClient("Test", "Test","124","Lodz", "Piotrkowska", 1);
        clientManagerCache.registerClient("Test", "Test","125","Lodz", "Piotrkowska", 1);
        clientManagerCache.registerClient("Test", "Test","126","Lodz", "Piotrkowska", 1);
        clientManagerCache.registerClient("Test", "Test","127","Lodz", "Piotrkowska", 1);
    }

    void getTest(){
        clientManager.getByPersonalId("123");
        clientManager.getByPersonalId("124");
        clientManager.getByPersonalId("125");
        clientManager.getByPersonalId("126");
        clientManager.getByPersonalId("127");
    }

    void getCacheTest(){
        clientManagerCache.getByPersonalId("123");
        clientManagerCache.getByPersonalId("124");
        clientManagerCache.getByPersonalId("125");
        clientManagerCache.getByPersonalId("126");
        clientManagerCache.getByPersonalId("127");
    }

    @Test
    void test(){
        long beginTime = System.currentTimeMillis();
        for (int i = 0; i < 100; i++) {
            getTest();
        }
        long endTime = System.currentTimeMillis();
        long mongoTime = endTime - beginTime;

        beginTime = System.currentTimeMillis();
        for (int i = 0; i < 100; i++) {
            getCacheTest();
        }
        endTime = System.currentTimeMillis();
        long cacheTime = endTime - beginTime;
        System.out.println("Mongo time:" + mongoTime);
        System.out.println("Cache time:" + cacheTime);
        assertTrue(cacheTime < mongoTime);
    }

}
