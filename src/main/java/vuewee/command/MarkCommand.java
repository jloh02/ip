package vuewee.command;

import vuewee.parser.CommandParser;
import vuewee.task.TaskList;
import vuewee.ui.TaskListUi;

/**
 * Represents a command to mark a task as completed in the task list.
 */
class MarkCommand extends Command {
    /**
     * Executes the mark command. Throws an exception if the task index is invalid.
     *
     * @param ui       The user interface for displaying task information.
     * @param taskList The list of tasks.
     * @param parser   The command parser for parsing user input.
     */
    @Override
    public void execute(TaskListUi ui, TaskList taskList, CommandParser parser) {
        assert ui != null : "UI cannot be null";
        assert taskList != null : "Task list cannot be null";
        assert parser != null : "Parser cannot be null";

        parser.parse(true, true);
        ui.markTask(parser.getIntParam(), true);
    }
}
