package vuewee;

import java.util.Scanner;

import vuewee.command.Command;
import vuewee.command.CommandType;
import vuewee.parser.CommandParser;
import vuewee.parser.IllegalCommandException;
import vuewee.task.Task;
import vuewee.task.TaskList;

public class TaskListUI {
  private Scanner scanner = new Scanner(System.in);
  private TaskList taskList = new TaskList();
  private TasksStorage storage = TasksStorage.getInstance();

  public TaskListUI(TaskList taskList) {
    this.taskList = taskList;
  }

  public TaskListUI(Scanner scanner) {
    this.scanner = scanner;
  }

  // Simple helper method to determine if the task count is 1 or more
  private String taskWord() {
    return this.taskList.size() == 1 ? "task" : "tasks";
  }

  // Add a task to the list
  public void addTask(Task task) {
    taskList.add(task);

    System.out.println("Got it. I've added this task:");
    System.out.println("  " + task.toString());
    System.out.println("Now you have " + this.taskList.size() + " " + this.taskWord() + " in the list.");
  }

  // Delete a task from the list at the specified index
  public void deleteTask(int taskNumber) throws IndexOutOfBoundsException {
    taskNumber--; // Adjust task number to match array index

    if (taskNumber >= this.taskList.size() || taskNumber < 0) {
      throw new IndexOutOfBoundsException(
          "Invalid task number. There are " + this.taskList.size() + " " + this.taskWord() + " in your list.");
    }

    Task task = this.taskList.get(taskNumber);
    System.out.println("Noted. I've removed this task:");
    System.out.println("  " + task.toString());
    System.out.println("Now you have " + (this.taskList.size() - 1) + " " + this.taskWord() + " in the list.");

    this.taskList.remove(taskNumber);
  }

  // Display all tasks in the list
  public void displayTasks() throws IllegalCommandException {
    if (this.taskList.size() == 0) {
      throw new IllegalCommandException("You have no tasks in your list.");
    }

    for (int i = 0; i < this.taskList.size(); i++) {
      Task task = this.taskList.get(i);
      System.out.println("  " + (i + 1) + ". " + task.toString());
    }
  }

  // Mark a task as done or not done
  public void markTask(int taskNumber, boolean isDone)
      throws IllegalCommandException {
    taskNumber--; // Adjust task number to match array index

    try {
      boolean isSuccessful = this.taskList.markTask(taskNumber, isDone);
      if (!isSuccessful) {
        throw new IllegalCommandException(this.taskList.get(taskNumber), isDone);
      }
      if (isDone) {
        System.out.println("Nice! I've marked this task as done:");
      } else {
        System.out.println("OK, I've marked this task as not done yet:");
      }
      System.out.println("  " + this.taskList.get(taskNumber).toString());
    } catch (IndexOutOfBoundsException e) {
      throw new IllegalCommandException(
          "Invalid task number. There are " + this.taskList.size() + " " + this.taskWord() + " in your list.");
    }
  }

  public void run() {
    this.taskList = storage.readTasks();
    if (this.taskList.size() > 0) {
      System.out.println(
          "Loaded " + this.taskList.size() + " " + this.taskWord() + " into your task list.");
    }

    System.out.println("____________________________________________________________");
    System.out.println("Hello! I'm Vuewee\nWhat can I do for you?");
    System.out.println("____________________________________________________________");

    // Echo input from user until user types "bye"
    try {
      while (true) {
        String input = scanner.nextLine();
        try {
          System.out.println("____________________________________________________________");

          CommandParser parser = new CommandParser(input);
          CommandType commandType = parser.getCommandType();
          Command command = commandType.createCommand();
          command.execute(this, taskList, parser);

        } catch (IndexOutOfBoundsException | IllegalCommandException e) {
          System.out.println(e.getMessage());
        }
        System.out.println("____________________________________________________________");
        storage.storeTasks(this.taskList);
      }
    } catch (EndProgramException e) {
    } finally {
      System.out.println("Bye. Hope to see you again soon!");
      System.out.println("____________________________________________________________");
      scanner.close();
    }
  }
}
