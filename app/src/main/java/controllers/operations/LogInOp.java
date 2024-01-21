package controllers.operations;

import java.util.concurrent.Callable;

import controllers.ProfileDB;

public class LogInOp implements Callable<Boolean> {

    private String email;
    private String password;

    public LogInOp(String email, String password) {
        this.email = email;
        this.password = password;
    }

    @Override
    public Boolean call() throws Exception {
        return ProfileDB.logIn(email, password);
    }
}
