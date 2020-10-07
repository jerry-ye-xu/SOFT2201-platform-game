package stickman.model;

import org.json.simple.JSONObject;

public interface EntityFactory {
    Entity createEntity(String entityType, JSONObject entityProperties);
}
