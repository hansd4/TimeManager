import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;

public class SaveManager {
    public static void save(Serializable data, String fileName) {
        try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(Paths.get(fileName)))) {
            oos.writeObject(data);
        } catch (Exception e) {
            System.out.println("Couldn't save: " + e.getMessage());
        }
    }

    public static Object load(String fileName) {
        try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(Paths.get(fileName)))) {
            return ois.readObject();
        } catch (Exception e) {
            System.out.println("Couldn't load save data: " + e.getMessage());
            return null;
        }
    }
}
