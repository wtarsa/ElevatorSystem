package pl.wtarsa.elevatorSystem;

import java.nio.BufferOverflowException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class ConsoleUI {

    private static int destinationFloor;

    private static boolean changed = false;

    private ElevatorSystem elevatorSystem;

    public ConsoleUI() {
    }

    public void setElevatorSystem(ElevatorSystem elevatorSystem) {
        this.elevatorSystem = elevatorSystem;
    }

    /**
     * get number of elevators and elevator's starting floors from user
     *
     * @return list of elevator's starting floors
     */
    public List<Integer> initialize() {
        List<Integer> positions = new ArrayList<>();
        System.out.println("Enter number of elevators: ");
        Scanner in = new Scanner(System.in);
        int numberOfElevators = in.nextInt();
        for (int i = 0; i < numberOfElevators; i++) {
            System.out.println("Enter starting floor for elevator number " + i + ":");
            positions.add(in.nextInt());
        }
        in.reset();
        return positions;
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String command = scanner.nextLine();
            List<String> commandDivided = Arrays.asList(command
                    .trim()
                    .replaceAll(" +", " ")
                    .split(" "));
            switch (commandDivided.get(0)) {
                case "pickup":
                    elevatorSystem.pickup(
                            Integer.parseInt(commandDivided.get(1)),
                            Integer.parseInt(commandDivided.get(2)));
                    break;
                case "status":
                    elevatorSystem.status().forEach(status -> System.out.println(status.toString()));
                    break;
                case "update":
                    elevatorSystem.update(
                            Integer.parseInt(commandDivided.get(1)),
                            Integer.parseInt(commandDivided.get(2)),
                            Integer.parseInt(commandDivided.get(3)));
                    break;
                default:
                    if (isNumeric(commandDivided.get(0))) {
                        destinationFloor = Integer.parseInt(commandDivided.get(0));
                        changed = true;
                    } else {
                        System.out.println("Undefined command.");
                    }
                    break;
            }
        }
    }

    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Integer.parseInt(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    public static int getDestinationFloor() {
        try {
            while (!changed) {
                TimeUnit.MILLISECONDS.sleep(500);
            }
            changed = false;
        } catch (InterruptedException e) {
            return -1;
        }
        return destinationFloor;
    }
}
