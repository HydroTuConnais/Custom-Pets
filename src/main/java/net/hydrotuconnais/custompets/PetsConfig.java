package net.hydrotuconnais.custompets;

public class PetsConfig {
    private final String name;
    private final int primaryColor;
    private final int secondaryColor;

    public PetsConfig(String name, int primaryColor, int secondaryColor) {
        this.name = name;
        this.primaryColor = primaryColor;
        this.secondaryColor = secondaryColor;
    }

    public String getName() { return name; }
    public int getPrimaryColor() { return primaryColor; }
    public int getSecondaryColor() { return secondaryColor; }
}
