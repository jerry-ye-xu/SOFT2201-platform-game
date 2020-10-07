package stickman.view;

import org.json.simple.JSONObject;

public interface PlatformFactory {
    Platform createPlatform(String platformType, JSONObject platformProperties);
}
