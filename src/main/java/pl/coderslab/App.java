package pl.coderslab;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

/** Task Manager! */
public class App {

  // added enums to keep all sync
  public enum OptionsEnum {
    ADD("add"),
    REMOVE("remove"),
    LIST("list"),
    EXIT("exit"),
    WRONG("wrong");
    public final String label;

    OptionsEnum(String label) {
      this.label = label;
    }
  }

  public static String[] OPTIONS = {"add", "remove", "list", "exit"};

  public static void main(String[] args) {

    showManagerOptions();
    commandLineInterface();
    //    String[][] tasks = readTasksFromFile("tasks.csv");

    //    for (String[] value : tasks) {
    //      System.out.println(Arrays.toString(value));
    //    }
  }

  /** show option at the beginning */
  public static void showManagerOptions() {
    System.out.println(ConsoleColors.BLUE + "Please select an option:");
    for (String text : OPTIONS) {
      System.out.println(ConsoleColors.RESET + text);
    }
  }

  static String[][] readTasksFromFile(String filename) {
    String[][] tasks = null;
    try {
      Path file = Paths.get(filename);
      if (Files.notExists(file)) {
        throw new IOException("Sorry, but file not exist");
      }
      // to have size of array
      // first value will be number of rows and second is standard as user will have 3 option t add
      List<String> test = Files.readAllLines(file, StandardCharsets.UTF_8);
      tasks = new String[test.size()][3];

      for (int i = 0; i < test.size(); i++) {
        int rowLength = test.get(i).split(",").length;
        if (rowLength > 3) {
          // check what to do with user message with comma inside
          // it should be checked when user adding text if we have extra comma
          System.out.println("this row is problem" + test.get(i));
        } else {
          tasks[i] = test.get(i).split(",");
        }
      }

    } catch (IOException e) {
      System.out.println("File not found");
      e.printStackTrace();
    }
    return tasks;
  }

  public static void commandLineInterface() {
    try (Scanner scan = new Scanner(System.in)) {
      String text;
      do {
        text = scan.nextLine();
        System.out.println("selected: " + text);

        switch (text.toLowerCase()) {
          case "add":
            addTask();
            break;
          case "remove":
            removeTask();
            break;
          case "list":
            listAllTasks();
            break;
          default:
            System.out.println("Please select a correct option.");
        }
      } while (!text.equalsIgnoreCase("exit"));
      System.out.println("Bye, bye!");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static boolean exist(String test) {

    for (OptionsEnum value : OptionsEnum.values()) {
      if (value == OptionsEnum.valueOf(test)) {
        return true;
      }
    }
    return false;
  }

  public static void addTask() {
    System.out.println("this is add task method");
  }

  public static void removeTask() {
    System.out.println("this is remove task method");
  }

  public static void listAllTasks() {
    System.out.println("this is method to list all tasks");
  }
}
