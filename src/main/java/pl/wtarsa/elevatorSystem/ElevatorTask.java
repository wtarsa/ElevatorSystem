package pl.wtarsa.elevatorSystem;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ElevatorTask {

    private final int pickupFloor;

    private int destinationFloor;

    private Direction pickupDirection;

    private Direction destinationDirection;

    private boolean picked = false;

    public void setPicked(boolean picked) {
        this.picked = picked;
    }

    public void setDestinationFloor(int destinationFloor) {
        this.destinationFloor = destinationFloor;
    }

    public void setPickupDirection(Direction pickupDirection) {
        this.pickupDirection = pickupDirection;
    }
}
