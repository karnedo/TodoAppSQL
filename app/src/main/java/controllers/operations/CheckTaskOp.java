package controllers.operations;

import java.util.concurrent.Callable;

import controllers.TaskDB;
import models.Task;

public class CheckTaskOp implements Callable<Boolean> {

    private Task task;
    private boolean isChecked;

    public CheckTaskOp(Task task, boolean isChecked){
        this.task = task;
        this.isChecked = isChecked;
    }

    @Override
    public Boolean call() { return TaskDB.checkTask(task, isChecked); }
}
