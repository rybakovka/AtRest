package pflb.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pflb.json.Deserializer.AuthErrorDeserializer;
import pflb.json.Serializers.AuthSerializer;
import pflb.json.Serializers.EmptyReqSerializer;
import pflb.json.Serializers.InfoSerializer;
import pflb.entity.User;

public class CustomGsonBuilder {

    private final Logger logger = LogManager.getLogger(this.getClass());

    protected Gson AuthErrorFromJson() {
        logger.info("User data deserialized");
        return new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(User.class, new AuthErrorDeserializer())
                .create();
    }

    protected Gson EmptyReqJson() {
        logger.info("Error request from server serialized");
        return new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(User.class, new EmptyReqSerializer())
                .create();
    }

    protected Gson AuthReqJson() {
        logger.info("Login request from server serialized");
        return new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(User.class, new AuthSerializer())
                .create();
    }

    protected Gson InfoReqJson() {
        logger.info("User info in request from server serialized");
        return new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(User.class, new InfoSerializer())
                .create();
    }
}