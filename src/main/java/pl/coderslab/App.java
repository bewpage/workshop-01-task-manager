package pl.coderslab;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

/** Task Manager! */
public class App {
  public static void main(String[] args) {

    showManagerOptions();
    String[][] tasks = readTasksFromFile();

    for(String[] value : tasks){
      System.out.println(Arrays.toString(value));
    }
  }

  public static void showManagerOptions() {
    String welcomeText = "Please select an option:";
    String[] options = {"add", "remove", "list", "exit"};

    System.out.println(ConsoleColors.BLUE + welcomeText);
    for (String text : options) {
      System.out.println(ConsoleColors.RESET + text);
    }
  }

  static String[][] readTasksFromFile() {
    //    File readData = new File("tasks.csv");
    String[][] tasks = null;
    try {
      Path file = Paths.get("tasks.csv");
      if (Files.notExists(file)) {
        throw new IOException("Sorry, but file not exist");
      }
      // to have size of array
      // first value will me number of rows and second is standard as user will have 3 option t add
      List<String> test = Files.readAllLines(file, StandardCharsets.UTF_8);
      tasks = new String[test.size()][3];

      for(int i = 0; i < test.size(); i++){
        int rowLength = test.get(i).split(",").length;
        if(rowLength > 3){
          // check what to do with user message with comma inside
          // it should be check when user adding text if we have extra comma
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
}
