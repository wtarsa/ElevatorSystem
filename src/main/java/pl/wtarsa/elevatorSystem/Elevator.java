package pl.wtarsa.elevatorSystem;

import lombok.Data;

import java.util.LinkedList;
import java.util.Queue;


@Data
public class Elevator {

    public Elevator(int id, ElevatorStatus currentStatus) {
        this.id = id;
        this.currentStatus = currentStatus;
        this.direction = Direction.NOMOVE;
    }

    private int id;

    private ElevatorStatus currentStatus;

    private ElevatorTask currentTask;

    private Direction direction;

    private Queue<ElevatorTask> tasks = new LinkedList<>();

    public void addTask(ElevatorTask task) {
        tasks.add(task);
    }


    /**
     * Destination floor is changing, because at the beginning is equal to pickup floor,
     * after pickup - destination floor is equal to floor given by user.
     *
     * @return current destination floor
     */
    public int getDestinationFloor() {
        if (currentTask.getPickupFloor() == currentStatus.getCurrentFloor() && !currentTask.isPicked()) {
            chooseDestinationFloor();
            resume();
            return currentTask.getDestinationFloor();
        } else if (currentTask.isPicked()) {
            return currentTask.getDestinationFloor();
        } else return currentTask.getPickupFloor();
    }

    /**
     * Change current task and elevator's direction.
     *
     * @param currentTask
     */
    public void setCurrentTask(ElevatorTask currentTask) {
        this.currentTask = currentTask;
        this.direction =
                currentStatus.getCurrentFloor() - currentTask.getPickupFloor() > 0 ? Direction.DOWN : Direction.UP;
        currentTask.setPickupDirection(direction);
    }

    /**
     * Get destination floor from user and check if it is correct.
     */
    private void chooseDestinationFloor() {
        currentTask.setPicked(true);
        stop();
        int floor = -10;
        while (floor == -10 || !isDestinationFloorCorrect(floor)) {
            System.out.println("[Elevator " + id + "] Choose destination floor: ");
            floor = ConsoleUI.getDestinationFloor();
        }
        currentTask.setDestinationFloor(floor);
        System.out.println("[Elevator " + id + "] Chosen destination floor: " + currentTask.getDestinationFloor());
    }

    public void stop() {
        direction = Direction.NOMOVE;
    }

    /**
     * Check if floor given by user is correct. Floor must be compatible with direction given by user at the beginning.
     *
     * @param chosenDestinationFloor direction given by user to check
     * @return true if destination floor given by user is equal with initial direction
     */
    private boolean isDestinationFloorCorrect(int chosenDestinationFloor) {
        return (currentTask.getDestinationDirection() == Direction.DOWN &&
                currentTask.getPickupFloor() - chosenDestinationFloor > 0) ||
                (currentTask.getDestinationDirection() == Direction.UP &&
                        currentTask.getPickupFloor() - chosenDestinationFloor < 0);
    }

    /**
     * Resume the elevator after choosing destination floor.
     */
    private void resume() {
        direction = currentTask.getDestinationDirection();
        currentStatus.setDestinationFloor(currentTask.getDestinationFloor());
        currentStatus.setCurrentFloor(currentTask.getPickupFloor());
    }
}