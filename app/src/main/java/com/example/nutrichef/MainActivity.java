package com.example.nutrichef;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Button btnSearchRecipe, btnPopularDishes, btnNutritionCalc;
    private ImageView imgLogo;
    private TextView tvWelcome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // views
        imgLogo = findViewById(R.id.imgLogo);
        tvWelcome = findViewById(R.id.tvWelcome);
        btnSearchRecipe = findViewById(R.id.btnSearchRecipe);
        btnPopularDishes = findViewById(R.id.btnPopularDishes);
        btnNutritionCalc = findViewById(R.id.btnNutritionCalc);

        // safe logo set (if your drawable missing fallback)
        try { imgLogo.setImageResource(R.drawable.logo); } catch (Exception e) { imgLogo.setImageResource(R.drawable.ic_launcher_background); }

        // fade-in animation for logo
        AlphaAnimation fade = new AlphaAnimation(0f, 1f);
        fade.setDuration(800);
        fade.setFillAfter(true);
        imgLogo.startAnimation(fade);
        // simple welcome interaction: clicking welcome toggles a tiny toast
        tvWelcome.setOnClickListener(v -> Toast.makeText(MainActivity.this, "Tip: Use search to find dishes fast!", Toast.LENGTH_SHORT).show());

        // Search -> open the full recipe list
        btnSearchRecipe.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RecipeActivity.class);
            startActivity(intent);
        });

        // Popular -> open recipe list with a special flag (we'll filter in RecipeActivity)
        btnPopularDishes.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RecipeActivity.class);
            intent.putExtra("showPopularOnly", true);
            startActivity(intent);
        });

        // Nutrition -> open NutritionCalculatorActivity
        btnNutritionCalc.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, NutritionCalculatorActivity.class);
            startActivity(intent);
        });
    }
}


