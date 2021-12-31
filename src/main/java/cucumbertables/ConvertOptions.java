package cucumbertables;

public enum ConvertOptions {
    ERROR_ON_NO_MATCHING_NAME(0b01),
    UNDERSCORE_TO_CAMELCASE(0b10);

    private int value;

    ConvertOptions(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public boolean match(int optionValue) {
        return (this.value & optionValue) != 0;
    }
}
