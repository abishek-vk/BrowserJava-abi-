public class DriverTest {
    public static void main(String[] args) {
        try {
            Class.forName("org.postgresql.Driver");
            System.out.println("✅ Driver loaded!");
        } catch (ClassNotFoundException e) {
            System.out.println("❌ Driver NOT found.");
            e.printStackTrace();
        }
    }
}