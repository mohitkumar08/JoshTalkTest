package com.bit.joshtalktest.utils;

import java.util.HashMap;
import java.util.Map;

public interface Constants {
    Map<Integer, String> mapPage = new HashMap() {{
        put(1, "59b3f0b0100000e30b236b7e");
        put(2, "59ac28a9100000ce0bf9c236");
        put(3, "59ac293b100000d60bf9c239");
    }};
    String FEED_TIME_FORMAT = "HH:mm a  dd MMM yy";

    enum SortSelection {
        DATE("Date"), LIKE("Like"), SHARE("Share"), VIEW("View");
        String type;

        SortSelection(final String type) {
            this.type = type;
        }

        public String getType() {
            return type;
        }

        public void setType(final String type) {
            this.type = type;
        }
    }

}
