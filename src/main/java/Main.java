import java.util.*;

class Main {
    public static void main(String[] args) {
        Office office = Office.of(15, 2);
        office.start();
        office.handle();
    }
} 