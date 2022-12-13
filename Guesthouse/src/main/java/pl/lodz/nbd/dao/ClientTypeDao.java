package pl.lodz.nbd.dao;

import com.datastax.oss.driver.api.mapper.annotations.Dao;
import com.datastax.oss.driver.api.mapper.annotations.QueryProvider;
import com.datastax.oss.driver.api.mapper.annotations.StatementAttributes;
import pl.lodz.nbd.model.ClientTypes.ClientType;
import pl.lodz.nbd.providers.ClientTypeQueryProvider;

import java.util.List;

@Dao
public interface ClientTypeDao {

    @StatementAttributes(consistencyLevel = "QUORUM")
    @QueryProvider(providerClass = ClientTypeQueryProvider.class)
    void create(ClientType clientType);

    @StatementAttributes(consistencyLevel = "QUORUM")
    @QueryProvider(providerClass = ClientTypeQueryProvider.class)
    void remove(ClientType clientType);

    @StatementAttributes(consistencyLevel = "QUORUM")
    @QueryProvider(providerClass = ClientTypeQueryProvider.class)
    void update(ClientType clientType);

    @StatementAttributes(consistencyLevel = "QUORUM")
    @QueryProvider(providerClass = ClientTypeQueryProvider.class)
    ClientType findByType(String type);

    @StatementAttributes(consistencyLevel = "QUORUM")
    @QueryProvider(providerClass = ClientTypeQueryProvider.class)
    List<ClientType> findAll();
}
