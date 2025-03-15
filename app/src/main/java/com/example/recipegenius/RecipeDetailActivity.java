package com.example.recipegenius;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.recipegenius.model.Recipe;
import com.squareup.picasso.Picasso;

public class RecipeDetailActivity extends AppCompatActivity {

    private ImageView recipeImageView;
    private TextView recipeTitleTextView;
    private TextView caloriesTextView;
    private TextView timeTextView;
    private TextView servingsTextView;
    private TextView cuisineTypeTextView;
    private TextView dietLabelsTextView;
    private TextView healthLabelsTextView;
    private TextView ingredientsTextView;
    private Button viewRecipeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        // Initialize views
        recipeImageView = findViewById(R.id.recipeImageView);
        recipeTitleTextView = findViewById(R.id.recipeTitleTextView);
        caloriesTextView = findViewById(R.id.caloriesTextView);
        timeTextView = findViewById(R.id.timeTextView);
        servingsTextView = findViewById(R.id.servingsTextView);
        cuisineTypeTextView = findViewById(R.id.cuisineTypeTextView);
        dietLabelsTextView = findViewById(R.id.dietLabelsTextView);
        healthLabelsTextView = findViewById(R.id.healthLabelsTextView);
        ingredientsTextView = findViewById(R.id.ingredientsTextView);
        viewRecipeButton = findViewById(R.id.viewRecipeButton);

        // Get recipe from intent
        Recipe recipe = (Recipe) getIntent().getSerializableExtra("RECIPE");
        if (recipe != null) {
            displayRecipeDetails(recipe);
        }
    }

    private void displayRecipeDetails(Recipe recipe) {
        // Set recipe title
        recipeTitleTextView.setText(recipe.getLabel());

        // Load recipe image
        if (recipe.getImage() != null && !recipe.getImage().isEmpty()) {
            Picasso.get().load(recipe.getImage()).into(recipeImageView);
        }

        // Set calories
        caloriesTextView.setText(String.format("Calories: %.0f", recipe.getCalories()));

        // Set cooking time
        if (recipe.getTotalTime() > 0) {
            timeTextView.setText(String.format("Time: %.0f min", recipe.getTotalTime()));
        } else {
            timeTextView.setText("Time: N/A");
        }

        // Set servings
        servingsTextView.setText(String.format("Servings: %d", recipe.getYield()));

        // Set cuisine type
        if (recipe.getCuisineType() != null && !recipe.getCuisineType().isEmpty()) {
            cuisineTypeTextView.setText(TextUtils.join(", ", recipe.getCuisineType()));
        } else {
            cuisineTypeTextView.setText("Not specified");
        }

        // Set diet labels
        if (recipe.getDietLabels() != null && !recipe.getDietLabels().isEmpty()) {
            dietLabelsTextView.setText(TextUtils.join(", ", recipe.getDietLabels()));
        } else {
            dietLabelsTextView.setText("None");
        }

        // Set health labels
        if (recipe.getHealthLabels() != null && !recipe.getHealthLabels().isEmpty()) {
            healthLabelsTextView.setText(TextUtils.join(", ", recipe.getHealthLabels()));
        } else {
            healthLabelsTextView.setText("None");
        }

        // Set ingredients
        if (recipe.getIngredientLines() != null && !recipe.getIngredientLines().isEmpty()) {
            ingredientsTextView.setText(TextUtils.join("\n", recipe.getIngredientLines()));
        } else {
            ingredientsTextView.setText("No ingredients listed");
        }

        // Set up view recipe button to open the recipe URL in a browser
        viewRecipeButton.setOnClickListener(v -> {
            if (recipe.getUrl() != null && !recipe.getUrl().isEmpty()) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(recipe.getUrl()));
                startActivity(browserIntent);
            }
        });
    }
}