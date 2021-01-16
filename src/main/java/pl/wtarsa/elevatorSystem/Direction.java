package pl.wtarsa.elevatorSystem;

public enum Direction {
    UP(1),
    DOWN(-1),
    NOMOVE(0);

    private final int directionCode;

    Direction(int directionCode) {
        this.directionCode = directionCode;
    }

    public int getDirectionCode() {
        return this.directionCode;
    }
}
