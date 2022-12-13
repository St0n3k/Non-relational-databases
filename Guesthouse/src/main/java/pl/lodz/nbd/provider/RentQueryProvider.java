package pl.lodz.nbd.provider;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.BatchStatement;
import com.datastax.oss.driver.api.core.cql.BatchType;
import com.datastax.oss.driver.api.core.cql.Row;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.datastax.oss.driver.api.mapper.MapperContext;
import com.datastax.oss.driver.api.querybuilder.QueryBuilder;
import com.datastax.oss.driver.api.querybuilder.relation.Relation;
import com.datastax.oss.driver.api.querybuilder.select.Select;
import pl.lodz.nbd.common.GuesthouseFinals;
import pl.lodz.nbd.common.RepositoryCreator;
import pl.lodz.nbd.model.Client;
import pl.lodz.nbd.model.Rent;
import pl.lodz.nbd.model.Room;
import pl.lodz.nbd.repository.ClientRepository;
import pl.lodz.nbd.repository.RoomRepository;
import pl.lodz.nbd.table.RentByClient;
import pl.lodz.nbd.table.RentByRoom;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.datastax.oss.driver.api.querybuilder.QueryBuilder.literal;

public class RentQueryProvider {

    private final CqlSession session;
    RoomRepository roomRepository = RepositoryCreator.getRoomRepository();
    ClientRepository clientRepository = RepositoryCreator.getClientRepository();


    public RentQueryProvider(MapperContext ctx) {
        this.session = ctx.getSession();
    }

    public boolean create(Rent rent) {
        RentByClient rentByClient = rent.toRentByClient();
        RentByRoom rentByRoom = rent.toRentByRoom();

        SimpleStatement insertRentByClient = QueryBuilder
                .insertInto(GuesthouseFinals.RENTS_BY_CLIENT)
                .value(GuesthouseFinals.CLIENT_PERSONAL_ID, literal(rentByClient.getClientPersonalId()))
                .value(GuesthouseFinals.RENT_BEGIN_DATE, literal(rentByClient.getBeginTime()))
                .value(GuesthouseFinals.RENT_END_DATE, literal(rentByClient.getEndTime()))
                .value(GuesthouseFinals.RENT_BOARD, literal(rentByClient.isBoard()))
                .value(GuesthouseFinals.RENT_COST, literal(rentByClient.getFinalCost()))
                .value(GuesthouseFinals.ROOM_NUMBER, literal(rentByClient.getRoomNumber()))
                .build();

        SimpleStatement insertRentByRoom = QueryBuilder
                .insertInto(GuesthouseFinals.RENTS_BY_ROOM)
                .value(GuesthouseFinals.CLIENT_PERSONAL_ID, literal(rentByRoom.getClientPersonalId()))
                .value(GuesthouseFinals.RENT_BEGIN_DATE, literal(rentByRoom.getBeginTime()))
                .value(GuesthouseFinals.RENT_END_DATE, literal(rentByRoom.getEndTime()))
                .value(GuesthouseFinals.RENT_BOARD, literal(rentByRoom.isBoard()))
                .value(GuesthouseFinals.RENT_COST, literal(rentByRoom.getFinalCost()))
                .value(GuesthouseFinals.ROOM_NUMBER, literal(rentByClient.getRoomNumber()))
                .build();

        BatchStatement batchStatement = BatchStatement.builder(BatchType.LOGGED)
                .addStatement(insertRentByClient)
                .addStatement(insertRentByRoom)
                .build();

        return session.execute(batchStatement).wasApplied();
    }

    public void update(Rent rent) {
        SimpleStatement updateRentByClient = QueryBuilder
                .update(GuesthouseFinals.RENTS_BY_CLIENT)
                .setColumn(GuesthouseFinals.RENT_BOARD, literal(rent.isBoard()))
                .setColumn(GuesthouseFinals.RENT_COST, literal(rent.getFinalCost()))
                .where(Relation.column(GuesthouseFinals.CLIENT_PERSONAL_ID).isEqualTo(literal(rent.getClient().getPersonalId())))
                .where(Relation.column(GuesthouseFinals.RENT_BEGIN_DATE).isEqualTo(literal(rent.getBeginTime())))
                .where(Relation.column(GuesthouseFinals.RENT_END_DATE).isEqualTo(literal(rent.getEndTime())))
                .build();

        SimpleStatement updateRentByRoom = QueryBuilder
                .update(GuesthouseFinals.RENTS_BY_ROOM)
                .setColumn(GuesthouseFinals.RENT_BOARD, literal(rent.isBoard()))
                .setColumn(GuesthouseFinals.RENT_COST, literal(rent.getFinalCost()))
                .where(Relation.column(GuesthouseFinals.ROOM_NUMBER).isEqualTo(literal(rent.getRoom().getRoomNumber())))
                .where(Relation.column(GuesthouseFinals.RENT_BEGIN_DATE).isEqualTo(literal(rent.getBeginTime())))
                .where(Relation.column(GuesthouseFinals.RENT_END_DATE).isEqualTo(literal(rent.getEndTime())))
                .build();

        BatchStatement batchStatement = BatchStatement.builder(BatchType.LOGGED)
                .addStatement(updateRentByClient)
                .addStatement(updateRentByRoom)
                .build();

        session.execute(batchStatement).wasApplied();
    }

    public void remove(Rent rent) {
        SimpleStatement removeRentByClient = QueryBuilder
                .deleteFrom(GuesthouseFinals.RENTS_BY_CLIENT)
                .where(Relation.column(GuesthouseFinals.CLIENT_PERSONAL_ID).isEqualTo(literal(rent.getClient().getPersonalId())))
                .where(Relation.column(GuesthouseFinals.RENT_BEGIN_DATE).isEqualTo(literal(rent.getBeginTime())))
                .where(Relation.column(GuesthouseFinals.RENT_END_DATE).isEqualTo(literal(rent.getEndTime())))
                .build();

        SimpleStatement removeRentByRoom = QueryBuilder
                .deleteFrom(GuesthouseFinals.RENTS_BY_ROOM)
                .where(Relation.column(GuesthouseFinals.ROOM_NUMBER).isEqualTo(literal(rent.getRoom().getRoomNumber())))
                .where(Relation.column(GuesthouseFinals.RENT_BEGIN_DATE).isEqualTo(literal(rent.getBeginTime())))
                .where(Relation.column(GuesthouseFinals.RENT_END_DATE).isEqualTo(literal(rent.getEndTime())))
                .build();

        BatchStatement batchStatement = BatchStatement.builder(BatchType.LOGGED)
                .addStatement(removeRentByClient)
                .addStatement(removeRentByRoom)
                .build();

        session.execute(batchStatement).wasApplied();
    }

    public List<Rent> findAll() {
        Select selectRent = QueryBuilder
                .selectFrom(GuesthouseFinals.RENTS_BY_ROOM)
                .all();
        List<Row> rows = session.execute(selectRent.build()).all();

        return rows.stream().map(this::getRent).collect(Collectors.toList());
    }

    public List<Rent> findByRoomNumber(int roomNumber) {
        Select selectRent = QueryBuilder
                .selectFrom(GuesthouseFinals.RENTS_BY_ROOM)
                .all()
                .where(Relation.column(GuesthouseFinals.ROOM_NUMBER).isEqualTo(literal(roomNumber)));
        List<Row> rows = session.execute(selectRent.build()).all();

        return rows.stream().map(this::getRent).collect(Collectors.toList());
    }

    public List<Rent> findByClientPersonalId(String personalId) {
        Select selectRent = QueryBuilder
                .selectFrom(GuesthouseFinals.RENTS_BY_CLIENT)
                .all()
                .where(Relation.column(GuesthouseFinals.CLIENT_PERSONAL_ID).isEqualTo(literal(personalId)));
        List<Row> rows = session.execute(selectRent.build()).all();

        return rows.stream().map(this::getRent).collect(Collectors.toList());
    }


    private Rent getRent(Row row) {

        Room room = roomRepository.getByRoomNumber(row.getInt(GuesthouseFinals.ROOM_NUMBER)).get();
        Client client = clientRepository.getClientByPersonalId(row.getString(GuesthouseFinals.CLIENT_PERSONAL_ID)).get();

        return new Rent(
                Objects.requireNonNull(row.getLocalDate(GuesthouseFinals.RENT_BEGIN_DATE)),
                Objects.requireNonNull(row.getLocalDate(GuesthouseFinals.RENT_END_DATE)),
                row.getBoolean(GuesthouseFinals.RENT_BOARD),
                row.getDouble(GuesthouseFinals.RENT_COST),
                client,
                room);
    }

}
