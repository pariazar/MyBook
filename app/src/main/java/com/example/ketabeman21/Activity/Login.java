package com.example.ketabeman21.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;


import com.example.ketabeman21.Helper.SessionManager;
import com.example.ketabeman21.Model.DB.SQLiteHandler;
import com.example.ketabeman21.Network.RequestInterface;
import com.example.ketabeman21.R;
import com.example.ketabeman21.Utils.Constants;
import com.example.ketabeman21.Utils.UsernameValidator;
import com.luozm.captcha.Captcha;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class Login extends Activity {
    private static final String TAG = Register.class.getSimpleName();
    private Button btnLinkToRegister;
    private EditText inputUsername;
    private EditText inputPassword;
    private TextView imNotBot_txt;
    private ProgressDialog pDialog;
    private SessionManager session;
    public static SQLiteHandler db;

    private boolean hasAccess;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        imNotBot_txt = findViewById(R.id.imNotbot_txt);
        db = new SQLiteHandler(this);
        final Captcha captcha = (Captcha) findViewById(R.id.captCha);
        captcha.setBitmap("https://footage.framepool.com/shotimg/qf/536757588-minnesota-traffic-light-crossroad-road-traffic.jpg");
        captcha.setCaptchaListener(new Captcha.CaptchaListener() {
            @Override
            public String onAccess(long time) {
                hasAccess = true;
                imNotBot_txt.setText("پازل با موفقیت حل شد");
                imNotBot_txt.setTextColor(Color.BLUE);
                // Login button Click Event
                String username = inputUsername.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();

                // Setup field validators.
                UsernameValidator usernameValidator = new UsernameValidator();
                inputUsername.addTextChangedListener(usernameValidator);

                // Check for empty data in the form
                if (!username.isEmpty() && !password.isEmpty() || usernameValidator.isValidUsername(inputUsername.getText().toString())) {
                    // login user
                    loginUser(username,password);
                    //checkLogin(email, password);
                } else {
                    // Prompt user to enter credentials
                    Toast.makeText(getApplicationContext(),
                            "Please enter the credentials!", Toast.LENGTH_LONG)
                            .show();
                }
                return String.valueOf(time);
            }

            @Override
            public String onFailed(int failedCount) {
                hasAccess = false;
                imNotBot_txt.setText("اشتباهی رخ داده لطفا مجددا سعی کنید");
                imNotBot_txt.setTextColor(Color.RED);
                captcha.reset(false);
                return String.valueOf(failedCount);
            }

            @Override
            public String onMaxFailed() {
                hasAccess = false;
                return "حداکثر ورود";
            }
        });
        inputUsername = findViewById(R.id.username);
        inputPassword =  findViewById(R.id.password);
        btnLinkToRegister = (Button) findViewById(R.id.btnLinkToRegisterScreen);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);


        // Session manager
        session = new SessionManager(getApplicationContext());

        // Check if user is already logged in or not
        if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(Login.this, MainActivity.class);
            startActivity(intent);
            finish();
        }




        // Link to Register Screen
        btnLinkToRegister.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        Register.class);
                startActivity(i);
                finish();
            }
        });


    }

    private void responseTask(String response) {
        Log.d(TAG, "Login Response: " + response.toString());
        hideDialog();

        try {
            JSONObject jObj = new JSONObject(response);
            boolean error = jObj.getBoolean("error");

            // Check for error node in json
            if (!error) {
                // user successfully logged in
                // Create login session
                session.setLogin(true);

                // Now store the user in SQLite
                String uid = jObj.getString("uid");

                JSONObject user = jObj.getJSONObject("user");
                String name = user.getString("name");
                String email = user.getString("email");
                String pic = user.getString("pic");
                String created_at = user
                        .getString("created_at");

                // Inserting row in users table
                db.addUser(name, email,pic, uid, created_at);

                // Launch main activity
                Intent intent = new Intent(Login.this,
                        MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                // Error in login. Get the error message
                String errorMsg = jObj.getString("error_msg");
                Toast.makeText(getApplicationContext(),
                        errorMsg, Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            // JSON error
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void loginUser(String email , String password) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL +"/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        RequestInterface api = retrofit.create(RequestInterface.class);

        Call<String> call = api.getUserLogin(email,password);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        String jsonResponse = response.body();
                        responseTask(jsonResponse);

                    } else {
                        Log.i("onEmptyResponse", "Returned empty response");
                        Toast.makeText(Login.this, "ارتباط با مشکل مواجه شد!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e(TAG, "Login Error: " + t.getMessage());
                Toast.makeText(getApplicationContext(),
                        t.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        });

    }
    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}
