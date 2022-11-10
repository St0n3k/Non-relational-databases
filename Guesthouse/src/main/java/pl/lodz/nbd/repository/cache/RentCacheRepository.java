package pl.lodz.nbd.repository.cache;

import org.json.JSONObject;
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

    private JedisPooled pool;

    public RentCacheRepository() {
        JedisClientConfig clientConfig = DefaultJedisClientConfig.builder().build();
        pool = new JedisPooled(new HostAndPort("localhost", 6379), clientConfig);
    }


    @Override
    public Optional<Rent> add(Rent rent) {
        pool.jsonSet("rents:" + rent.getUuid(), new JSONObject(rent));
        pool.expire("rents:" + rent.getUuid(), 15);
        return super.add(rent);
    }

    @Override
    public void remove(Rent rent) {
        super.remove(rent);
    }

    @Override
    public Optional<Rent> getById(UUID id) {
//        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.findAndRegisterModules();
//        //Rent rent = pool.jsonGet("rents:" + id, Rent.class);
//        Rent rent;
//        try {
//            rent = objectMapper.readValue(pool.get("rents:" + id), Rent.class);
//        } catch (JsonProcessingException e) {
//            throw new RuntimeException(e);
//        }
//        System.out.println(rent);
        return super.getById(id);
    }

    @Override
    public List<Rent> getAll() {
        return super.getAll();
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
