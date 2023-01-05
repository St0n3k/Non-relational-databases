package pl.lodz.nbd.repository.cache;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.ConfigProvider;
import pl.lodz.nbd.common.ClientTypeInstanceCreator;
import pl.lodz.nbd.model.Client;
import pl.lodz.nbd.model.ClientTypes.ClientType;
import pl.lodz.nbd.repository.impl.ClientRepository;
import redis.clients.jedis.*;
import redis.clients.jedis.exceptions.JedisConnectionException;
import redis.clients.jedis.json.Path;

import java.util.Optional;

//TODO fix or delete
public class ClientCacheRepository extends ClientRepository {

    private JedisPooled pool;
    private Jedis jedis;
    private Gson gson;

    private long lastCheck;
    private boolean connected;

    public ClientCacheRepository() {
        try {
            gson = new GsonBuilder().registerTypeAdapter(ClientType.class, new ClientTypeInstanceCreator()).create();

            Config config = ConfigProvider.getConfig();

            String host = config.getValue("jedis.host", String.class);
            int port = config.getValue("jedis.port", Integer.class);

            JedisClientConfig clientConfig = DefaultJedisClientConfig.builder().socketTimeoutMillis(1000).build();
            jedis = new Jedis(new HostAndPort(host, port), clientConfig);
            pool = new JedisPooled(new HostAndPort(host, port), clientConfig);
            pool.set("ping", "ping");
            connected = true;
        } catch (JedisConnectionException e) {
            connected = false;
        }
        lastCheck = System.currentTimeMillis();
    }

    private boolean healthcheck(){
        if(System.currentTimeMillis() - lastCheck < 60000){
            return false;
        }
        try {
            pool.set("ping", "ping");
        } catch (Exception e) {
            return false;
        }
        if(!connected){
            jedis.flushDB();
            connected = true;
        }
        return true;
    }

    @Override
    public Optional<Client> add(Client client) {
        Optional<Client> optionalClient = super.add(client);

        if (connected && optionalClient.isPresent()) {
            try{
                addToCache(client);
            } catch (Exception e) {
                jedisConnectionExceptionHandler(e);
                add(client);
            }
        } else if(!connected && optionalClient.isPresent()) {
            if(healthcheck()){
                add(client);
            }
        }
        return optionalClient;
    }

    @Override
    public void remove(Client client) {
        if (connected) {
            try{
                pool.jsonDel("clients:" + client.getPersonalId());
            } catch (Exception e) {
                jedisConnectionExceptionHandler(e);
                remove(client);
            }
        } else {
            if(healthcheck()){
                remove(client);
            }
        }
        super.remove(client);
    }

    @Override
    public Optional<Client> getClientByPersonalId(String personalId) {
        if (connected) {
            Client client = null;
            try{
                String json = pool.jsonGetAsPlainString("clients:" + personalId, Path.ROOT_PATH);
                client = gson.fromJson(json, Client.class);
            } catch (Exception e) {
                jedisConnectionExceptionHandler(e);
                getClientByPersonalId(personalId);
            }

            if (client != null) {
                System.out.println("Got client from cache!");
                return Optional.of(client);
            } else {
                Optional<Client> optionalClient = super.getClientByPersonalId(personalId);
                try{
                    optionalClient.ifPresent(this::addToCache);
                } catch (Exception e) {
                    jedisConnectionExceptionHandler(e);
                }
            }
        } else {
            if(healthcheck()){
                getClientByPersonalId(personalId);
            }
        }
        return super.getClientByPersonalId(personalId);
    }

    @Override
    public boolean update(Client client) {
        boolean successful = super.update(client);

        if (connected && successful) {
            try{
                pool.jsonSet("clients:" + client.getPersonalId(), gson.toJson(client));
            } catch (Exception e) {
                jedisConnectionExceptionHandler(e);
                update(client);
            }
        } else if(!connected && successful){
            if(healthcheck()){
                update(client);
            }
        }
        return successful;
    }

    private void addToCache(Client client){
        pool.jsonSet("clients:" + client.getPersonalId(), gson.toJson(client));
        pool.expire("clients:" + client.getPersonalId(), 60);
    }

    private void jedisConnectionExceptionHandler(Exception e){
        e.printStackTrace();
        connected = false;
        lastCheck = System.currentTimeMillis();
    }
}
