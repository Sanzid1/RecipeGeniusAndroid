package com.example.recipegenius;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.widget.Toolbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class DashboardActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        mAuth = FirebaseAuth.getInstance();
        drawerLayout = findViewById(R.id.drawerLayout);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        // Check if the user is logged in
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            // Show Login Button if not logged in
            Button loginButton = findViewById(R.id.loginButton);
            loginButton.setVisibility(View.VISIBLE);
            loginButton.setOnClickListener(v -> {
                // Redirect to Login Activity
                startActivity(new Intent(DashboardActivity.this, LoginActivity.class));
            });
        } else {
            // Show Sidebar if logged in
            findViewById(R.id.loginButton).setVisibility(View.GONE);
            // Set up Navigation Drawer
            findViewById(R.id.navigationView).setVisibility(View.VISIBLE);

            // Handle Logout Action
            findViewById(R.id.menu_logout).setOnClickListener(v -> {
                mAuth.signOut();
                // After logging out, redirect to login page
                startActivity(new Intent(DashboardActivity.this, LoginActivity.class));
                finish();
            });
        }
    }
}