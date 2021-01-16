package pl.wtarsa.elevatorSystem;

import java.util.List;

public interface ElevatorSystem {

    void pickup(int pickupFloor, int direction);

    void update(int id, int floor, int destinationFloor);

    void step() throws InterruptedException;

    List<ElevatorStatus> status();
}
