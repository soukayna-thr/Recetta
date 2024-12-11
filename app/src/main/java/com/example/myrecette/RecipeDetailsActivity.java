package com.example.myrecette;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class RecipeDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);
        String name = getIntent().getStringExtra("RECIPE_NAME");
        String ingredients = getIntent().getStringExtra("RECIPE_INGREDIENTS");
        String steps = getIntent().getStringExtra("RECIPE_STEPS");
        String photoUri = getIntent().getStringExtra("RECIPE_PHOTO_URI");
        TextView recipeNameText = findViewById(R.id.tvRecipeName);
        TextView recipeIngredientsText = findViewById(R.id.tvIngredients);
        TextView recipeStepsText = findViewById(R.id.tvSteps);
        ImageView recipePhoto = findViewById(R.id.ivRecipePhoto);
        Button deleteButton = findViewById(R.id.btnDeleteRecipe);
        Button editButton = findViewById(R.id.btnEditRecipe);
        recipeNameText.setText(name);
        recipeIngredientsText.setText(ingredients);
        recipeStepsText.setText(steps);

        if (photoUri != null && !photoUri.isEmpty()) {
            recipePhoto.setImageURI(android.net.Uri.parse(photoUri));
        } else {
            recipePhoto.setImageResource(android.R.drawable.ic_menu_gallery);
        }
        deleteButton.setOnClickListener(v -> deleteRecipe(name));
        editButton.setOnClickListener(v -> {
            Intent intent = new Intent(RecipeDetailsActivity.this, EditRecipeActivity.class);
            intent.putExtra("RECIPE_NAME", name);
            intent.putExtra("RECIPE_INGREDIENTS", ingredients);
            intent.putExtra("RECIPE_STEPS", steps);
            startActivity(intent);
        });
    }

    private void deleteRecipe(String recipeName) {
        SharedPreferences sharedPreferences = getSharedPreferences("RecettesPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.remove(recipeName + "_ingredients");
        editor.remove(recipeName + "_steps");
        editor.remove(recipeName + "_photo");

        if (editor.commit()) {
            Toast.makeText(this, "Recette supprimée avec succès", Toast.LENGTH_SHORT).show();
            finish(); // Retourner à l'écran précédent
        } else {
            Toast.makeText(this, "Erreur lors de la suppression de la recette", Toast.LENGTH_SHORT).show();
        }
    }
}
