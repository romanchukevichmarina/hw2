import java.io.*;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Objects;

public class Main {
    public static void main(String[] args) {
        File dir = new File("src/Archive/BasicExample");
        ArrayList<File> arr = new ArrayList<>(findAllFiles(dir));
        ArrayDeque<File> sortedArr = new ArrayDeque<>(arr.size());
        readFiles(arr, sortedArr);
    }

    private static void readFiles(ArrayList<File> arr, ArrayDeque<File> sortedArr) {
        for (File item : Objects.requireNonNull(arr)) {
            try (BufferedReader br = new BufferedReader(new FileReader(item))) {
                String s;
                while ((s = br.readLine()) != null) {
                    findDependencies(sortedArr, item, s);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static void findDependencies(ArrayDeque<File> sortedArr, File item, String s) {
        if(s.contains("require")){
            String[] strArr = s.split(" ");
            //-----
            sortedArr.add(item);
        }
    }

    private static ArrayList<File> findAllFiles(File dir) {
        ArrayList<File> arr = new ArrayList<>();
        if (dir.isDirectory()) {
            for (File item : Objects.requireNonNull(dir.listFiles())) {
                if (item.isDirectory()) {
                    findAllFiles(item);
                } else {
                    arr.add(item);
                }
            }
        }
        return arr;
    }
}
