package pl.lodz.nbd.common;

import com.datastax.oss.driver.api.core.CqlIdentifier;

public class GuesthouseFinals {
    public static final CqlIdentifier GUESTHOUSE_NAMESPACE = CqlIdentifier.fromCql("guesthouse");
    public static final CqlIdentifier ROOMS = CqlIdentifier.fromCql("rooms");
    public static final CqlIdentifier ROOM_NUMBER = CqlIdentifier.fromCql("room_number");
    public static final CqlIdentifier ROOM_SIZE = CqlIdentifier.fromCql("size");
    public static final CqlIdentifier ROOM_PRICE = CqlIdentifier.fromCql("price");
}
