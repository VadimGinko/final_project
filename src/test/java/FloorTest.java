import enums.Direction;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class FloorTest {

    @Test
    public void testGetHumansInElevator() {
        Floor floor1 = Floor.of(8);
        Human human = Human.of(40, 4, 8, 15);
        Human human2 = Human.of(60, 9, 8, 15);
        Human human3 = Human.of(70, 6, 8, 15);

        floor1.addHuman(human);
        floor1.addHuman(human2);
        floor1.addHuman(human3);


        var relHumans = floor1.getHumansInElevator(110);
        System.out.println(relHumans);
        assertEquals(relHumans, List.of(human, human3));
    }

    @Test
    public void testPressElevatorButton() {
        Floor floor1 = Floor.of(8);
        floor1.pressElevatorButton(4);
        assertEquals(floor1.pressedButtonDirection(), Direction.Down);
        floor1.pressElevatorButton(10);
        assertEquals(floor1.pressedButtonDirection(), Direction.Up);
    }
}