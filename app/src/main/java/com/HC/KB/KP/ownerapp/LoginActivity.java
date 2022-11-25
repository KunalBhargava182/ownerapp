package com.HC.KB.KP.ownerapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.HC.KB.KP.ownerapp.api.ServerInterface;
import com.HC.KB.KP.ownerapp.pojo.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;



import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class LoginActivity extends AppCompatActivity {
    TextView textView_register;
    static final String KEY_EMPTY = "";
    EditText e_users_email, e_users_password;
    String users_email, users_password;
    public static String d_users_name, d_users_email, d_users_mobile, d_users_dor;
    ProgressDialog pDialog;
    Button login;
    public static String g_p_s1 = "No network connection available.";

    public static SharedPreferences prefs;
    public static SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        prefs = getSharedPreferences("chandan", MODE_PRIVATE);
        editor = prefs.edit();
        String users_id = prefs.getString("usersid", "");
        final String s_users_password = prefs.getString("userspassword", "");
        //Already Loged In
        if (users_id.length() > 0 && s_users_password.length() > 0) {
            Intent intent = new Intent(LoginActivity.this, Dashboard.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            this.finish();
        }

        ui_xmltojava_connect_L();
        validInputs_L();

        textView_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                finish();
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isDeviceOnline()) {
                    activate_online_device();
                } else {
                    //Retrieve the data entered in the edit texts
                    users_email = e_users_email.getText().toString().trim();
                    users_password = e_users_password.getText().toString().trim();
                    if (validInputs_L()) {
                        login();
                    }
                }
            }
        });
    }

    private void login() {

        displayLoader();
        Gson gson = new GsonBuilder().setLenient().create();


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ServerInterface.LOGIN_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        ServerInterface api = retrofit.create(ServerInterface.class);

        Call<User> call = api.getUserLogin(users_email, users_password);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                pDialog.dismiss();
                User users = response.body();
                parseLoginData(users);
            }

            @Override
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                pDialog.dismiss();
                Toast.makeText(LoginActivity.this, "Error : " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    //By this method we will retrieve  the values from json Object
    private void parseLoginData(User response) {
        try {
            if (response.getStatus().equals("true")) {

                saveInfo(response);

                Toast.makeText(LoginActivity.this, "Login Successfully!", Toast.LENGTH_SHORT).show();
                //Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                Intent intent = new Intent(LoginActivity.this, Dashboard.class);

                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                this.finish();
            } else {
                Toast.makeText(this, response.getMessage(), Toast.LENGTH_SHORT).show();
                cleartext();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void activate_online_device() {
        AlertDialog.Builder alert112 = new AlertDialog.Builder(this);
        alert112.setTitle("Network Error");
        alert112.setIcon(R.drawable.logo);
        alert112.setMessage(g_p_s1);
        alert112.setPositiveButton("Activate Internet", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface alert, int which) {
                //Do something
                Intent settingsIntent = new Intent(Settings.ACTION_SETTINGS);
                startActivityForResult(settingsIntent, 9003);
                alert.dismiss();
            }
        });
        alert112.setNegativeButton("Exit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        alert112.show();
    }

    private void displayLoader() {
        pDialog = new ProgressDialog(LoginActivity.this);
        pDialog.setMessage("Logging In.. Please wait...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
    }

    private void cleartext() {
        e_users_email.setText("");
        e_users_password.setText("");
    }

    private void saveInfo(User data) {
        editor.putString("users_id", data.getUsersId());
        editor.putString("users_name", data.getUsersName());
        editor.putString("users_email", data.getUsersEmail());
        editor.putString("users_mobile", data.getUsersMobile());
        editor.putString("users_password", data.getUsersPassword());
        editor.putString("usre_dor", data.getUsersDor());

        //Data for Dashboard
        d_users_name = data.getUsersName().toUpperCase();
        d_users_email = data.getUsersEmail().toLowerCase();
        d_users_mobile = data.getUsersMobile().trim();
        d_users_dor = data.getUsersDor().trim();

        editor.commit();
    }

    private boolean isDeviceOnline() {
        ConnectivityManager connMgr =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    private boolean validInputs_L() {
        if (KEY_EMPTY.equals(users_email)) {
            e_users_email.setError("Email ID cannot be empty");
            e_users_email.requestFocus();
            return false;
        }
        if (KEY_EMPTY.equals(users_password)) {
            e_users_password.setError("Password cannot be empty");
            e_users_password.requestFocus();
            return false;
        }
        return true;
    }

    private void ui_xmltojava_connect_L() {
        textView_register = findViewById(R.id.textViewRegister);
        e_users_email = findViewById(R.id.editTextUsername_login);
        e_users_password = findViewById(R.id.editTextPassword_login);
        login = findViewById(R.id.buttonLogin);
    }
}