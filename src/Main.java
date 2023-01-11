import java.io.*;
import java.util.ArrayList;
import java.util.Objects;

/**
 * ДЗ2 по КПО
 *
 * @author Марина Романчукевич БПИ216
 */
public class Main {

    /**
     * поле пути к корневой папке с файлами
     */
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
        writingResult(stringArr);
    }

    /**
     * функция для вывода результата
     *
     * @param stringArr - коллекция строк из файлов (уже в правильном порядке)
     * @throws IOException - исключение в блоке catch для потока ввода
     */
    private static void writingResult(ArrayList<String> stringArr) throws IOException {
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

    /**
     * функция для чтения файлов
     *
     * @param item - файл, который нужно прочитать
     * @return - содержимое item
     */
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

    /**
     * рекурсивная функция поиска зависимостей
     *
     * @param arr       - несортированная коллекция всех файлов
     * @param sortedArr - вспомогательная сортированная коллекция файлов, заполняется в функции
     * @param item      - текущий файл, который надо поместить в sortedArr
     * @param text      - содержимое текущего файла
     * @param stringArr - сортированная коллекция строк, заполняется в функции
     * @param children  - коллекция для выявления циклических зависисмостей
     * @return true - если найдена циклическая зависимость или другая ошибка, false - если все ок
     */
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

    /**
     * функция для поиска всех фалов в корневой папке и ее директориях
     *
     * @param dir - файл корневой папки
     * @param arr - коллекция, в которую добавляются все найденные файлы
     */
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
