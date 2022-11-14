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
import redis.clients.jedis.json.Path;

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

        pool.jsonSet("clients:" + client.getPersonalId(), gson.toJson(client));
        pool.expire("clients:" + client.getPersonalId(), 60);

        return optionalClient;
    }

    @Override
    public List<Client> getAll() {
        List<Client> clients = super.getAll();
        clients.forEach((client -> pool.jsonSet("clients:" + client.getPersonalId(), gson.toJson(client))));
        return clients;
    }

    @Override
    public void remove(Client client) {
        pool.jsonDel("clients:" + client.getPersonalId());
        super.remove(client);
    }

    @Override
    public Optional<Client> getById(UUID id) {
        return super.getById(id);
    }

    @Override
    public Optional<Client> getClientByPersonalId(String personalId) {
        //TODO ftSearch?
        String json = pool.jsonGetAsPlainString("clients:" + personalId, Path.ROOT_PATH);
        Client client = gson.fromJson(json, Client.class);

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
            pool.jsonSet("clients:" + client.getPersonalId(), gson.toJson(client));
        }
        return successful;
    }
}
