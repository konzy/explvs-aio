package org.aio.gui;

import org.json.simple.JSONObject;

public interface JSONSerializable {
    JSONObject toJSON();
    void fromJSON(final JSONObject jsonObject);
}
