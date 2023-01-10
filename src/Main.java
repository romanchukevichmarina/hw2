import java.io.File;
import java.util.Objects;

public class Main {
    public static void main(String[] args) {

        // определяем объект для каталога
        File dir = new File("src/Archive/BasicExample");
        // если объект представляет каталог
        if(dir.isDirectory())
        {
            // получаем все вложенные объекты в каталоге
            for(File item : Objects.requireNonNull(dir.listFiles())){

                if(item.isDirectory()){

                    System.out.println(item.getName() + "  \t folder");
                }
                else{

                    System.out.println(item.getName() + "\t file");
                }
            }
        }
    }
}
