import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Main {
    private static final String quotes = "\"''";

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        //вводимо дані
        System.out.println("Enter the name of input file : ");
        String input = scanner.next();

        File newFile = new File(input);
        if (!newFile.exists()){
            throw new FileNotFoundException(newFile.getName());
        }
        System.out.println("Enter the name of Output file : ");
        String output = scanner.next();
        System.out.println("Enter the delimiter: ");
        String inputDelimiter = scanner.next();
        System.out.println("Enter the joiner: ");
        String outputDelimiter = scanner.next();
        //задаємо регулярний вираз для пошуку роздільників в рядках файлу
        String regex = String.format("%1$s(?=(?:[^%2$s]*[%2$s][^%2$s]*[%2$s])*[^%2$s]*$)", inputDelimiter, quotes);
        Pattern pattern = Pattern.compile(regex);
        //зчитуємо рядки файлу, розбиває    мо на слова, рахуємо кількість символів та збираємо в список
        List<String> result = Main.splitAndCount(input, outputDelimiter, pattern);
        //записуємо результат у вихідний файл та повідомляємо користувача
        Files.write(Path.of(output), result);
        System.out.println("Done.");
    }


    public static List<String> splitAndCount(String path, String outputDelimiter, Pattern pattern) throws IOException {
        return Files.lines(Path.of(path))
                .map(line ->
                        pattern.splitAsStream(line)
                                .map(Main::trimQuotes)
                                .map(x -> String.valueOf(x.length()))
                                .collect(Collectors.joining(outputDelimiter)))
                .collect(Collectors.toList());
    }

    private static String trimQuotes(String str) {
        if (str.length() >= 2 &&
                quotes.indexOf(str.charAt(0)) != -1 &&
                quotes.indexOf(str.charAt(str.length() - 1)) != -1) {

            return str.substring(1, str.length() - 1);
        }
        return str;
    }
}