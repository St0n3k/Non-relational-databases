package pl.lodz.nbd.provider;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.Row;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.datastax.oss.driver.api.mapper.MapperContext;
import com.datastax.oss.driver.api.querybuilder.QueryBuilder;
import com.datastax.oss.driver.api.querybuilder.relation.Relation;
import com.datastax.oss.driver.api.querybuilder.select.Select;
import pl.lodz.nbd.common.GuesthouseFinals;
import pl.lodz.nbd.model.ClientTypes.*;

import java.util.List;
import java.util.stream.Collectors;

import static com.datastax.oss.driver.api.querybuilder.QueryBuilder.literal;

public class ClientTypeQueryProvider {

    private final CqlSession session;


    public ClientTypeQueryProvider(MapperContext ctx) {
        this.session = ctx.getSession();
    }

    public boolean create(ClientType clientType) {
        SimpleStatement createStatement = QueryBuilder
                .insertInto(GuesthouseFinals.CLIENT_TYPES)
                .value(GuesthouseFinals.CLIENT_TYPE_DISCOUNT, literal(clientType.getDiscount()))
                .value(GuesthouseFinals.CLIENT_TYPE_DISCRIMINATOR, literal(clientType.getDiscriminator()))
                .ifNotExists()
                .build();
        return session.execute(createStatement).wasApplied();
    }

    public void remove(ClientType clientType) {
        SimpleStatement removeStatement = QueryBuilder
                .deleteFrom(GuesthouseFinals.CLIENT_TYPES)
                .where(Relation.column(GuesthouseFinals.CLIENT_TYPE_DISCRIMINATOR).isEqualTo(literal(clientType.getDiscriminator())))
                .build();
        session.execute(removeStatement);
    }

    public void update(ClientType clientType) {
        SimpleStatement updateStatement = QueryBuilder
                .update(GuesthouseFinals.CLIENT_TYPES)
                .setColumn(GuesthouseFinals.CLIENT_TYPE_DISCOUNT, literal(clientType.getDiscount()))
                .where(Relation.column(GuesthouseFinals.CLIENT_TYPE_DISCRIMINATOR).isEqualTo(literal(clientType.getDiscriminator())))
                .build();
        session.execute(updateStatement);
    }

    public ClientType findByType(String type) {
        Select selectClientType = QueryBuilder
                .selectFrom(GuesthouseFinals.CLIENT_TYPES)
                .all()
                .where(Relation.column(GuesthouseFinals.CLIENT_TYPE_DISCRIMINATOR).isEqualTo(literal(type)));
        Row row = session.execute(selectClientType.build()).one();

        if (row == null) {
            return null;
        }

        return getClientType(row);
    }

    public List<ClientType> findAll() {
        Select selectClientTypes = QueryBuilder
                .selectFrom(GuesthouseFinals.CLIENT_TYPES)
                .all();
        List<Row> rows = session.execute(selectClientTypes.build()).all();

        return rows.stream().map(this::getClientType).collect(Collectors.toList());
    }

    private ClientType getClientType(Row row) {

        String discriminator = row.getString(GuesthouseFinals.CLIENT_TYPE_DISCRIMINATOR);
        double discount = row.getDouble(GuesthouseFinals.CLIENT_TYPE_DISCOUNT);
        assert discriminator != null;

        return switch (discriminator) {
            case "Default" -> new Default(discriminator, discount);
            case "Bronze" -> new Bronze(discriminator, discount);
            case "Silver" -> new Silver(discriminator, discount);
            case "Gold" -> new Gold(discriminator, discount);
            default -> throw new IllegalStateException("Unexpected value: " + discriminator);
        };
    }
}
