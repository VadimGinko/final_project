import com.google.common.base.Objects;
import enums.Direction;
import lombok.Getter;
import java.util.Comparator;
import java.util.Random;
import java.util.UUID;
import static com.google.common.base.Preconditions.checkArgument;


public class Human {
    @Getter
    private final UUID id;
    private final int maxWeight = 100;
    @Getter
    private int weight;
    @Getter
    private int rightFloor;
    private final int officeSize;
    @Getter
    private int floor;
    public static final Comparator<Human> COMPARE_BY_RIGHT_FLOOR = Comparator.comparingInt(Human::getRightFloor);

    public void setWeight(int weight) {
        checkArgument(weight >= 1 && weight <= this.maxWeight,  String.format("weight cannot be less than 1 and no higher than %s", this.maxWeight));
        this.weight = weight;
    }

    public void setRightFloor(int rightFloor) {
        checkArgument(rightFloor >= 1 && rightFloor <= this.officeSize && rightFloor != this.floor,  String.format("rightFloor cannot be less than 1 and no higher than %s", this.officeSize));
        this.rightFloor = rightFloor;
    }

    public void setFloor(int floor) {
        checkArgument(floor >= 1 && floor <= this.officeSize, String.format("floor cannot be less than 1 and no higher than %s", this.officeSize));
        this.floor = floor;
    }

    private Human(int weight, int rightFloor, int floor, int officeSize) {
        checkArgument(weight >= 1 && rightFloor <= this.maxWeight,  String.format("weight cannot be less than 1 and no higher than %s", this.maxWeight));
        checkArgument(rightFloor > 0 && rightFloor <= officeSize,  String.format("rightFloor cannot be less than 1 and no higher than %s", officeSize));
        checkArgument(floor >= 1 && floor <= officeSize, String.format("floor cannot be less than 1 and no higher than %s", officeSize));
        this.weight = weight;
        this.rightFloor = rightFloor;
        this.floor = floor;
        this.officeSize = officeSize;
        this.id = UUID.randomUUID();
    }

    public static Human ofRandom(int officeSize){
        Random random = new Random();
        int rWeight = random.nextInt(100) + 25;
        int rFloor = random.nextInt(officeSize) + 1;
        int rRightFloor;
        do {
            rRightFloor = random.nextInt(officeSize) + 1;
        } while (rRightFloor == rFloor);
        return new Human(rWeight, rRightFloor, rFloor, officeSize);
    }

    //for tests
    public static Human of(int weight, int rightFloor, int floor, int officeSize){
        return new Human(weight, rightFloor, floor, officeSize);
    }

    public Direction suchHumanDirection(){
        if(rightFloor > floor){
            return Direction.Up;
        }else{
            return Direction.Down;
        }
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("id", id)
                .add("weight", weight)
                .add("rightFloor", rightFloor)
                .add("floor", floor)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Human human = (Human) o;
        return Objects.equal(id, human.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
