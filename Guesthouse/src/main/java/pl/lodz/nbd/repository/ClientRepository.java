package pl.lodz.nbd.repository;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.datastax.oss.driver.api.core.type.DataTypes;
import pl.lodz.nbd.common.GuesthouseFinals;
import pl.lodz.nbd.dao.ClientDao;
import pl.lodz.nbd.mapper.ClientMapper;
import pl.lodz.nbd.mapper.ClientMapperBuilder;
import pl.lodz.nbd.model.Client;
import pl.lodz.nbd.table.ClientEntity;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.datastax.oss.driver.api.querybuilder.SchemaBuilder.createTable;
import static com.datastax.oss.driver.api.querybuilder.SchemaBuilder.dropTable;

public class ClientRepository {


    private final ClientDao clientDao;

    public ClientRepository(CqlSession session) {
        session.execute(dropTable(GuesthouseFinals.CLIENTS).ifExists().build());
        session.execute(createClientTableStatement());

        ClientMapper clientMapper = new ClientMapperBuilder(session).build();
        clientDao = clientMapper.clientDao();
    }

    private SimpleStatement createClientTableStatement() {
        return createTable(GuesthouseFinals.CLIENTS)
                .ifNotExists()
                .withPartitionKey(GuesthouseFinals.CLIENT_PERSONAL_ID, DataTypes.TEXT)
                .withColumn(GuesthouseFinals.CLIENT_FIRST_NAME, DataTypes.TEXT)
                .withColumn(GuesthouseFinals.CLIENT_LAST_NAME, DataTypes.TEXT)
                .withColumn(GuesthouseFinals.CLIENT_TYPE, DataTypes.TEXT)
                .build();
    }

    public boolean add(Client client) {
        return clientDao.create(client.toClientEntity());
    }

    public List<Client> getAll() {
        return clientDao.findAll()
                .stream()
                .map((ClientEntity::toClient))
                .collect(Collectors.toList());
    }


    public void remove(Client client) {
        clientDao.remove(client.toClientEntity());
    }

    public Optional<Client> getClientByPersonalId(String personalId) {
        ClientEntity byPersonalId = clientDao.findByPersonalId(personalId);
        if (byPersonalId == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(clientDao.findByPersonalId(personalId).toClient());
    }

    public void update(Client client) {
        clientDao.update(client.toClientEntity());
    }

}
