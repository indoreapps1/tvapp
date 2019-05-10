package loop.tvapp;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import es.dmoral.toasty.Toasty;
import loop.tvapp.utilities.Utility;

public class LoginActivity extends AppCompatActivity {
    TextInputEditText edt_pass, edt_username;
    String username, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
    }

    private void init() {
        edt_username = findViewById(R.id.edt_username);
        edt_pass = findViewById(R.id.edt_pass);
        Button btn_login = findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//check login Data here.
                if (Utility.isOnline(LoginActivity.this)) {
                    loginData();
                } else {
                    Toasty.error(LoginActivity.this, "No Internet Connection").show();
                }
            }
        });
    }

    private void loginData() {
        if (validation()) {
            startActivity(new Intent(this, MainActivity.class));
        }

    }

    private boolean validation() {
        username = edt_username.getText().toString().trim();
        password = edt_pass.getText().toString();
        if (username.length() == 0) {
            edt_username.setError("Enter Username");
            edt_username.requestFocus();
            return false;
        } else if (password.length() == 0) {
            edt_pass.setError("Enter Password");
            edt_pass.requestFocus();
            return false;
        } else if (!username.equals("android")) {
            edt_username.setError("Enter Valid  Username");
            edt_username.requestFocus();
            return false;
        } else if (!password.equals("123")) {
            edt_pass.setError("Enter Valid Password");
            edt_pass.requestFocus();
            return false;
        }

        return true;
    }
}
