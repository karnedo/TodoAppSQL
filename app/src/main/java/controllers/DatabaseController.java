package controllers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.example.todoapp.LoginActivtity;
import com.example.todoapp.R;
import com.example.todoapp.TaskListActivity;

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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import models.Task;
import models.ProviderType;

public class DatabaseController {
    private static final String DATABASE_URL = "http://192.168.1.71/";
    private static final String PREPROCESSOR_URL = DATABASE_URL + "tasks";
    private static final String INSERT_TASK = "/insert_.php";
    private static final String GET_TASKS = "/get_tasks_.php";
    private static final String DELETE_TASK = "/delete_task.ph";
    private static final String CHECK_TASK = "/check_task_.php";
    private static final String SIGN_UP = "/sign_up_.php";
    private static final String LOG_IN = "/log_in_.php";
    private static final String INSERT_PICTURE = "/insert_picture_.php";
    private static final String GET_PICTURE = "/get_picture_.ph";

    private TaskListActivity taskListActivity;
    private Context context;

    public DatabaseController(TaskListActivity taskListActivity, Context context){
        this.taskListActivity = taskListActivity;
        this.context = context;
    }

    /***************************TASKS***************************/

    public void saveTask(Task task, String email){
        StringRequest request =new StringRequest(Request.Method.POST, PREPROCESSOR_URL + INSERT_TASK,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.trim().equalsIgnoreCase("success")) {
                        } else {
                            Toast.makeText(context,
                                    context.getResources().getString(R.string.Error_save_task),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context,
                        context.getResources().getString(R.string.Error_save_task),
                        Toast.LENGTH_SHORT).show();
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
        requestQueue.add(request);
    }

    public void loadTasks(String email) {
        StringRequest request =new StringRequest(Request.Method.POST, PREPROCESSOR_URL+ GET_TASKS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success=jsonObject.getString("success");
                            JSONArray jsonArray =jsonObject.getJSONArray("kraTASKS");

                            if (success.equals("1")){
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
                Toast.makeText(context,
                        context.getResources().getString(R.string.Error_load_task),
                        Toast.LENGTH_SHORT).show();
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

    public void deleteTask(Context context, Task task){
        StringRequest request =new StringRequest(Request.Method.POST, PREPROCESSOR_URL + DELETE_TASK,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Use of trim because somehow response has invisible characters
                        if (response.trim().equalsIgnoreCase("success")) {
                        } else {
                            Toast.makeText(context,
                                    context.getResources().getString(R.string.Error_delete_task),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context,
                        context.getResources().getString(R.string.Error_delete_task),
                        Toast.LENGTH_SHORT).show();
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
        StringRequest request =new StringRequest(Request.Method.POST, PREPROCESSOR_URL + CHECK_TASK,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Use of trim because somehow response has invisible characters
                        if (response.trim().equalsIgnoreCase("success")) {
                        } else {
                            Toast.makeText(context,
                                    context.getResources().getString(R.string.Error_check_task),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context,
                        context.getResources().getString(R.string.Error_check_task),
                        Toast.LENGTH_SHORT).show();
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
        StringRequest request =new StringRequest(Request.Method.POST, PREPROCESSOR_URL + SIGN_UP,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.trim().equalsIgnoreCase("success")) {
                            loginScreen.logIn(ProviderType.EMAIL, email);
                        } else {
                            Toast.makeText(context,
                                    context.getResources().getString(R.string.Error_SignUp),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context,
                        context.getResources().getString(R.string.Error_SignUp),
                        Toast.LENGTH_SHORT).show();
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
        StringRequest request =new StringRequest(Request.Method.POST, PREPROCESSOR_URL + LOG_IN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.trim().equalsIgnoreCase("success")) {
                            loginScreen.logIn(ProviderType.EMAIL, email);
                        } else {
                            loginScreen.showError(context.getResources().getString(R.string.Error_LogIn));
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loginScreen.showError(context.getResources().getString(R.string.Error_LogIn));
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
        StringRequest request =new StringRequest(Request.Method.POST, PREPROCESSOR_URL + INSERT_PICTURE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.trim().equalsIgnoreCase("success")) {
                        } else {
                            Toast.makeText(context,
                                    context.getResources().getString(R.string.Error_save_picture),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context,
                        context.getResources().getString(R.string.Error_save_picture),
                        Toast.LENGTH_SHORT).show();
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
        StringRequest request =new StringRequest(Request.Method.POST, PREPROCESSOR_URL+ GET_PICTURE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
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
                Toast.makeText(context,
                        context.getResources().getString(R.string.Error_get_picture),
                        Toast.LENGTH_SHORT).show();
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

    //metodo que convierte byte[] a bitmap
    private Bitmap bytes_to_bitmap(byte[] b, int width, int height){
        Bitmap.Config config = Bitmap.Config.ARGB_8888;
        Bitmap bitmap = Bitmap.createBitmap(width, height,config);
        try{
            bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
        } catch (Exception e){
        }
        return bitmap;
    }

    //metodo que convierte bitmap a byte[]
    public static byte[] bitmap_to_bytes(Bitmap bitmap)
    {
        if(bitmap == null) return null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 50, bos);
        byte[] bArray = bos.toByteArray();
        return bArray;
    }

}
