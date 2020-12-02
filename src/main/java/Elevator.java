import com.google.common.base.Objects;
import enums.Direction;
import enums.DoorState;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkArgument;

@Slf4j
public class Elevator extends Thread{
    @Getter
    private final UUID idElevator;
    private final int timeLift; // время прохождения одного этажа лифтом
    private final int timeOpenOrClosed; //время открытия/закрытая лифта

    @Setter
    @Getter
    private int floor;
    private int certainFloor;
    private final int capacity;

    @Setter
    @Getter
    private DoorState doorState;
    private boolean goToCertainFloor;
    @Getter
    private boolean isFree;

    List<Human> humans = new ArrayList<>();

    private Elevator(int timeLift, int timeOpenOrClosed, int capacity){
        this.timeLift = timeLift;
        this.timeOpenOrClosed = timeOpenOrClosed;
        this.capacity = capacity;
        this.idElevator = UUID.randomUUID();

        this.floor = 1;
        this.certainFloor = -1;
        this.doorState = DoorState.CLOSED;
        this.goToCertainFloor = false;
        this.isFree = true;
    }

    public static Elevator of(int timeLift, int timeOpenOrClosed, int capacity){
        checkArgument(timeLift >= 1 , "timeLift cannot be less than 1");
        checkArgument(timeOpenOrClosed >= 1 , "timeOpenOrClosed cannot be less than 1");
        checkArgument(capacity >= 50, "capacity cannot be less than 50");
        return new Elevator(timeLift, timeOpenOrClosed, capacity);
    }

    @SneakyThrows
    @Override
    public void run() {
        log.info("лифт запущен");
        while(true){
            if(goToCertainFloor){
                log.info("Лифт отправляется с {} на {}", this.floor, this.certainFloor);
                sleep(timeLift * (Math.abs(this.certainFloor - this.floor)));
                this.isFree = false;
                goToCertainFloor = false;
                log.info("Лифт прибыл на {}", this.certainFloor);
                floor = certainFloor;
                certainFloor = -1;
            }
            if(doorState == DoorState.CLOSED && !humans.isEmpty()){
                if(floor != nextHumanFloor())
                    log.info("next floor {}",nextHumanFloor());
                if(floor > nextHumanFloor()){
                    floor--;
                    log.info("Лифт едет " + floor);
                    sleep(timeLift);
                }
                if(floor < nextHumanFloor()){
                    floor++;
                    log.info("Лифт едет " + floor);
                    sleep(timeLift);
                }
            }
            if(doorState == DoorState.OPENS ){
                log.info("Лифт открывает двери");
                sleep(timeOpenOrClosed);
                this.doorState = DoorState.OPEN;
                log.info("дверь открыта");
            }
            if(doorState == DoorState.CLOSES ){
                log.info("Лифт закрывает двери");
                sleep(timeOpenOrClosed);
                if(isEmpty()) {
                    this.isFree = true;
                }
                this.doorState = DoorState.CLOSED;
                log.info(humans.toString());
                log.info("дверь закрыта");
            }
        }
    }

    public boolean isCertainFloorBe(){
        return this.certainFloor != -1;
    }

    public void setCertainFloor(int certainFloor) {
        this.certainFloor = certainFloor;
        this.goToCertainFloor = true;
    }

    public void setHumans(List<Human> humans) {
        checkArgument(humans != null, "humans cannot be null");
        this.humans.addAll(humans);
        //this.sortHumans();
    }

    public List<Human> releaseHumansByFloorNumber() {
        List<Human> releaseList = humans.stream()
                                        .filter(x->x.getRightFloor() == this.floor)
                                        .collect(Collectors.toList());
        this.humans.removeAll(releaseList);
        return releaseList;
    }

    public int nextHumanFloor(){
        if(this.humans.isEmpty())
            return -1; //return 1
        else
            return humans.get(0).getRightFloor();
    }

    public int getFreeSpace(){
        return this.capacity - this.humans.stream().mapToInt(Human::getWeight).sum();
    }

    public Direction suchElevatorDirection(){
        if(this.floor <= nextHumanFloor()){
            return Direction.Up;
        }else{
            return Direction.Down;
        }
    }

    public boolean isEmpty() {
        return this.humans.isEmpty();
    }

//    public void sortHumans(){
//        humans.sort(Human.COMPARE_BY_RIGHT_FLOOR);
//    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("idElevator", idElevator)
                .add("floor", floor)
                .add("doorState", doorState)
                .add("humans", humans)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Elevator elevator = (Elevator) o;
        return timeLift == elevator.timeLift &&
                timeOpenOrClosed == elevator.timeOpenOrClosed &&
                Objects.equal(idElevator, elevator.idElevator);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(idElevator, timeLift, timeOpenOrClosed);
    }
}