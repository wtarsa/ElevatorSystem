package pl.wtarsa.elevatorSystem;

import java.util.List;

public class Runner {

    public static void main(String[] args) {
        ConsoleUI ui = new ConsoleUI();
        List<Integer> positions = ui.initialize();
        SimpleElevatorSystem elevatorSystem = new SimpleElevatorSystem(positions.size(), positions);
        ui.setElevatorSystem(elevatorSystem);
        ui.run();
    }
}
