package pl.lodz.nbd.provider;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.Row;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.datastax.oss.driver.api.mapper.MapperContext;
import com.datastax.oss.driver.api.querybuilder.QueryBuilder;
import com.datastax.oss.driver.api.querybuilder.relation.Relation;
import com.datastax.oss.driver.api.querybuilder.select.Select;
import pl.lodz.nbd.common.GuesthouseFinals;
import pl.lodz.nbd.table.ClientEntity;

import java.util.List;
import java.util.stream.Collectors;

import static com.datastax.oss.driver.api.querybuilder.QueryBuilder.literal;

public class ClientQueryProvider {

    private final CqlSession session;


    public ClientQueryProvider(MapperContext ctx) {
        this.session = ctx.getSession();
    }

    public void create(ClientEntity client) {
        SimpleStatement createStatement = QueryBuilder
                .insertInto(GuesthouseFinals.CLIENTS)
                .value(GuesthouseFinals.CLIENT_PERSONAL_ID, literal(client.getPersonalId()))
                .value(GuesthouseFinals.CLIENT_FIRST_NAME, literal(client.getFirstName()))
                .value(GuesthouseFinals.CLIENT_LAST_NAME, literal(client.getLastName()))
                .value(GuesthouseFinals.CLIENT_TYPE, literal(client.getClientType()))
                .build();
        session.execute(createStatement);
    }

    public ClientEntity findByPersonalId(String personalId) {
        Select selectClient = QueryBuilder
                .selectFrom(GuesthouseFinals.CLIENTS)
                .all()
                .where(Relation.column(GuesthouseFinals.CLIENT_PERSONAL_ID).isEqualTo(literal(personalId)));
        Row row = session.execute(selectClient.build()).one();

        if (row == null) {
            return null;
        }

        return getClientEntity(row);
    }

    public List<ClientEntity> findAll() {
        Select selectClient = QueryBuilder
                .selectFrom(GuesthouseFinals.CLIENTS)
                .all();
        List<Row> rows = session.execute(selectClient.build()).all();

        return rows.stream().map(this::getClientEntity).collect(Collectors.toList());
    }

    private ClientEntity getClientEntity(Row row) {
        return new ClientEntity(
                row.getString(GuesthouseFinals.CLIENT_PERSONAL_ID),
                row.getString(GuesthouseFinals.CLIENT_FIRST_NAME),
                row.getString(GuesthouseFinals.CLIENT_LAST_NAME),
                row.getString(GuesthouseFinals.CLIENT_TYPE));
    }
}
