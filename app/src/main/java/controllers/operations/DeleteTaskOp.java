package controllers.operations;

import java.util.concurrent.Callable;

import controllers.TaskDB;
import models.Task;

public class DeleteTaskOp implements Callable<Boolean> {

    private Task task;

    public DeleteTaskOp(Task task){ this.task = task; }

    @Override
    public Boolean call(){
        return TaskDB.deleteTask(task);
    }
}
