package vuewee.command;

import vuewee.parser.CommandParser;
import vuewee.task.TaskList;
import vuewee.TaskListUI;

class MarkCommand extends Command {
    public void execute(TaskListUI ui, TaskList taskList, CommandParser parser) {
        parser.parse(true, true);
        ui.markTask(parser.getIntParam(), true);
    }
}
