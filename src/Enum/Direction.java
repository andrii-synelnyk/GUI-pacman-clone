package Enum;

public enum Direction {
    UP(90),
    DOWN(-90),
    LEFT(180),
    RIGHT(0);

    private final int directionMultiplier;

    Direction(int directionMultiplier){
        this.directionMultiplier = directionMultiplier;
    }

    public int getDirectionMultiplier(){
        return directionMultiplier;
    }
}
