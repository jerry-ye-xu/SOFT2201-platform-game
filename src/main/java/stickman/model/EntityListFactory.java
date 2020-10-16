package stickman.model;

import org.json.simple.JSONObject;

import java.util.List;

public interface EntityListFactory {
    List<Entity> createPlatform(String platformType, JSONObject platformProperties);
}
