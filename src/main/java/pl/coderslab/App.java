package pl.coderslab;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.InputMismatchException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

/** Task Manager! */
public class App {

  // test some enums to keep all sync
  // not used for now
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

  /** Main constant values used in Task Manager */
  static String FILE = "tasks.csv";

  static String[] OPTIONS = {"add", "remove", "list", "exit"};
  static String[][] TASKS;

  public static void main(String[] args) {

    commandLineInterface();
  }

  /** show CL option at the beginning */
  public static void showManagerOptions() {
    System.out.println(ConsoleColors.BLUE + "Please select an option:");
    for (String text : OPTIONS) {
      System.out.println(ConsoleColors.RESET + text);
    }
  }

  /** Main logic for Task Manager */
  public static void commandLineInterface() {
    // get tasks data from file
    TASKS = readTasksFromFile(FILE);
    // show CL options
    showManagerOptions();
    try (Scanner scan = new Scanner(System.in)) {
      String text;
      do {
        text = scan.nextLine();
        System.out.println(ConsoleColors.YELLOW_UNDERLINED + "selected: " + text);
        System.out.println(ConsoleColors.RESET);

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
          case "exit":
            // end of apps and write all changes to file
            writeDataToFile(TASKS, FILE);
            System.out.println(ConsoleColors.RED + "Bye, bye!");
            System.out.println(ConsoleColors.RESET);
            System.exit(0);
            break;
          default:
            System.out.println("Please select a correct option.");
        }
        showManagerOptions();
      } while (!text.equalsIgnoreCase("exit"));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * read all tasks from file
   *
   * @param filename String
   * @return String[][]
   */
  public static String[][] readTasksFromFile(String filename) {
    String[][] data = null;
    try {
      Path file = Paths.get(filename);
      if (Files.notExists(file)) {
        throw new IOException(
            "Sorry, but file not exist, do you want to continue? or type 'exit' to finish");
      }
      // to have size of array
      // first value will be number of rows and second is standard as user will have 3 option to add
      List<String> test = Files.readAllLines(file, StandardCharsets.UTF_8);
      data = new String[test.size()][3];

      for (int i = 0; i < test.size(); i++) {
        int rowLength = test.get(i).split(",").length;
        if (rowLength > 3) {
          // check what to do with user message with comma inside
          // it should be checked when user adding text if we have extra comma
          System.out.println("this row is problem" + test.get(i));
        } else {
          data[i] = test.get(i).split(",");
        }
      }

    } catch (IOException e) {
      System.out.println(ConsoleColors.RED + e.getMessage());
      System.out.println(ConsoleColors.RESET);
      e.printStackTrace();
    }
    return data;
  }

  /** Add Task collect data from user and update tasks array */
  public static void addTask() {
    StringBuilder data = new StringBuilder();
    try {
      // Not close scanner here only in interface function
      // otherwise will have problem
      Scanner scan = new Scanner(System.in);
      // add description,
      // need as well some kind validation if user
      // use comma in string
      System.out.println("Please add task description:");
      String description = scan.nextLine();
      data.append(description).append(", ");

      // this will need maybe some date validation, but for now
      // I will focus on app workflow
      System.out.println("Please add task due date:");
      String dueDate = scan.nextLine();
      data.append(dueDate).append(", ");

      // Is your task important? (true / false)
      System.out.println("Is your task important? (true / false)");
      String isImportant = scan.nextLine();
      data.append(isImportant);
      // here update values in the TASKS array
      updateTasksArray(data.toString());

    } catch (NoSuchElementException e) {
      e.printStackTrace();
    }
  }

  /**
   * Update Tasks Array
   *
   * @param data String
   */
  public static void updateTasksArray(String data) {
    TASKS = ArrayUtils.add(TASKS, data.split(","));
  }

  /**
   * check if task id is valid number and bigger than zero
   *
   * @param input String
   * @return boolean
   */
  public static boolean isCorrectNumberValue(String input) {
    if (NumberUtils.isDigits(input)) {
      return Integer.parseInt(input) >= 0;
    }
    return false;
  }

  /** remove task method, ask user to type task number and execute task remove */
  public static void removeTask() {
    String taskId;

    try {
      Scanner scan = new Scanner(System.in);

      do {
        System.out.println("Please select number to remove:");
        taskId = scan.nextLine();
        if (!NumberUtils.isDigits(taskId)) {
          System.out.println(ConsoleColors.RED_BOLD + "Sorry but this is not a number!");
          System.out.println(ConsoleColors.RESET);
        }
      } while (!isCorrectNumberValue(taskId));

      boolean isRemoved = false;

      // remove selected task
      if (TASKS != null) {
        isRemoved = removeSelectedTask(Integer.parseInt(taskId));
      }
      if (isRemoved) {
        System.out.printf(
            ConsoleColors.YELLOW_UNDERLINED + "Task number %s was removed successful\n", taskId);
        System.out.println(ConsoleColors.RESET);
      } else {
        System.out.println(
            ConsoleColors.RED + "Sorry, but probably that task not exist in your list");
        System.out.println(ConsoleColors.RESET);
      }

    } catch (InputMismatchException e) {
      e.printStackTrace();
    }
  }

  /**
   * Remove selected task by ID
   *
   * @param taskId int
   * @return boolean
   */
  public static boolean removeSelectedTask(int taskId) {
    boolean isRemoved = false;
    // find task to remove and remove it from data
    try {
      if (TASKS.length > taskId) {
        TASKS = ArrayUtils.remove(TASKS, taskId);
        isRemoved = true;
      }
    } catch (IndexOutOfBoundsException e) {
      System.out.println("Sorry but element not exist");
    }
    return isRemoved;
  }

  /**
   * write changes to file
   *
   * @param array String[][]
   * @param fileName String
   */
  public static void writeDataToFile(String[][] array, String fileName) {
    File data = new File(fileName);
    Path file = Paths.get(fileName);
    // extra check if file not exist and user wants to continue,
    // so we create that file for user
    try {
      if (Files.notExists(file)) {
        Files.createFile(file);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    try (PrintWriter printWriter = new PrintWriter(data)) {
      // add data to file
      if (ArrayUtils.isNotEmpty(TASKS)) {
        for (String[] value : array) {
          printWriter.println(String.join(",", value));
        }
      }

    } catch (FileNotFoundException | NullPointerException e) {
      e.printStackTrace();
    }
  }

  /** show all tasks in CL */
  public static void listAllTasks() {
    StringBuilder taskText = new StringBuilder();

    if (ArrayUtils.isEmpty(TASKS)) {
      System.out.println(
          ConsoleColors.YELLOW_UNDERLINED + "Sorry, but there is no tasks in your list!");
      System.out.println(ConsoleColors.RESET);
      return;
    }

    for (int i = 0; i < TASKS.length; i++) {
      taskText.append(i).append(" : ");
      for (int j = 0; j < TASKS[i].length; j++) {
        taskText.append(TASKS[i][j]).append(" ");
      }
      taskText.append("\n");
    }
    System.out.println(taskText);
  }
}
