package net.mrchar.zzplant.model;

public enum Gender {
    MALE, FEMALE;

    public static Gender fromString(String gender) {
        return switch (gender.toUpperCase()) {
            case "MALE" -> MALE;
            case "FEMALE" -> FEMALE;
            default -> null;
        };
    }
}
