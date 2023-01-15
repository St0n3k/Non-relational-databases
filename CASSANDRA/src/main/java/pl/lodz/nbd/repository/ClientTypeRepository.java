package pl.lodz.nbd.repository;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.datastax.oss.driver.api.core.type.DataTypes;
import pl.lodz.nbd.common.GuesthouseFinals;
import pl.lodz.nbd.dao.ClientTypeDao;
import pl.lodz.nbd.mapper.ClientTypeMapper;
import pl.lodz.nbd.mapper.ClientTypeMapperBuilder;
import pl.lodz.nbd.model.ClientTypes.ClientType;

import java.util.List;
import java.util.Optional;

import static com.datastax.oss.driver.api.querybuilder.SchemaBuilder.createTable;
import static com.datastax.oss.driver.api.querybuilder.SchemaBuilder.dropTable;

public class ClientTypeRepository {

    private final ClientTypeDao clientTypeDao;

    public ClientTypeRepository(CqlSession session) {
        session.execute(dropTable(GuesthouseFinals.CLIENT_TYPES).ifExists().build());
        session.execute(createClientTypeTableStatement());

        ClientTypeMapper clientTypeMapper = new ClientTypeMapperBuilder(session).build();
        clientTypeDao = clientTypeMapper.clientTypeDao();
    }

    private SimpleStatement createClientTypeTableStatement() {
        return createTable(GuesthouseFinals.CLIENT_TYPES)
                .ifNotExists()
                .withPartitionKey(GuesthouseFinals.CLIENT_TYPE_DISCRIMINATOR, DataTypes.TEXT)
                .withColumn(GuesthouseFinals.CLIENT_TYPE_DISCOUNT, DataTypes.DOUBLE)
                .build();
    }

    public boolean add(ClientType clientType) {
        return clientTypeDao.create(clientType);
    }

    public void remove(ClientType clientType) {
        clientTypeDao.remove(clientType);
    }

    public void update(ClientType clientType) {
        clientTypeDao.update(clientType);
    }

    public List<ClientType> getAll() {
        return clientTypeDao.findAll();
    }

    public Optional<ClientType> getByType(String type) {
        return Optional.ofNullable(clientTypeDao.findByType(type));
    }
}
