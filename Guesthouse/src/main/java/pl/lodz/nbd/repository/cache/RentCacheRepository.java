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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class RentCacheRepository extends RentRepository {

    private final JedisPooled pool;
    private final Gson gson;

    public RentCacheRepository() {
        JedisClientConfig clientConfig = DefaultJedisClientConfig.builder().build();
        pool = new JedisPooled(new HostAndPort("localhost", 6379), clientConfig);
        gson = new GsonBuilder().registerTypeAdapter(ClientType.class, new ClientTypeInstanceCreator()).create();
    }


    @Override
    public Optional<Rent> add(Rent rent) {
        pool.jsonSet("rents:" + rent.getUuid(), gson.toJson(rent));
        pool.expire("rents:" + rent.getUuid(), 60);
        return super.add(rent);
    }

    @Override
    public void remove(Rent rent) {
        pool.jsonDel("rents:" + rent.getUuid());
        super.remove(rent);
    }

    @Override
    public Optional<Rent> getById(UUID id) {

        Rent rent = pool.jsonGet("rents:" + id, Rent.class);

        if (rent != null) {
            System.out.println("Got rent from cache!");
            return Optional.of(rent);
        }
        return super.getById(id);
    }

    @Override
    public List<Rent> getAll() {
        List<Rent> rents = super.getAll();
        rents.forEach((rent -> new Thread(() -> {
            pool.jsonSet("rents:" + rent.getUuid(), gson.toJson(rent));
        }).start()));
        return rents;
    }

    @Override
    public List<Rent> getByRoomNumber(int roomNumber) {
        return super.getByRoomNumber(roomNumber);
    }

    @Override
    public List<Rent> getByClientPersonalId(String personalId) {
        return super.getByClientPersonalId(personalId);
    }

    @Override
    public boolean update(Rent rent) {
        pool.jsonSet("rents:" + rent.getUuid(), gson.toJson(rent));
        return super.update(rent);
    }

    @Override
    public boolean isColliding(LocalDateTime beginDate, LocalDateTime endDate, int roomNumber) {
        return super.isColliding(beginDate, endDate, roomNumber);
    }

    @Override
    public boolean updateIsBeingRented(int roomNumber, boolean increment) {
        return super.updateIsBeingRented(roomNumber, increment);
    }
}
