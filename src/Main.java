import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Main {
    private static boolean isGame;

    public static final String START = "старт";
    public static final String EXIT = "выход";
    public static final String VICTORY = "Победа!";
    public static final String GAME_OVER = "Поражение";
    public static final String PATH = "words.txt";

    private static String word;
    private static String maskedWord;

    private static int mistakeCount = 0;

    private static final String[] lines = new String[7];

    private static ArrayList<Character> letters = new ArrayList<>();

    public static void main(String[] args) {
        startGame();
    }

    public static void startGame() {
        isGame = true;
        while (isGame) {
            System.out.println("Хотите начать новую игру или выйти из приложения? (Введите 'старт' или 'выход')");
            Scanner scanner = new Scanner(System.in);
            String string = scanner.nextLine();
            if (string.equals(START)) {
                mistakeCount = 5;
                letters.clear();
                issuedWord();
                newGame();
            } else if (string.equals(EXIT)) {
                isGame = false;
            }
        }
    }

    public static void issuedWord() {
        System.out.println("Вы решили начать игру");
        randomWord();
        System.out.println("Загадано слово: " + transformsWordInStars(word));
    }

    public static void newGame() {
        while (isGame) {
            if (mistakeCount == 0) {
                System.out.println();
                System.out.println(GAME_OVER);
                System.out.println();
                return;
            }

            if (maskedWord.equals(word)) {
                setVictory();
                return;
            }

            System.out.println();
            System.out.print("Введите букву: ");
            Scanner scanner = new Scanner(System.in);
            String letter = scanner.nextLine();

            if ((letter.equals(word) && mistakeCount >= 1)) {
                setVictory();
                return;
            }

            if (letter.isEmpty()) {
                System.out.println("Похоже вы ввели пустую строку, введите букву");
                continue;
            }

            char charLetter = letter.charAt(0);
            if (!isValidRussianLetter(charLetter)) {
                System.out.println("Введите маленькую букву русского алфавита");
                continue;
            }

            if (letter.length() > 1) {
                System.out.println("Было загадано не это слово, либо пишите по одной букве");
                drawHangman();
                continue;
            }

            if (letters.contains(charLetter)) {
                System.out.println("Эту букву вы уже вводили");
                openLetterInWord(letter);
                drawHangman();
                continue;
            }

            letters.add(charLetter);

            if ((isLetterInWord(letter)) && mistakeCount >= 1) {
                System.out.println("Вы угадали букву");
                openLetterInWord(letter);
                drawHangman();
                continue;
            }

            openLetterInWord(letter);
            mistakeCount -= 1;
            System.out.println("Такой буквы в слове нет");
            drawHangman();
        }
    }

    public static void drawHangman() {
        System.out.println("Количество попыток: " + mistakeCount);

        if (mistakeCount == 4) {
            for (int i = 0; i < lines.length; i++) {
                lines[i] = "|";
            }
            drawingForCycle(lines);
        }

        if (mistakeCount == 3) {
            lines[0] = "|______";
            drawingForCycle(lines);
        }

        if (mistakeCount == 2) {
            lines[1] = "|  |";
            lines[2] = "|  /\\";
            lines[3] = "|  \\/";
            drawingForCycle(lines);
        }

        if (mistakeCount == 1) {
            lines[4] = "| \\|/";
            drawingForCycle(lines);
        }
        if (mistakeCount == 0) {
            lines[5] = "|  |";
            lines[6] = "|  /\\";
            drawingForCycle(lines);
        }
    }

    public static void drawingForCycle(String[] lines) {
        for (int i = 0; i < lines.length; i++) {
            System.out.println(lines[i]);
        }
    }

    public static String transformsWordInStars(String word) {
        char[] charsWordStars = word.toCharArray();
        for (int i = 0; i < word.length(); i++) {
            charsWordStars[i] = '*';
        }
        maskedWord = String.valueOf(charsWordStars);
        return maskedWord;
    }

    public static void openLetterInWord(String letter) {
        if (letter == null || letter.isEmpty()) {
            return;
        }
        char charLetter = letter.charAt(0);
        char[] charsWord = word.toCharArray();
        char[] charsWordStars = maskedWord.toCharArray();
        for (int i = 0; i < word.length(); i++) {
            if ((charsWordStars[i] != charLetter) && (charsWord[i] == charLetter)) {
                charsWordStars[i] = charLetter;
            }
        }
        maskedWord = String.valueOf(charsWordStars);
        System.out.println(maskedWord);
    }

    public static boolean isLetterInWord(String letter) {
        try {
            char charLetter = letter.charAt(0);
            char[] charsWord = word.toCharArray();
            for (int i = 0; i < word.length(); i++) {
                if (charsWord[i] == charLetter) {
                    return true;
                }
            }
        } catch (Exception e) {

        }
        return false;
    }

    public static void setVictory() {
        System.out.println();
        System.out.println(VICTORY);
        System.out.println();
    }

    public static void randomWord() {
        try {
            String[] words = Files.readAllLines(Path.of(PATH)).toArray(new String[0]);

            if (words.length == 0) {
                System.out.println("Файл пуст!");
                return;
            }
            Random random = new Random();
            int index = random.nextInt(words.length);
            word = words[index];
        } catch (Exception e) {
            System.out.println("Ошибка чтения файла");
        }
    }

    public static boolean isValidRussianLetter(char c) {
        return c >= 'а' && c <= 'я';
    }
}