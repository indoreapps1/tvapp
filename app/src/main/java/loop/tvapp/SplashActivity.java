package loop.tvapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        SharedPreferences sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);
        String id = sharedPreferences.getString("id", null);
        if (id != null) {
            startActivity(new Intent(this, MainActivity.class));
            finish();

        } else {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
    }
}
