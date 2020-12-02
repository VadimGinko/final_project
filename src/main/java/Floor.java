import com.google.common.base.Objects;
import enums.ButtonState;
import enums.Direction;
import lombok.Getter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import static com.google.common.base.Preconditions.checkArgument;


public class Floor {
    @Getter
    private final int floorNumber;
    private ButtonState buttonUp;
    private ButtonState buttonDown;

    List<Human> humans = new ArrayList<>();

    private Floor(int floorNumber) {
        checkArgument(floorNumber >= 1, "floor cannot be less than one");
        this.floorNumber = floorNumber;
        unPressButtons();
    }

    public static Floor of(int floorNumber){
        return new Floor(floorNumber);
    }

    public void addHuman(Human human) {
        checkArgument(human != null, "human cannot be null");
        humans.add(human);
    }

    public List<Human> getHumansInElevator(int freeSpace){
        List<Human> list = new ArrayList<>();
//        List<Human> list;
//        list = humans.stream()
//                       .filter(x -> freeSpace >= x.getWeight() &&
//                               x.suchHumanDirection() == this.pressedButtonDirection())
//                      .collect(Collectors.toList());
        for(var i: humans){
            if(freeSpace >= i.getWeight() && i.suchHumanDirection() == this.pressedButtonDirection()){
                list.add(i);
            }

        }
        humans.removeAll(list);
        return list;
    }


    public void pressElevatorButton(int rightFloorNumber){
        if(rightFloorNumber > floorNumber){
            this.pressElevatorButtonUp();
        }else{
            this.pressElevatorButtonDown();
        }
    }

    public Direction pressedButtonDirection(){
        if(this.buttonUp == ButtonState.ON)
            return Direction.Up;
        else
            return Direction.Down;
    }

    public boolean isButtonsPressed(){
        return this.buttonUp == ButtonState.ON || this.buttonDown == ButtonState.ON;
    }

    public void pressElevatorButtonUp(){
        this.buttonUp = ButtonState.ON;
    }

    public void pressElevatorButtonDown(){
        this.buttonDown  = ButtonState.ON;
    }

    public void unPressButtons(){
        this.buttonUp = ButtonState.OFF;
        this.buttonDown = ButtonState.OFF;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("buttonUp", buttonUp)
                .add("buttonDown", buttonDown)
                .add("floorNumber", floorNumber)
                .add("humans", humans)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Floor floor = (Floor) o;
        return floorNumber == floor.floorNumber;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(floorNumber);
    }
}
