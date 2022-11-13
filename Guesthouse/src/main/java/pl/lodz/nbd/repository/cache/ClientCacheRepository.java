package pl.lodz.nbd.repository.cache;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import pl.lodz.nbd.common.ClientTypeInstanceCreator;
import pl.lodz.nbd.model.Client;
import pl.lodz.nbd.model.ClientTypes.ClientType;
import pl.lodz.nbd.repository.impl.ClientRepository;
import redis.clients.jedis.DefaultJedisClientConfig;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisClientConfig;
import redis.clients.jedis.JedisPooled;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

//TODO fix or delete
public class ClientCacheRepository extends ClientRepository {

    private final JedisPooled pool;
    private final Gson gson;

    public ClientCacheRepository() {
        JedisClientConfig clientConfig = DefaultJedisClientConfig.builder().build();
        pool = new JedisPooled(new HostAndPort("localhost", 6379), clientConfig);
        gson = new GsonBuilder().registerTypeAdapter(ClientType.class, new ClientTypeInstanceCreator()).create();
    }

    @Override
    public Optional<Client> add(Client client) {
        Optional<Client> optionalClient = super.add(client);
        Thread thread = new Thread(() -> {
            pool.jsonSet("clients:" + client.getPersonalId(), gson.toJson(client));
            pool.expire("clients:" + client.getPersonalId(), 60);
        });
        thread.start();
        return optionalClient;
    }

    @Override
    public List<Client> getAll() {
        List<Client> clients = super.getAll();
        Thread thread = new Thread(() -> clients.forEach((client -> pool.jsonSet("clients:" + client.getPersonalId(), gson.toJson(client)))));
        thread.start();
        return clients;
    }

    @Override
    public void remove(Client client) {
        Thread thread = new Thread(() -> pool.jsonDel("clients:" + client.getPersonalId()));
        thread.start();
        super.remove(client);
    }

    @Override
    public Optional<Client> getById(UUID id) {
        return super.getById(id);
    }

    @Override
    public Optional<Client> getClientByPersonalId(String personalId) {
        //TODO ftSearch?
        Client client = pool.jsonGet("clients:" + personalId, Client.class);

        if (client != null) {
            System.out.println("Got client from cache!");
            return Optional.of(client);
        }
        return super.getClientByPersonalId(personalId);
    }

    @Override
    public boolean update(Client client) {
        boolean successful = super.update(client);
        if (successful) {
            Thread thread = new Thread(() -> pool.jsonSet("clients:" + client.getPersonalId(), gson.toJson(client)));
            thread.start();
        }
        return successful;
    }
}
