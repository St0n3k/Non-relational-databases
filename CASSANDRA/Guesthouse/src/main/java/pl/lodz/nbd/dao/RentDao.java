package pl.lodz.nbd.dao;

import com.datastax.oss.driver.api.mapper.annotations.Dao;
import com.datastax.oss.driver.api.mapper.annotations.QueryProvider;
import com.datastax.oss.driver.api.mapper.annotations.StatementAttributes;
import pl.lodz.nbd.model.Rent;
import pl.lodz.nbd.provider.RentQueryProvider;

import java.util.List;

@Dao
public interface RentDao {

    @StatementAttributes(consistencyLevel = "QUORUM")
    @QueryProvider(providerClass = RentQueryProvider.class)
    boolean create(Rent rent);

    @StatementAttributes(consistencyLevel = "QUORUM")
    @QueryProvider(providerClass = RentQueryProvider.class)
    void update(Rent rent);

    @StatementAttributes(consistencyLevel = "QUORUM")
    @QueryProvider(providerClass = RentQueryProvider.class)
    void remove(Rent rent);

    @StatementAttributes(consistencyLevel = "QUORUM")
    @QueryProvider(providerClass = RentQueryProvider.class)
    List<Rent> findAll();

    @StatementAttributes(consistencyLevel = "QUORUM")
    @QueryProvider(providerClass = RentQueryProvider.class)
    List<Rent> findByRoomNumber(int roomNumber);

    @StatementAttributes(consistencyLevel = "QUORUM")
    @QueryProvider(providerClass = RentQueryProvider.class)
    List<Rent> findByClientPersonalId(String personalId);
}
