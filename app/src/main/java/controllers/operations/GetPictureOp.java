package controllers.operations;

import android.graphics.Bitmap;

import java.util.concurrent.Callable;

import controllers.ProfileDB;

public class GetPictureOp implements Callable<Bitmap> {

    private String email;

    public GetPictureOp(String email){
        this.email = email;
    }

    @Override
    public Bitmap call() {
        return ProfileDB.getPicture(email);
    }
}
