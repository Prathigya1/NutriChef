package com.example.nutrichef;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RecipeDetails extends AppCompatActivity {

    private static final String TAG = "RecipeDetails";

    TextView recipeTitle, recipeIngredients, recipeSteps, recipeNutrition;
    ImageView recipeImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            setContentView(R.layout.activity_recipe_details);

            recipeTitle = findViewById(R.id.recipeTitle);
            recipeIngredients = findViewById(R.id.recipeIngredients);
            recipeSteps = findViewById(R.id.recipeSteps);
            recipeNutrition = findViewById(R.id.recipeNutrition);
            recipeImage = findViewById(R.id.recipeImage);

            if (getIntent() == null || getIntent().getExtras() == null) {
                Toast.makeText(this, "No recipe data provided", Toast.LENGTH_SHORT).show();
                return;
            }

            String title = getIntent().getStringExtra("title");
            String ingredients = getIntent().getStringExtra("ingredients");
            String steps = getIntent().getStringExtra("steps");
            String nutrition = getIntent().getStringExtra("nutrition");
            int image = getIntent().getIntExtra("image", -1);

            if (title != null) recipeTitle.setText(title);
            if (ingredients != null) recipeIngredients.setText(ingredients);
            if (steps != null) recipeSteps.setText(steps);
            if (nutrition != null) recipeNutrition.setText(nutrition);

            if (image != -1) {
                try {
                    recipeImage.setImageResource(image);
                } catch (Exception e) {
                    Log.e(TAG, "Invalid image resource", e);
                    recipeImage.setImageResource(R.drawable.ic_launcher_background);
                }
            } else {
                recipeImage.setImageResource(R.drawable.ic_launcher_background);
            }
        } catch (Exception e) {
            Log.e(TAG, "Exception in onCreate", e);
            Toast.makeText(this, "Failed to open recipe details (see Logcat).", Toast.LENGTH_SHORT).show();
        }
    }
}


