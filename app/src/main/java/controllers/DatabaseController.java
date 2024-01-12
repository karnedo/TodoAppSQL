package controllers;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.todoapp.LoginActivtity;
import com.example.todoapp.R;
import com.example.todoapp.TaskListActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageException;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import models.Task;
import models.ProviderType;

public class DatabaseController {

    public static final String DATABASE_URL = "http://192.168.1.71/";
    public static final String PREPROCESSOR_URL = DATABASE_URL + "tasks";

    private FirebaseDatabase database;
    private DatabaseReference ref;
    private ArrayList<Task> tasks;
    private TaskListActivity taskListActivity;
    private Context context;

    public DatabaseController(TaskListActivity taskListActivity, Context context){
        database = FirebaseDatabase.getInstance("https://to-do-app-9be18-default-rtdb.europe-west1.firebasedatabase.app/");
        //ref = database.getReference("tasks");
        ref = database.getReference("" + FirebaseAuth.getInstance().getUid() + "/tasks");
        tasks = new ArrayList<Task>(0);
        this.taskListActivity = taskListActivity;
        this.context = context;
    }

    /***************************TASKS***************************/

    public void saveTask(Task task, String email){
        StringRequest request =new StringRequest(Request.Method.POST, PREPROCESSOR_URL + "/insert_.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("MyApp", response);
                        if (response.trim().equalsIgnoreCase("success")) {
                            Log.i("MyApp", "Success");
                        } else {
                            Log.i("MyApp", "Fail onResponse on save task");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("MyApp", "Fail onErrorResponse: " + error.getMessage() + " , " + error.getCause());
            }
        }
        ){

            @Override
            protected Map<String, String> getParams() {

                Map<String,String>params=new HashMap<>();
                //idTask -> tasks identifiers are auto incrementable
                params.put("email", email);
                params.put("name", task.getName());
                params.put("date", task.getDate().toString());
                params.put("priority", "" + task.getPriority().toString().charAt(0));
                params.put("isChecked", "" + ((task.isChecked()) ? "1" : "0"));

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        Log.i("MyApp", "Request: " + request);
        requestQueue.add(request);
    }

    public void loadTasks(String email) {
        StringRequest request =new StringRequest(Request.Method.POST, PREPROCESSOR_URL+ "/get_tasks_.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.i("MyApp", "Response: " + response);
                            JSONObject jsonObject = new JSONObject(response);
                            String exito=jsonObject.getString("success");
                            JSONArray jsonArray =jsonObject.getJSONArray("kraTASKS");

                            if (exito.equals("1")){
                                for (int i=0;i<jsonArray.length();i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    int id = object.getInt("idTask");
                                    String name = object.getString("name");
                                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                    Date date = dateFormat.parse(object.getString("date"));
                                    String priorityFirstLetter = object.getString("priority");
                                    Task.Priority priority = Task.Priority.HIGH;
                                    switch (priorityFirstLetter){
                                        case "M":
                                            priority = Task.Priority.MED;
                                            break;
                                        case "L":
                                            priority = Task.Priority.LOW;
                                            break;
                                    }
                                    Task task = new Task(id, name, date, priority);
                                    taskListActivity.addTask(task);
                                }
                            }
                        }
                        catch (JSONException | ParseException ex) {
                            throw new RuntimeException(ex);
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context,error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        }
        ){
            @Override
            protected Map<String, String> getParams() {
                Map<String,String>params=new HashMap<>();
                Log.i("MyApp", "Email: " + email);
                params.put("email", email);
                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
    }

    public void deleteTask(Context context, Task task){
        StringRequest request =new StringRequest(Request.Method.POST, PREPROCESSOR_URL + "/delete_task.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Use of trim because somehow response has invisible characters
                        if (response.trim().equalsIgnoreCase("success")) {
                            Log.i("MyApp", "Success");
                        } else {
                            Log.i("MyApp", "Fail: " + response);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("MyApp", "onErrorResponse");
            }
        }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String>params=new HashMap<>();
                params.put("idTask", "" + task.getId());
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
    }

    public void checkTask(Context context, Task task, boolean isChecked){
        StringRequest request =new StringRequest(Request.Method.POST, PREPROCESSOR_URL + "/check_task_.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Use of trim because somehow response has invisible characters
                        if (response.trim().equalsIgnoreCase("success")) {
                            Log.i("MyApp", "Updated task");
                        } else {
                            Log.i("MyApp", "Couldnt update task");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("MyApp", "Update task onErrorResponse");
            }
        }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String>params=new HashMap<>();
                params.put("idTask", ""+ task.getId());
                params.put("isChecked", isChecked ? "1" : "0");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
    }

    /***************************USERS***************************/

    public void signUp(String email, String password, LoginActivtity loginScreen){
        StringRequest request =new StringRequest(Request.Method.POST, PREPROCESSOR_URL + "/sign_up_.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.trim().equalsIgnoreCase("success")) {
                            loginScreen.logIn(ProviderType.EMAIL, email);
                        } else {
                            loginScreen.showError("No ha sido posible el registro");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loginScreen.showError("No ha sido posible el registro");
            }
        }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String>params=new HashMap<>();
                params.put("email",email);
                params.put("password",password);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
    }

    public void logIn(String email, String password, LoginActivtity loginScreen){
        StringRequest request =new StringRequest(Request.Method.POST, PREPROCESSOR_URL + "/log_in_.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("MyApp", response);
                        if (response.trim().equalsIgnoreCase("success")) {
                            loginScreen.logIn(ProviderType.EMAIL, email);
                        } else {
                            loginScreen.showError("No ha sido posible iniciar sesión");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loginScreen.showError("No ha sido posible iniciar sesión");
            }
        }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String>params=new HashMap<>();
                params.put("email",email);
                params.put("password",password);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
    }

    /***************************PROFILE PICTURE***************************/
    public void saveProfilePicture(String email, Bitmap bitmap){
        StringRequest request =new StringRequest(Request.Method.POST, PREPROCESSOR_URL + "/insert_picture_.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("MyApp", "Picture: " + response);
                        if (response.trim().equalsIgnoreCase("success")) {
                            Log.i("MyApp", "Saved picture");
                        } else {
                            Log.i("MyApp", "Couldnt save picture");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("MyApp", "Couldnt save picture: " + error.getClass() + " -> " + error.getMessage());
            }
        }
        ){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String>params=new HashMap<>();
                params.put("email", email);
                byte[] picBytes = bitmap_to_bytes(bitmap);
                String picString = android.util.Base64.encodeToString(picBytes, Base64.DEFAULT);
                params.put("picture",picString);
                return params;
            }
        };


        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
    }

    public void getProfilePicture(String email){
        StringRequest request =new StringRequest(Request.Method.POST, PREPROCESSOR_URL+ "/get_picture_.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.i("MyApp", "Response: " + response);
                            JSONObject jsonObject = new JSONObject(response);
                            String success=jsonObject.getString("success");

                            if (success.equals("1")){
                                JSONArray jsonArray =jsonObject.getJSONArray("picture");
                                if(jsonArray.length() <= 0){
                                    taskListActivity.setProfilePic(null);
                                    return;
                                }
                                for (int i=0;i<jsonArray.length();i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    String picture = object.getString("picture");
                                    byte[] pictureBytes = android.util.Base64.decode(picture,Base64.DEFAULT);
                                    Bitmap pictureBitmap = bytes_to_bitmap(pictureBytes, 500, 500);
                                    taskListActivity.setProfilePic(pictureBitmap);
                                }
                            }
                        }
                        catch (JSONException ex) {
                            throw new RuntimeException(ex);
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context,error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        }
        ){
            @Override
            protected Map<String, String> getParams() {
                Map<String,String>params=new HashMap<>();
                params.put("email", email);
                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
    }

    //método que convierte byte[] a bitmap
    private Bitmap bytes_to_bitmap(byte[] b, int width, int height){
        Bitmap.Config config = Bitmap.Config.ARGB_8888;
        Bitmap bitmap = Bitmap.createBitmap(width, height,config);
        try{
            bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
        } catch (Exception e){
        }
        return bitmap;
    }

    //método que convierte bitmap a byte[]
    public static byte[] bitmap_to_bytes(Bitmap bitmap)
    {
        if(bitmap != null){
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 50, bos);
            byte[] bArray = bos.toByteArray();
            return bArray;
        }
        return null;
    }

}
