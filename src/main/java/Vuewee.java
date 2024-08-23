import java.util.Scanner;

public class Vuewee {
  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    TaskList taskList = new TaskList();

    System.out.println("____________________________________________________________");
    System.out.println("Hello! I'm Vuewee\nWhat can I do for you?");
    System.out.println("____________________________________________________________");

    // Echo input from user until user types "bye"
    while (true) {
      String input = scanner.nextLine();
      try {
        System.out.println("____________________________________________________________");

        CommandParser parser = new CommandParser(input);

        // Exit the loop if user types "bye"
        if (parser.getCommand().equals("bye")) {
          break;
        }

        switch (parser.getCommand()) {
          // List all tasks with done status if user types "list"
          case "list": {
            taskList.displayTasks();
            break;
          }
          // Mark task as done if user types "mark <task number>"
          case "mark": {
            parser.parse(true, true);
            taskList.markTask(parser.getIntParam(), true);
            break;
          }
          // Unmark task as done if user types "unmark <task number>"
          case "unmark": {
            parser.parse(true, true);
            taskList.markTask(parser.getIntParam(), false);
            break;
          }
          // Add todo task to task list
          // (Usage: todo <description>)
          case "todo": {
            parser.parse(true);
            taskList.addTask(new TodoTask(parser.getDescription()));
            break;
          }
          // Add deadline task to task list
          // (Usage: deadline <description> /by <date>)
          case "deadline": {
            parser.parse(true, false, new CommandOption[] { new CommandOption("by", "date") });
            taskList.addTask(new DeadlineTask(parser.getDescription(), parser.getOption("by")));
            break;
          }
          // Add event task to task list
          // (Usage: event <description> /from <fromDate> /to <toDate>)
          case "event": {
            parser.parse(true, false,
                new CommandOption[] { new CommandOption("from", "fromDate"), new CommandOption("to", "toDate") });
            taskList.addTask(new EventTask(parser.getDescription(), parser.getOption("from"), parser.getOption("to")));
            break;
          }
          // Delete task from task list
          // (Usage: delete <task number>)
          case "delete": {
            parser.parse(true, true);
            taskList.deleteTask(parser.getIntParam());
            break;
          }
          default: {
            System.out.println("Unhandled unknown command: " + parser.getCommand());
            break;
          }
        }
      } catch (
          TaskListException
          | IndexOutOfBoundsException
          | IllegalArgumentException e) {
        System.out.println(e.getMessage());
      }
      System.out.println("____________________________________________________________");
    }

    System.out.println("Bye. Hope to see you again soon!");
    System.out.println("____________________________________________________________");
    scanner.close();
  }
}
