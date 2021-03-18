package io.github.agentrkid.rabbit.shared.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ParseUtil {
    public int parseInteger(String str) {
        int count = 0;
        try {
            count = Integer.parseInt(str);
        } catch (Exception ex) {
            count = -1;
        }
        return count;
    }

    public boolean parseBoolean(String str) {
        boolean bool;
        try {
            bool = Boolean.parseBoolean(str);
        } catch (Exception ignored) {
            bool = false;
        }
        return bool;
    }
}
