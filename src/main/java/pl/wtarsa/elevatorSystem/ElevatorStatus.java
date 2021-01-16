package pl.wtarsa.elevatorSystem;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ElevatorStatus {

    private int id;

    private int currentFloor;

    private int destinationFloor;

    public String toString() {
        return "id: " + id + " current floor: " + currentFloor + " destination floor: " + destinationFloor;
    }
}
