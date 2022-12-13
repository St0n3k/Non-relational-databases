package pl.lodz.nbd.providers;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.Row;
import com.datastax.oss.driver.api.mapper.MapperContext;
import com.datastax.oss.driver.api.querybuilder.QueryBuilder;
import com.datastax.oss.driver.api.querybuilder.relation.Relation;
import com.datastax.oss.driver.api.querybuilder.select.Select;
import pl.lodz.nbd.common.GuesthouseFinals;
import pl.lodz.nbd.model.Room;

import java.util.List;
import java.util.stream.Collectors;

import static com.datastax.oss.driver.api.querybuilder.QueryBuilder.literal;

public class RoomQueryProvider {
    private final CqlSession session;


    public RoomQueryProvider(MapperContext ctx) {
        this.session = ctx.getSession();
    }

    public Room findByRoomNumber(int number) {
        Select selectRoom = QueryBuilder
                .selectFrom(GuesthouseFinals.ROOMS)
                .all()
                .where(Relation.column(GuesthouseFinals.ROOM_NUMBER).isEqualTo(literal(number)));
        Row row = session.execute(selectRoom.build()).one();

        if (row == null) {
            return null;
        }

        return getRoom(row);
    }

    public List<Room> findAll() {
        Select selectRoom = QueryBuilder
                .selectFrom(GuesthouseFinals.ROOMS)
                .all();
        List<Row> rows = session.execute(selectRoom.build()).all();

        return rows.stream().map(this::getRoom).collect(Collectors.toList());
    }

    private Room getRoom(Row row) {
        return Room.builder()
                .roomNumber(row.getInt(GuesthouseFinals.ROOM_NUMBER))
                .size(row.getInt(GuesthouseFinals.ROOM_SIZE))
                .price(row.getDouble(GuesthouseFinals.ROOM_PRICE))
                .build();
    }
}
