import enums.Direction;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class ElevatorTest {

    @Test
    public void testCreateElevatorSuccess(){
        Assertions.assertDoesNotThrow(() -> Elevator.of(2000, 3000, 50));
        Assertions.assertDoesNotThrow(() -> Elevator.of(1000, 2000, 70));
    }

    @Test
    public void testCreateElevatorInvalid(){
        assertThrows(IllegalArgumentException.class, () -> Elevator.of(0, 3000, 50));
        assertThrows(IllegalArgumentException.class, () -> Elevator.of(1000, 0, 70));
        assertThrows(IllegalArgumentException.class, () -> Elevator.of(1000, 3000, 40));
    }

    @Test
    void run() {
    }

    @Test
    public void testSetCertainFloor() {
        Elevator elevator = Elevator.of(1000, 3000, 60);

        assertFalse(elevator.isCertainFloorBe());

        elevator.setCertainFloor(10);
        assertTrue(elevator.isCertainFloorBe());
    }

    @Test
    public void testReleaseHumansByFloorNumber() {
        Elevator elevator = Elevator.of(1000, 3000, 500);
        Human human = Human.of(50, 10, 8, 15);
        Human human2 = Human.of(50, 14, 6, 15);
        Human human3 = Human.of(50, 10, 4, 15);
        List<Human> humans = List.of(human, human2, human3);

        elevator.setHumans(humans);
        elevator.setFloor(10);
        var relHumans = elevator.releaseHumansByFloorNumber();
        assertEquals(relHumans, List.of(human, human3));
    }

    @Test
    public void testNextFloor() {
        Elevator elevator = Elevator.of(1000, 3000, 500);
        Human human = Human.of(50, 10, 8, 15);
        Human human2 = Human.of(50, 14, 6, 15);
        Human human3 = Human.of(50, 13, 4, 15);
        Human human4 = Human.of(50, 6, 4, 15);
        List<Human> humans = new ArrayList<>(List.of(human, human2, human3));

        elevator.setHumans(humans);

        assertEquals(elevator.nextHumanFloor(), 10);
        elevator.setFloor(10);
        elevator.releaseHumansByFloorNumber();
        assertEquals(elevator.nextHumanFloor(), 14);

        elevator.setHumans(new ArrayList<>(List.of(human4)));
        assertEquals(elevator.nextHumanFloor(), 14);

        elevator.setFloor(14);
        elevator.releaseHumansByFloorNumber();
        elevator.setFloor(13);
        elevator.releaseHumansByFloorNumber();
        elevator.setFloor(6);
        elevator.releaseHumansByFloorNumber();

        assertEquals(elevator.nextHumanFloor(), -1);
    }

    @Test
    public void testGetFreeSpace() {
        Elevator elevator = Elevator.of(1000, 3000, 500);
        Human human = Human.of(60, 10, 8, 15);
        Human human2 = Human.of(70, 14, 6, 15);
        Human human3 = Human.of(80, 13, 4, 15);
        List<Human> humans = new ArrayList<>(List.of(human, human2, human3));

        elevator.setHumans(humans);

        assertEquals(500 - 60 - 70 - 80, elevator.getFreeSpace());
    }

    @Test
    public void testSuchElevatorDirection() {
        Elevator elevator = Elevator.of(1000, 3000, 500);
        Human human = Human.of(60, 10, 8, 15);
        Human human2 = Human.of(70, 14, 6, 15);
        Human human3 = Human.of(80, 13, 4, 15);
        List<Human> humans = new ArrayList<>(List.of(human, human2, human3));

        elevator.setHumans(humans);
        elevator.setFloor(7);
        assertEquals(elevator.suchElevatorDirection(), Direction.Up);

        elevator.setFloor(12);
        assertEquals(elevator.suchElevatorDirection(), Direction.Down);

        elevator.setFloor(15);
        assertEquals(elevator.suchElevatorDirection(), Direction.Down);
    }
}