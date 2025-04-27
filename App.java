import java.util.ArrayList;
import java.util.List;
import model.Student;
import ui.MainUI;
public class App {
    public static void main(String[] args) {
        List<Student> students = new ArrayList<>();
        new MainUI(students);
    }
}
