package stickman.model;

import org.json.simple.JSONObject;

import java.util.List;

public interface EntityFactoryPlatform {
    List<Entity> createPlatform(String platformType, JSONObject platformProperties);
}
