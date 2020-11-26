package uhk.fim.actions;

public enum CartFormat {
    CSV("csv"),
    JSON("json");
    //TODO add more

    private final String text;

    CartFormat(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}
