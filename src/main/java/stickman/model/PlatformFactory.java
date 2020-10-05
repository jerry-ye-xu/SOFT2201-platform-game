package stickman.model;

import org.json.simple.JSONObject;

public interface PlatformFactory {
    Platform createPlatform(JSONObject platformProperties);
}
