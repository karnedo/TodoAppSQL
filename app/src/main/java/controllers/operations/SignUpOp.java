package controllers.operations;

import android.graphics.Bitmap;

import java.util.concurrent.Callable;

import controllers.ProfileDB;

public class SignUpOp implements Callable<Boolean> {

    private String email;
    private String password;

    public SignUpOp(String email, String password){
        this.email = email;
        this.password = password;
    }

    @Override
    public Boolean call() throws Exception {
        return ProfileDB.signUp(email, password);
    }
}
