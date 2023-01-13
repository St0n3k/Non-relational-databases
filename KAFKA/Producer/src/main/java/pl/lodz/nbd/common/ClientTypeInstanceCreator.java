package pl.lodz.nbd.common;

import com.google.gson.*;
import pl.lodz.nbd.model.ClientTypes.ClientType;

import java.lang.reflect.Type;

public class ClientTypeInstanceCreator implements JsonSerializer<ClientType>, JsonDeserializer<ClientType> {

    private static final String CLASS_META_KEY = "_clazz";

    @Override
    public ClientType deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject jsonObj = jsonElement.getAsJsonObject();
        String className = jsonObj.get(CLASS_META_KEY).getAsString();
        try {
            Class<?> clz = Class.forName(className);
            return jsonDeserializationContext.deserialize(jsonElement, clz);
        } catch (ClassNotFoundException e) {
            throw new JsonParseException(e);
        }
    }

    @Override
    public JsonElement serialize(ClientType clientType, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonElement jsonEle = jsonSerializationContext.serialize(clientType, clientType.getClass());
        jsonEle.getAsJsonObject().addProperty(CLASS_META_KEY,
                clientType.getClass().getCanonicalName());
        return jsonEle;
    }
}
