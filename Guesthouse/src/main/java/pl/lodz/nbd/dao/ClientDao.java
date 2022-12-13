package pl.lodz.nbd.dao;

import com.datastax.oss.driver.api.mapper.annotations.*;
import pl.lodz.nbd.provider.ClientQueryProvider;
import pl.lodz.nbd.table.ClientEntity;

import java.util.List;

@Dao
public interface ClientDao {

    @StatementAttributes(consistencyLevel = "QUORUM")
    @Insert(ifNotExists = true)
    boolean create(ClientEntity client);

    @StatementAttributes(consistencyLevel = "QUORUM")
    @Delete
    void remove(ClientEntity client);

    @StatementAttributes(consistencyLevel = "QUORUM")
    @Update
    void update(ClientEntity client);

    @StatementAttributes(consistencyLevel = "QUORUM")
    @QueryProvider(providerClass = ClientQueryProvider.class)
    ClientEntity findByPersonalId(String personalId);

    @StatementAttributes(consistencyLevel = "QUORUM")
    @QueryProvider(providerClass = ClientQueryProvider.class)
    List<ClientEntity> findAll();
}
