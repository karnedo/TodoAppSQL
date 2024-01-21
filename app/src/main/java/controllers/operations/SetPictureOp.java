package controllers.operations;

import android.graphics.Bitmap;

import java.util.concurrent.Callable;

import controllers.ProfileDB;

public class SetPictureOp implements Callable<Boolean> {

    private String email;

    private Bitmap picture;

    public SetPictureOp(String email, Bitmap picture){
        this.email = email;
        this.picture = picture;
    }

    @Override
    public Boolean call() throws Exception {
        return ProfileDB.setPicture(email, picture);
    }
}
