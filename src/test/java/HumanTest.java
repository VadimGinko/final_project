import enums.Direction;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class HumanTest {

    @Test
    public void testCreateRandomHumanSuccess(){
        Assertions.assertDoesNotThrow(() -> Human.ofRandom(10));
        Assertions.assertDoesNotThrow(() -> Human.ofRandom(20));
        Assertions.assertDoesNotThrow(() -> Human.ofRandom(25));
        Assertions.assertDoesNotThrow(() -> Human.ofRandom(13));
        Assertions.assertDoesNotThrow(() -> Human.ofRandom(8));
    }

    @Test
    public void testSetWeightSuccess() {
        Human human = Human.ofRandom(10);

        human.setWeight(20);
        assertEquals(human.getWeight(), 20);
    }

    @Test
    public void testSetWeightInvalid() {
        Human human = Human.ofRandom(10);

        assertThrows(IllegalArgumentException.class, () -> human.setWeight(-20));
        assertThrows(IllegalArgumentException.class, () -> human.setWeight(110));
    }

    @Test
    public void testSetRightFloorSuccess() {
        int officeSize = 10;
        Human human = Human.ofRandom(officeSize);

        Random random = new Random();
        int rRightFloor;
        do {
            rRightFloor = random.nextInt(officeSize) + 1;
        } while (rRightFloor == human.getFloor());

        human.setRightFloor(rRightFloor);
        assertEquals(human.getRightFloor(), rRightFloor);
    }

    @Test
    public void testSetRightFloorInvalid() {
        Human human = Human.ofRandom(10);

        assertThrows(IllegalArgumentException.class, () -> human.setRightFloor(11));
        assertThrows(IllegalArgumentException.class, () -> human.setRightFloor(0));
        assertThrows(IllegalArgumentException.class, () -> human.setRightFloor(human.getFloor()));
    }

    @Test
    public void testSetFloorSuccess() {
        Human human = Human.ofRandom(10);

        human.setFloor(8);
        assertEquals(human.getFloor(), 8);
    }

    @Test
    public void testSetFloorInvalid() {
        Human human = Human.ofRandom(10);

        assertThrows(IllegalArgumentException.class, () -> human.setFloor(11));
        assertThrows(IllegalArgumentException.class, () -> human.setFloor(0));
    }

    @Test
    public void testSuchHumanDirection() {
        Human human = Human.ofRandom(10);

        human.setFloor(4);
        human.setRightFloor(8);
        assertEquals(human.suchHumanDirection(), Direction.Up);

        human.setFloor(8);
        human.setRightFloor(4);
        assertEquals(human.suchHumanDirection(), Direction.Down);
    }
}