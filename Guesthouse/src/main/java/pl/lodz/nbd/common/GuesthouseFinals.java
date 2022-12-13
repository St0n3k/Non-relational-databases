package pl.lodz.nbd.common;

import com.datastax.oss.driver.api.core.CqlIdentifier;

public class GuesthouseFinals {
    public static final CqlIdentifier GUESTHOUSE_NAMESPACE = CqlIdentifier.fromCql("guesthouse");
    public static final CqlIdentifier ROOMS = CqlIdentifier.fromCql("rooms");
    public static final CqlIdentifier ROOM_NUMBER = CqlIdentifier.fromCql("room_number");
    public static final CqlIdentifier ROOM_SIZE = CqlIdentifier.fromCql("size");
    public static final CqlIdentifier ROOM_PRICE = CqlIdentifier.fromCql("price");

    public static final CqlIdentifier CLIENT_TYPES = CqlIdentifier.fromCql("client_types");
    public static final CqlIdentifier CLIENT_TYPE_DISCOUNT = CqlIdentifier.fromCql("discount");
    public static final CqlIdentifier CLIENT_TYPE_DISCRIMINATOR = CqlIdentifier.fromCql("discriminator");

    public static final CqlIdentifier CLIENTS = CqlIdentifier.fromCql("clients");
    public static final CqlIdentifier CLIENT_PERSONAL_ID = CqlIdentifier.fromCql("personal_id");
    public static final CqlIdentifier CLIENT_FIRST_NAME = CqlIdentifier.fromCql("first_name");
    public static final CqlIdentifier CLIENT_LAST_NAME = CqlIdentifier.fromCql("last_name");
    public static final CqlIdentifier CLIENT_TYPE = CqlIdentifier.fromCql("client_type");

    public static final CqlIdentifier RENTS_BY_ROOM = CqlIdentifier.fromCql("rents_by_room");
    public static final CqlIdentifier RENTS_BY_CLIENT = CqlIdentifier.fromCql("rents_by_client");
    public static final CqlIdentifier RENT_BEGIN_DATE = CqlIdentifier.fromCql("begin_date");
    public static final CqlIdentifier RENT_END_DATE = CqlIdentifier.fromCql("end_date");
    public static final CqlIdentifier RENT_BOARD = CqlIdentifier.fromCql("board");
    public static final CqlIdentifier RENT_COST = CqlIdentifier.fromCql("cost");

}
