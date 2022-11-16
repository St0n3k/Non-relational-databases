package pl.lodz.nbd.repository.cache;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import pl.lodz.nbd.common.ClientTypeInstanceCreator;
import pl.lodz.nbd.model.ClientTypes.ClientType;
import pl.lodz.nbd.model.Rent;
import pl.lodz.nbd.repository.impl.RentRepository;
import redis.clients.jedis.DefaultJedisClientConfig;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisClientConfig;
import redis.clients.jedis.JedisPooled;
import redis.clients.jedis.json.Path;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class RentCacheRepository extends RentRepository {

    private JedisPooled pool;
    private Gson gson;

    private boolean connected;

    public RentCacheRepository() {
        try {
            gson = new GsonBuilder().registerTypeAdapter(ClientType.class, new ClientTypeInstanceCreator()).create();

            JedisClientConfig clientConfig = DefaultJedisClientConfig.builder().socketTimeoutMillis(100).build();
            pool = new JedisPooled(new HostAndPort("localhost", 6379), clientConfig);
            pool.set("ping", "ping");
            connected = true;
        } catch (Exception e) {
            connected = false;
        }
    }

    @Override
    public Optional<Rent> add(Rent rent) {
        if (connected) {
            addToCache(rent);
        }
        return super.add(rent);
    }

    @Override
    public void remove(Rent rent) {
        if (connected) {
            pool.jsonDel("rents:" + rent.getUuid());
        }
        super.remove(rent);
    }

    @Override
    public Optional<Rent> getById(UUID id) {
        if (connected) {
            String json = pool.jsonGetAsPlainString("rents:" + id, Path.ROOT_PATH);
            Rent rent = gson.fromJson(json, Rent.class);

            if (rent != null) {
                System.out.println("Got rent from cache!");
                return Optional.of(rent);
            } else {
               Optional<Rent> optionalRent = super.getById(id);
               optionalRent.ifPresent(this::addToCache);
               return optionalRent;
            }
        }
        return super.getById(id);
    }


    @Override
    public boolean update(Rent rent) {
        boolean successful = super.update(rent);
        if (connected && successful) {
            pool.jsonSet("rents:" + rent.getUuid(), gson.toJson(rent));
        }
        return successful;
    }

    private void addToCache(Rent rent){
        pool.jsonSet("rents:" + rent.getUuid(), gson.toJson(rent));
        pool.expire("rents:" + rent.getUuid(), 60);
    }
}
