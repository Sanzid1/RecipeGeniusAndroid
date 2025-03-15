package com.example.recipegenius;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.recipegenius.api.EdamamApi;
import com.example.recipegenius.api.RetrofitClient;
import com.example.recipegenius.model.Recipe;
import com.example.recipegenius.model.RecipeResponse;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipeSearchActivity extends AppCompatActivity {

    private static final String APP_ID = "4c13f4e4";
    private static final String APP_KEY = "eae8b25320802e70118428e9c4cf033d";

    private LinearLayout ingredientsContainer;
    private Button addIngredientButton;
    private Button searchRecipesButton;
    private RadioGroup dietaryRadioGroup;
    private RadioButton halalRadioButton;
    private RadioButton allRadioButton;
    private CheckBox americanCheckBox, asianCheckBox, italianCheckBox, mexicanCheckBox, indianCheckBox, middleEasternCheckBox;

    private int ingredientCount = 1;
    private List<TextInputEditText> ingredientEditTexts = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_search);

        // Initialize views
        ingredientsContainer = findViewById(R.id.ingredientsContainer);
        addIngredientButton = findViewById(R.id.addIngredientButton);
        searchRecipesButton = findViewById(R.id.searchRecipesButton);
        dietaryRadioGroup = findViewById(R.id.dietaryRadioGroup);
        halalRadioButton = findViewById(R.id.halalRadioButton);
        allRadioButton = findViewById(R.id.allRadioButton);

        // Initialize cuisine checkboxes
        americanCheckBox = findViewById(R.id.americanCheckBox);
        asianCheckBox = findViewById(R.id.asianCheckBox);
        italianCheckBox = findViewById(R.id.italianCheckBox);
        mexicanCheckBox = findViewById(R.id.mexicanCheckBox);
        indianCheckBox = findViewById(R.id.indianCheckBox);
        middleEasternCheckBox = findViewById(R.id.middleEasternCheckBox);

        // Add the first ingredient EditText to the list
        TextInputEditText firstIngredient = findViewById(R.id.ingredient1EditText);
        ingredientEditTexts.add(firstIngredient);

        // Set up add ingredient button
        addIngredientButton.setOnClickListener(v -> addIngredientField());

        // Set up search button
        searchRecipesButton.setOnClickListener(v -> searchRecipes());
    }

    private void addIngredientField() {
        ingredientCount++;

        // Create a new TextInputLayout
        TextInputLayout textInputLayout = new TextInputLayout(this);
        textInputLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        textInputLayout.setStyle(com.google.android.material.R.style.Widget_MaterialComponents_TextInputLayout_OutlinedBox);
        textInputLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        ((LinearLayout.LayoutParams) textInputLayout.getLayoutParams()).setMargins(0, 0, 0, 32);

        // Create a new TextInputEditText
        TextInputEditText editText = new TextInputEditText(textInputLayout.getContext());
        editText.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        editText.setHint("Ingredient " + ingredientCount);

        // Add EditText to TextInputLayout
        textInputLayout.addView(editText);

        // Add TextInputLayout to container
        ingredientsContainer.addView(textInputLayout);

        // Add EditText to list for later retrieval
        ingredientEditTexts.add(editText);
    }

    private void searchRecipes() {
        // Get ingredients from EditTexts
        List<String> ingredients = new ArrayList<>();
        for (TextInputEditText editText : ingredientEditTexts) {
            String ingredient = editText.getText().toString().trim();
            if (!TextUtils.isEmpty(ingredient)) {
                ingredients.add(ingredient);
            }
        }

        if (ingredients.isEmpty()) {
            Toast.makeText(this, "Please enter at least one ingredient", Toast.LENGTH_SHORT).show();
            return;
        }

        // Join ingredients with comma
        String ingredientsQuery = TextUtils.join(",", ingredients);

        // Get dietary preference
        String healthParam = halalRadioButton.isChecked() ? "halal" : "";

        // Get selected cuisines
        List<String> selectedCuisines = new ArrayList<>();
        if (americanCheckBox.isChecked()) selectedCuisines.add("american");
        if (asianCheckBox.isChecked()) selectedCuisines.add("asian");
        if (italianCheckBox.isChecked()) selectedCuisines.add("italian");
        if (mexicanCheckBox.isChecked()) selectedCuisines.add("mexican");
        if (indianCheckBox.isChecked()) selectedCuisines.add("indian");
        if (middleEasternCheckBox.isChecked()) selectedCuisines.add("middle_eastern");

        // Join cuisines with comma
        String cuisineParam = selectedCuisines.isEmpty() ? "" : TextUtils.join(",", selectedCuisines);

        // Show loading indicator
        Toast.makeText(this, "Searching for recipes...", Toast.LENGTH_SHORT).show();

        // Make API call
        EdamamApi edamamApi = RetrofitClient.getClient().create(EdamamApi.class);
        Call<RecipeResponse> call = edamamApi.searchRecipes(
                ingredientsQuery,
                APP_ID,
                APP_KEY,
                cuisineParam,
                healthParam
        );

        call.enqueue(new Callback<RecipeResponse>() {
            @Override
            public void onResponse(Call<RecipeResponse> call, Response<RecipeResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    RecipeResponse recipeResponse = response.body();
                    if (recipeResponse.getHits() != null && !recipeResponse.getHits().isEmpty()) {
                        // Get the first recipe for now (in a real app, you'd show a list)
                        Recipe recipe = recipeResponse.getHits().get(0).getRecipe();
                        
                        // Navigate to recipe detail screen
                        Intent intent = new Intent(RecipeSearchActivity.this, RecipeDetailActivity.class);
                        intent.putExtra("RECIPE", (Serializable) recipe);
                        startActivity(intent);
                    } else {
                        Toast.makeText(RecipeSearchActivity.this, "No recipes found with these ingredients", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(RecipeSearchActivity.this, "Error fetching recipes", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RecipeResponse> call, Throwable t) {
                Toast.makeText(RecipeSearchActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}