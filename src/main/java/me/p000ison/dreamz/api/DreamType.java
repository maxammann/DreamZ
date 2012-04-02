package me.p000ison.dreamz.api;

/**
 *
 * @author p000ison
 */
public enum DreamType {

    DREAMWORLD(1),
    NIGHTMARE(2),
    NOTHING(0);
    private int value;

    private DreamType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static boolean isNightMare() {
        for (DreamType dtest : DreamType.values()) {
            if (dtest.getValue() == 2) {
                return true;
            }
        }
        return false;
    }

    public static boolean isDreamWorld() {
        for (DreamType dtest : DreamType.values()) {
            if (dtest.getValue() == 1) {
                return true;
            }
        }
        return false;
    }
}