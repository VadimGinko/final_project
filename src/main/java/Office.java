import enums.DoorState;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Slf4j
public class Office extends Thread {
    private List<Floor> floors;
    private volatile List<Elevator> elevators;
    private ArrayDeque<Integer> queue = new ArrayDeque<>();

    private Office(int floorsNumber, int elevatorsNumber) {
        this.floors = createListOfFloors(floorsNumber);
        this.elevators = createListOfElevators(elevatorsNumber);
    }

    public static Office of(int floorsNumber, int elevatorsNumber){
        return new Office(floorsNumber, elevatorsNumber);
    }

    @SneakyThrows
    @Override
    public void run() {
        while (true) {
            int floorsSize = floors.size();
            Human man1 = Human.ofRandom(floorsSize);
            int humanRightFloor = man1.getRightFloor();
            int humanFloor = man1.getFloor();
            var floor = floors.get(humanFloor - 1);
            floor.addHuman(man1);
            if (!floor.isButtonsPressed()) {
                floor.pressElevatorButton(humanRightFloor);
                this.queue.add(humanFloor);
                log.info("Человек {} встал в очередь к лифту и нажал кнопку {} очередь этажей на которых запрошен лифт {}", man1, floor, this.queue.toString());
            }else{
                log.info("Человек {} встал в очередь к лифту и не нажал кнопку {} очередь этажей на которых запрошен лифт {}", man1, floor, this.queue.toString());
            }
            sleep(8000);
        }
    }

    @SneakyThrows
    public void handle() {
        int number = 1;
        for (var elev : elevators) {
            elev.setName(String.format("Лифт %s",number));
            elev.start();
            number += 1;
        }
        while (true) {
            for (var elev : elevators) {
                if (!elev.isCertainFloorBe() && !queue.isEmpty() && elev.humans.isEmpty() && elev.isFree()) {
                    log.info("лифт {} вызван на этаж {}", elev.getIdElevator(), queue.getFirst());
                    elev.setCertainFloor(queue.removeFirst());
                    continue;
                }
                if (!elev.getState().toString().equals("TIMED_WAITING") && !elev.isFree()) {
                    var suchFloorNumber = elev.getFloor();
                    var floor = floors.get(suchFloorNumber - 1);
                    if (floor.isButtonsPressed()) {
                        if (elev.isEmpty()) {
                            if(elev.getDoorState()!= DoorState.OPEN) {
                                if(elev.getDoorState()!= DoorState.OPENS) {
                                    elev.setDoorState(DoorState.OPENS);
                                }
                                continue;
                            }
                            this.pickUpPeopleFromFloor(elev, floor, suchFloorNumber);
                            elev.setDoorState(DoorState.CLOSES);
                        }else{
                            if (elev.suchElevatorDirection() == floor.pressedButtonDirection()) {
                                if(elev.getDoorState()!= DoorState.OPEN) {
                                    elev.setDoorState(DoorState.OPENS);
                                    continue;
                                }
                                elev.releaseHumansByFloorNumber();
                                this.pickUpPeopleFromFloor(elev, floor, suchFloorNumber);
                                elev.setDoorState(DoorState.CLOSES);
                            } else {
                                if(elev.nextHumanFloor() == suchFloorNumber){
                                    if(elev.getDoorState()!= DoorState.OPEN) {
                                        if(elev.getDoorState()!= DoorState.OPENS) {
                                            elev.setDoorState(DoorState.OPENS);
                                        }
                                        continue;
                                    }
                                    elev.releaseHumansByFloorNumber();
                                    elev.setDoorState(DoorState.CLOSES);
                                }
                            }
                        }
                    }else{
                        suchFloorNumber = elev.getFloor();
                        if(elev.nextHumanFloor() == suchFloorNumber){
                            if(elev.getDoorState()!= DoorState.OPEN) {
                                if(elev.getDoorState()!= DoorState.OPENS) {
                                    elev.setDoorState(DoorState.OPENS);
                                }
                                continue;
                            }
                            elev.releaseHumansByFloorNumber();
                            elev.setDoorState(DoorState.CLOSES);
                        }else {
                            floor = floors.get(suchFloorNumber - 1);
                            if (elev.isEmpty() && elev.nextHumanFloor() == -1
                                    && elev.getDoorState() == DoorState.CLOSED && floor.isEmpty()) {
                                elev.doElevFree();
                            }
                        }
                    }
                }
            }
        }
    }

    public void pickUpPeopleFromFloor(Elevator elev, Floor floor, int suchFloorNumber){
        int freeSpaceInElevator = elev.getFreeSpace();
        var humans = floor.getHumansInElevator(freeSpaceInElevator);
        if (humans != null) {
            elev.setHumans(humans);
        }
        queue.remove(suchFloorNumber);
        floor.unPressButtons();
        floor.pressButtonIfPeopleOnFloorNotEmpty();
    }

    public List<Floor> getFloors() {
        return new ArrayList<>(floors);
    }

    public List<Elevator> getElevators() {
        return new ArrayList<>(elevators);
    }

    private List<Floor> createListOfFloors(int floorsNumber) {
        return IntStream.range(1, floorsNumber + 1)
                .mapToObj(Floor::of)
                .collect(Collectors.toList());
    }

    private List<Elevator> createListOfElevators(int elevatorsNumber) {
        List<Elevator> elevators = new ArrayList<>();
        for (var i = 0; i < elevatorsNumber; i++) {
            elevators.add(Elevator.of(2000, 500, 500));
        }
        return elevators;
    }
}