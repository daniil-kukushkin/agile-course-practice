package ru.unn.agile.todoapp.viewmodel;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TodoAppViewModelLoggingTest {
    private static final LocalDate TODAY = LocalDate.now();
    private static final String TEST_TASK_DESCRIPTION = "Test task";
    private TodoAppViewModel viewModel;

    public void setViewModel(final TodoAppViewModel viewModel)  {
        this.viewModel = viewModel;
    }

    @Before
    public void setUp() {
        viewModel = new TodoAppViewModel(new TestLogger());
    }

    @Test
    public void logIsEmptyAfterStart() {
        List<String> log = viewModel.getLog();

        assertEquals(true, log.isEmpty());
    }

    @Test
    public void setNewTaskDescriptionMentionedInTheLog()  {
        viewModel.setNewTaskDescription(TEST_TASK_DESCRIPTION);
        viewModel.onNewTaskDescriptionfocusChanged();
        String lastLogMessage = viewModel.getLastLogMessage();

        assertTrue(lastLogMessage.matches(".*" + viewModel.getNewTaskDescription() + ".*"));
    }

    @Test
    public void setNewTaskDueDateMentionedInTheLog()  {
        viewModel.setNewTaskDueDate(TODAY);
        viewModel.onTaskDueDateChanged(TODAY, LocalDate.ofYearDay(2, 2));
        String lastLogMessage = viewModel.getLastLogMessage();

        assertTrue(lastLogMessage.matches(".*" + TODAY.toString() + ".*"));
    }

    @Test
    public void addNewTaskEventIsMentionedInTheLog()  {
        viewModel.setNewTaskDescription(TEST_TASK_DESCRIPTION);
        viewModel.pressAddNewTaskButton();
        String lastLogMessage = viewModel.getLastLogMessage();
        assertTrue(lastLogMessage.matches(".*" + LogMessages.NEW_TASK_PRESSED));
    }

    @Test
    public void taskDoneEventIsLogged()  {
        viewModel.setNewTaskDescription(TEST_TASK_DESCRIPTION);
        viewModel.pressAddNewTaskButton();
        viewModel.getTasksViewModels().get(0).clickIsDoneCheckBox();

        String lastLogMessage = viewModel.getLastLogMessage();
        assertTrue(lastLogMessage.matches(".*"
                + LogMessages.TASK_FINISHED + TEST_TASK_DESCRIPTION));
    }

    @Test
    public void taskDeleteEventIsLogged()  {
        viewModel.setNewTaskDescription(TEST_TASK_DESCRIPTION);
        viewModel.pressAddNewTaskButton();
        viewModel.pressDeleteButton(viewModel.getTasksViewModels().get(0));

        String lastLogMessage = viewModel.getLastLogMessage();
        assertTrue(lastLogMessage.matches(".*"
                + LogMessages.TASK_DELETED + TEST_TASK_DESCRIPTION));
    }

    @Test
    public void logsStringPropertyUpdatedViaCallback()  {
        viewModel.setNewTaskDescription(TEST_TASK_DESCRIPTION);
        viewModel.pressAddNewTaskButton();

        String logsString = viewModel.getLogsString();
        assertTrue(logsString.matches(".*" + LogMessages.NEW_TASK_PRESSED + "\n.*"));
    }
}
