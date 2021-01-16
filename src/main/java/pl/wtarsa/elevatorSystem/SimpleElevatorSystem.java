package pl.wtarsa.elevatorSystem;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static java.lang.Math.abs;

public class SimpleElevatorSystem implements ElevatorSystem {

    private final List<Elevator> elevators;

    public SimpleElevatorSystem(int numberOfElevators, List<Integer> positions) {
        elevators = new ArrayList<>();
        initializeElevators(numberOfElevators, positions);
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
        executorService.scheduleAtFixedRate(this::step, 1, 2, TimeUnit.SECONDS);
    }

    private void initializeElevators(int numberOfElevators, List<Integer> positions) {
        for (int i = 0; i < numberOfElevators; i++) {
            elevators.add(new Elevator(i, new ElevatorStatus(i, positions.get(i), positions.get(i))));
        }
    }

    public void pickup(int pickupFloor, int direction) {
        Direction destinationDirection = direction > 0 ? Direction.UP : Direction.DOWN;
        Elevator chosen = chooseElevator(pickupFloor);
        chosen.addTask(new ElevatorTask(pickupFloor, -1, null, destinationDirection, false));
    }

    /**
     * Check if elevator has ended current task. If so, stop it.
     *
     * @param id               elevator's id
     * @param floor            elevator's current floor
     * @param destinationFloor elevator's destination floor
     */
    public void update(int id, int floor, int destinationFloor) {
        Elevator chosen = elevators.stream()
                .filter(elevator -> elevator.getId() == id)
                .findFirst().orElse(null);
        if (chosen == null) throw new RuntimeException("An elevator with provided id does not exist.");

        if (chosen.getCurrentTask().getDestinationFloor() == chosen.getCurrentStatus().getCurrentFloor() &&
                chosen.getCurrentTask().isPicked()) {
            chosen.getTasks().remove();             // delete finished tasks
            chosen.stop();                          // stop the elevator
            System.out.println("[Elevator " + id + "] task completed.");
        }
    }

    /**
     * Make a step for each running elevator.
     * Run non-running elevator with tasks.
     * Update each elevator with tasks.
     */
    public void step() {
        elevators.stream() // if an elevator is not running, but it has task - run first task from queue on it
                .filter(elevator -> elevator.getDirection() == Direction.NOMOVE)
                .filter(elevator -> elevator.getTasks().size() > 0)
                .forEach(elevator -> elevator.setCurrentTask(elevator.getTasks().element()));

        elevators.stream() // for each elevator with at least one task - check if it ends current task
                .filter(elevator -> elevator.getTasks().size() > 0)
                .forEach(e -> update(
                        e.getId(),
                        e.getCurrentStatus().getCurrentFloor(),
                        e.getDestinationFloor()));

        elevators.stream() // make a step for each moving elevator
                .filter(elevator -> elevator.getDirection() != Direction.NOMOVE)
                .forEach(e -> e.setCurrentStatus(new ElevatorStatus(
                        e.getId(),
                        e.getCurrentStatus().getCurrentFloor() + e.getDirection().getDirectionCode(),
                        e.getDestinationFloor()
                )));
    }

    public List<ElevatorStatus> status() {
        return elevators.stream()
                .map(Elevator::getCurrentStatus)
                .collect(Collectors.toList());
    }

    /**
     * Choose elevator after getting "pickup" call
     *
     * @param pickupFloor pickup floor
     * @return elevator which is responsible for pickup
     */
    private Elevator chooseElevator(int pickupFloor) {
        List<Elevator> availableElevators = elevators.stream()
                .filter(elevator -> elevator.getDirection() == Direction.NOMOVE)
                .collect(Collectors.toList()); // choose list of elevators without tasks
        if (availableElevators.size() == 0) {
            return elevators.stream()
                    .min(Comparator.comparing(elevator -> elevator.getTasks().size())) // if all elevators have tasks -
                    // then delegate task to the elevator with minimal amount of tasks
                    .orElse(elevators.get(0));
        } else {
            Elevator closest = availableElevators.get(0);
            for (Elevator elevator : availableElevators) { // choose the closest elevator
                if (getFloorDifference(elevator, pickupFloor) < getFloorDifference(closest, pickupFloor)) {
                    closest = elevator;
                }
            }
            return closest;
        }
    }

    private int getFloorDifference(Elevator elevator, int pickupFloor) {
        return abs(elevator.getCurrentStatus().getCurrentFloor() - pickupFloor);
    }
}
