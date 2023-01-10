import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

public class Main {
    public static void main(String[] args) {
        File dir = new File("src/Archive/BasicExample");
        ArrayList<File> arr = new ArrayList<>(findAllFiles(dir));
    }

    private static ArrayList<File> findAllFiles(File dir) {
        ArrayList<File> arr = new ArrayList<>();
        if(dir.isDirectory())
        {
            for(File item : Objects.requireNonNull(dir.listFiles())){
                if(item.isDirectory()){
                    findAllFiles(item);
                }
                else{
                    arr.add(item);
                }
            }
        }
        return arr;
    }
}
