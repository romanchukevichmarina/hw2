import java.io.*;
import java.util.ArrayList;
import java.util.Objects;

public class Main {
    static String root = "src/Archive/BasicExample";

    public static void main(String[] args) throws IOException {
        File dir = new File(root);
        ArrayList<File> arr = new ArrayList<>();
        findAllFiles(dir, arr);
        ArrayList<File> sortedArr = new ArrayList<>(arr.size());
        ArrayList<String> stringArr = new ArrayList<>(arr.size());
        for (File item : Objects.requireNonNull(arr)) {
            if (!sortedArr.contains(item)) {
                ArrayList<File> children = new ArrayList<>();
                if (findDependencies(arr, sortedArr, item, readFile(item), stringArr, children)) return;
            }
        }
        File ans = new File("src/Archive/ans");
        boolean b = true;
        if (!ans.exists()) b = ans.createNewFile();
        if (b) {
            try (FileWriter writer = new FileWriter(ans, false)) {
                for (String s : Objects.requireNonNull(stringArr)) {
                    writer.append(s);
                }
                writer.flush();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            System.out.println("Result in file 'ans' in src/Archive");
        }
    }

    private static String readFile(File item) {
        try (BufferedReader br = new BufferedReader(new FileReader(item))) {
            String s;
            StringBuilder allStr = new StringBuilder();
            while ((s = br.readLine()) != null) {
                allStr.append(s);
                allStr.append("\n");
            }
            return allStr.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static boolean findDependencies(ArrayList<File> arr, ArrayList<File> sortedArr, File item, String text, ArrayList<String> stringArr, ArrayList<File> children) {
        if (text.contains("require")) {
            String[] requires = text.split("require");
            int index = 0;
            for (int i = 1; i < requires.length; ++i) {
                String path = requires[i].split("'")[1];
                File parent = new File(root + "/" + path);
                children.add(item);
                if (!arr.contains(parent)) {
                    System.out.println("Incorrect require path in file" + item.getName());
                    return true;
                }
                if (!sortedArr.contains(parent)) {
                    if (children.contains(parent)) {
                        System.out.print("Can't find dependencies: circle in files ");
                        children.forEach(f -> System.out.print("'" + f.getName() + "' "));
                        System.out.println();
                        return true;
                    }
                    if (findDependencies(arr, sortedArr, parent, readFile(parent), stringArr, children)) return true;
                }
                int j = sortedArr.indexOf(parent);
                if (index < j) index = j;
                else children.remove(parent);
            }
            sortedArr.add(index + 1, item);
            stringArr.add(index + 1, text);
        } else {
            sortedArr.add(0, item);
            stringArr.add(0, text);
        }
        return false;
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
