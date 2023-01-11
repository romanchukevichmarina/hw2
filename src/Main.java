import java.io.*;
import java.util.ArrayList;
import java.util.Objects;

public class Main {
    public static void main(String[] args) throws Exception {
        File dir = new File("src/Archive/BasicExample");
        ArrayList<File> arr = new ArrayList<>();
        findAllFiles(dir, arr);
        ArrayList<File> sortedArr = new ArrayList<>(arr.size());
        ArrayList<String> stringArr = new ArrayList<>(arr.size());
        for (File item : Objects.requireNonNull(arr)){
            if(!sortedArr.contains(item))
                findDependencies(arr, sortedArr, item, readFile(item), stringArr);
        }
    }

    private static String readFile(File item) {
            try (BufferedReader br = new BufferedReader(new FileReader(item))) {
                String s;
                String allStr = "";
                while ((s = br.readLine()) != null) {
                    allStr = allStr.concat(s);
                }
                return allStr;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
    }

    private static void findDependencies(ArrayList<File> arr, ArrayList<File> sortedArr, File item, String s, ArrayList<String> stringArr) throws Exception {
        if (s.contains("require")) {
            String[] strArr = s.split(" ");
            int index = 0;
            for (int i = 0; i < strArr.length; ++i) {
                if (Objects.equals(strArr[i], "require")) {
                    String path = strArr[i + 1].substring(1, strArr[i + 1].length() - 1);
                    File parent = new File(path);
                    if(!arr.contains(parent)){
                        throw new Exception("Incorrect require path in file" + item.getName());
                    }
                    if(!sortedArr.contains(parent))
                        findDependencies(arr, sortedArr, parent, readFile(parent), stringArr);
                    int j = sortedArr.indexOf(parent);
                    if (index < j)
                        index = j;
                }
            }
            sortedArr.add(index + 1, item);
            stringArr.add(index + 1, s);
        } else {
            sortedArr.add(0, item);
            stringArr.add(0, s);
        }
    }

    private static void findAllFiles(File dir, ArrayList<File> arr) {
        if (dir.isDirectory()) {
            for (File item : Objects.requireNonNull(dir.listFiles())) {
                if (item.isDirectory()) {
                    findAllFiles(item, arr);
                } else {
                    arr.add(item);
                }
            }
        }
    }
}
