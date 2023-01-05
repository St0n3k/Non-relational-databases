package pl.lodz.nbd.repository.cache;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.ConfigProvider;
import pl.lodz.nbd.common.ClientTypeInstanceCreator;
import pl.lodz.nbd.model.ClientTypes.ClientType;
import pl.lodz.nbd.model.Rent;
import pl.lodz.nbd.repository.impl.RentRepository;
import redis.clients.jedis.*;
import redis.clients.jedis.json.Path;

import java.util.Optional;
import java.util.UUID;

public class RentCacheRepository extends RentRepository {

    private JedisPooled pool;
    private Jedis jedis;
    private Gson gson;

    private long lastCheck;
    private boolean connected;

    public RentCacheRepository() {
        try {
            gson = new GsonBuilder().registerTypeAdapter(ClientType.class, new ClientTypeInstanceCreator()).create();

            Config config = ConfigProvider.getConfig();

            String host = config.getValue("jedis.host", String.class);
            int port = config.getValue("jedis.port", Integer.class);

            JedisClientConfig clientConfig = DefaultJedisClientConfig.builder().socketTimeoutMillis(100).build();
            jedis = new Jedis(new HostAndPort(host, port), clientConfig);
            pool = new JedisPooled(new HostAndPort(host, port), clientConfig);
            pool.set("ping", "ping");
            connected = true;
        } catch (Exception e) {
            connected = false;
        }
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
    public Optional<Rent> add(Rent rent) {
        Optional<Rent> optionalRent = super.add(rent);
        if(optionalRent.isEmpty()){
            return optionalRent;
        }
        if (connected) {
            try{
                addToCache(rent);
            } catch (Exception e) {
                jedisConnectionExceptionHandler(e);
                add(rent);
            }
        } else {
            if(healthcheck()){
                add(rent);
            }
        }
        return optionalRent;
    }

    @Override
    public void remove(Rent rent) {
        if (connected) {
            try{
                pool.jsonDel("rents:" + rent.getUuid());
            } catch (Exception e) {
                jedisConnectionExceptionHandler(e);
                remove(rent);
            }
        } else {
            if(healthcheck()){
                remove(rent);
            }
        }
        super.remove(rent);
    }

    @Override
    public Optional<Rent> getById(UUID id) {
        if (connected) {
            Rent rent = null;
            try{
                String json = pool.jsonGetAsPlainString("rents:" + id, Path.ROOT_PATH);
                rent = gson.fromJson(json, Rent.class);
            } catch (Exception e) {
                jedisConnectionExceptionHandler(e);
                getById(id);
            }

            if (rent != null) {
                System.out.println("Got rent from cache!");
                return Optional.of(rent);
            } else {
               Optional<Rent> optionalRent = super.getById(id);
                try{
                    optionalRent.ifPresent(this::addToCache);
                } catch (Exception e) {
                    jedisConnectionExceptionHandler(e);
                }
               return optionalRent;
            }
        } else {
            if(healthcheck()){
                getById(id);
            }
        }
        return super.getById(id);
    }


    @Override
    public boolean update(Rent rent) {
        boolean successful = super.update(rent);
        if (connected && successful) {
            try{
                pool.jsonSet("rents:" + rent.getUuid(), gson.toJson(rent));
            } catch (Exception e) {
                jedisConnectionExceptionHandler(e);
                update(rent);
            }
        } else if(!connected && successful){
            if(healthcheck()){
                update(rent);
            }
        }
        return successful;
    }

    private void addToCache(Rent rent){
        pool.jsonSet("rents:" + rent.getUuid(), gson.toJson(rent));
        pool.expire("rents:" + rent.getUuid(), 60);
    }

    private void jedisConnectionExceptionHandler(Exception e){
        e.printStackTrace();
        connected = false;
        lastCheck = System.currentTimeMillis();
    }
}
