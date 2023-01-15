package pl.lodz.nbd.repository;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.datastax.oss.driver.api.core.type.DataTypes;
import pl.lodz.nbd.common.GuesthouseFinals;
import pl.lodz.nbd.dao.RentDao;
import pl.lodz.nbd.mapper.RentMapper;
import pl.lodz.nbd.mapper.RentMapperBuilder;
import pl.lodz.nbd.model.Rent;

import java.time.LocalDate;
import java.util.List;

import static com.datastax.oss.driver.api.querybuilder.SchemaBuilder.createTable;
import static com.datastax.oss.driver.api.querybuilder.SchemaBuilder.dropTable;

public class RentRepository {

    private final RentDao rentDao;

    public RentRepository(CqlSession session) {
        session.execute(dropTable(GuesthouseFinals.RENTS_BY_ROOM).ifExists().build());
        session.execute(dropTable(GuesthouseFinals.RENTS_BY_CLIENT).ifExists().build());
        session.execute(createRentByRoomTableStatement());
        session.execute(createRentByClientTableStatement());

        RentMapper rentMapper = new RentMapperBuilder(session).build();
        rentDao = rentMapper.rentDao();
    }

    private SimpleStatement createRentByRoomTableStatement() {
        return createTable(GuesthouseFinals.RENTS_BY_ROOM)
                .ifNotExists()
                .withPartitionKey(GuesthouseFinals.ROOM_NUMBER, DataTypes.INT)
                .withClusteringColumn(GuesthouseFinals.RENT_BEGIN_DATE, DataTypes.DATE)
                .withClusteringColumn(GuesthouseFinals.RENT_END_DATE, DataTypes.DATE)
                .withColumn(GuesthouseFinals.CLIENT_PERSONAL_ID, DataTypes.TEXT)
                .withColumn(GuesthouseFinals.RENT_COST, DataTypes.DOUBLE)
                .withColumn(GuesthouseFinals.RENT_BOARD, DataTypes.BOOLEAN)
                .build();
    }

    private SimpleStatement createRentByClientTableStatement() {
        return createTable(GuesthouseFinals.RENTS_BY_CLIENT)
                .ifNotExists()
                .withPartitionKey(GuesthouseFinals.CLIENT_PERSONAL_ID, DataTypes.TEXT)
                .withClusteringColumn(GuesthouseFinals.RENT_BEGIN_DATE, DataTypes.DATE)
                .withClusteringColumn(GuesthouseFinals.RENT_END_DATE, DataTypes.DATE)
                .withColumn(GuesthouseFinals.ROOM_NUMBER, DataTypes.INT)
                .withColumn(GuesthouseFinals.RENT_COST, DataTypes.DOUBLE)
                .withColumn(GuesthouseFinals.RENT_BOARD, DataTypes.BOOLEAN)
                .build();
    }


    public boolean add(Rent rent) {

        if (isColliding(rent.getBeginTime(), rent.getEndTime(), rent.getRoom().getRoomNumber())) {
            return false;
        }
        return rentDao.create(rent);
    }

    //
    public void remove(Rent rent) {
        rentDao.remove(rent);
    }


    public List<Rent> getAll() {
        return rentDao.findAll();
    }

    public List<Rent> getByRoomNumber(int roomNumber) {
        return rentDao.findByRoomNumber(roomNumber);
    }

    public List<Rent> getByClientPersonalId(String personalId) {
        return rentDao.findByClientPersonalId(personalId);
    }


    public void update(Rent rent) {
        rentDao.update(rent);
    }

    public boolean isColliding(LocalDate beginDate, LocalDate endDate, int roomNumber) {

        List<Rent> rents = getByRoomNumber(roomNumber);
        return rents.stream().anyMatch((rent ->
                        (rent.getBeginTime().isAfter(beginDate) && rent.getBeginTime().isBefore(endDate)) ||
                                (rent.getEndTime().isAfter(beginDate) && rent.getEndTime().isBefore(endDate)) ||
                                (rent.getBeginTime().isBefore(beginDate) && rent.getEndTime().isAfter(endDate)) ||
                                (rent.getBeginTime().isEqual(beginDate) || rent.getEndTime().isEqual(endDate))
                )
        );
    }
//
//    public boolean updateIsBeingRented(int roomNumber, boolean increment) {
//        Bson filter = Filters.eq("number", roomNumber);
//        Bson updateIsBeingRented;
//        if (increment) {
//            updateIsBeingRented = Updates.inc("is_being_rented", 1);
//        } else {
//            updateIsBeingRented = Updates.set("is_being_rented", 0);
//        }
//        try {
//            return roomCollection.updateOne(filter, updateIsBeingRented).wasAcknowledged();
//        } catch (MongoWriteException e) {
//            return false;
//        }
//    }

}
