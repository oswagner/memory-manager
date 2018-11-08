package entity;

public enum MemoryRequestType {
    S("S"),
    L("L");

    private final String value;

    /**
     * @param value
     */
    private MemoryRequestType(final String value) {
        this.value = value;
    }

    public String getName() {
        return value;
    }
}