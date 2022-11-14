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
import redis.clients.jedis.exceptions.JedisConnectionException;
import redis.clients.jedis.json.Path;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

//TODO fix or delete
public class ClientCacheRepository extends ClientRepository {

    private JedisPooled pool;
    private Gson gson;

    private boolean connected;

    public ClientCacheRepository() {
        try {
            gson = new GsonBuilder().registerTypeAdapter(ClientType.class, new ClientTypeInstanceCreator()).create();

            JedisClientConfig clientConfig = DefaultJedisClientConfig.builder().socketTimeoutMillis(1000).build();
            pool = new JedisPooled(new HostAndPort("localhost", 6379), clientConfig);
            pool.set("ping", "ping");
            connected = true;
        } catch (JedisConnectionException e) {
            connected = false;
        }
    }


    @Override
    public Optional<Client> add(Client client) {
        Optional<Client> optionalClient = super.add(client);

        if (connected && optionalClient.isPresent()) {
            pool.jsonSet("clients:" + client.getPersonalId(), gson.toJson(client));
            pool.expire("clients:" + client.getPersonalId(), 60);
        }
        return optionalClient;
    }

    @Override
    public List<Client> getAll() {
        return super.getAll();
    }

    @Override
    public void remove(Client client) {
        if (connected) {
            pool.jsonDel("clients:" + client.getPersonalId());
        }
        super.remove(client);
    }

    @Override
    public Optional<Client> getById(UUID id) {
        return super.getById(id);
    }

    @Override
    public Optional<Client> getClientByPersonalId(String personalId) {
        if (connected) {
            String json = pool.jsonGetAsPlainString("clients:" + personalId, Path.ROOT_PATH);
            Client client = gson.fromJson(json, Client.class);

            if (client != null) {
                System.out.println("Got client from cache!");
                return Optional.of(client);
            }
        }
        return super.getClientByPersonalId(personalId);
    }

    @Override
    public boolean update(Client client) {
        boolean successful = super.update(client);

        if (connected && successful) {
            pool.jsonSet("clients:" + client.getPersonalId(), gson.toJson(client));
        }
        return successful;
    }
}
