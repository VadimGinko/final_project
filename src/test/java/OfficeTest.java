import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class OfficeTest {
    @Test
    public void testCreateOffice() {
        Office office = Office.of(15, 2);
        assertEquals(office.getFloors().size(), 15);
        assertEquals(office.getElevators().size(), 2);
    }


}