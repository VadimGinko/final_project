import org.junit.jupiter.api.Test;

class OfficeTest {
    @Test
    public void testCreateOffice() {
        Office office = new Office(15, 2);
        System.out.println(office.getFloors());
    }


    @Test
    public void testCreateOffice2() {
        Office office = new Office(15, 2);
        office.handle2();
    }
}