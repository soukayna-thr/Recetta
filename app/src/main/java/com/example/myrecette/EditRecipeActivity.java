package com.example.myrecette;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.io.IOException;

public class EditRecipeActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private EditText etRecipeName, etIngredients, etSteps;
    private ImageView ivRecipePhoto;
    private Uri recipeImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_recipe);

        etRecipeName = findViewById(R.id.edit_recipe_name);
        etIngredients = findViewById(R.id.edit_ingredients);
        etSteps = findViewById(R.id.edit_steps);
        ivRecipePhoto = findViewById(R.id.edit_recipe_photo);
        Button btnSaveRecipe = findViewById(R.id.save_changes);
        Button btnChangePhoto = findViewById(R.id.change_photo);
        String recipeName = getIntent().getStringExtra("RECIPE_NAME");
        String recipeIngredients = getIntent().getStringExtra("RECIPE_INGREDIENTS");
        String recipeSteps = getIntent().getStringExtra("RECIPE_STEPS");
        String recipePhotoUri = getIntent().getStringExtra("RECIPE_PHOTO_URI");
        etRecipeName.setText(recipeName);
        etIngredients.setText(recipeIngredients);
        etSteps.setText(recipeSteps);

        if (recipePhotoUri != null) {
            recipeImageUri = Uri.parse(recipePhotoUri);
            ivRecipePhoto.setImageURI(recipeImageUri);
        } else {
            ivRecipePhoto.setImageResource(android.R.drawable.ic_menu_gallery);
        }

        btnChangePhoto.setOnClickListener(v -> openGallery());
        btnSaveRecipe.setOnClickListener(v -> saveRecipeChanges(recipeName));
    }
    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            recipeImageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), recipeImageUri);
                ivRecipePhoto.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Erreur lors du chargement de l'image", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void saveRecipeChanges(String oldName) {
        SharedPreferences sharedPreferences = getSharedPreferences("RecettesPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        String newName = etRecipeName.getText().toString().trim();
        String ingredients = etIngredients.getText().toString().trim();
        String steps = etSteps.getText().toString().trim();

        if (newName.isEmpty() || ingredients.isEmpty() || steps.isEmpty()) {
            Toast.makeText(this, "Tous les champs doivent être remplis", Toast.LENGTH_SHORT).show();
            return;
        }

        editor.putString(newName + "_ingredients", ingredients);
        editor.putString(newName + "_steps", steps);

        if (recipeImageUri != null) {
            editor.putString(newName + "_photo", recipeImageUri.toString());
        }
        if (!oldName.equals(newName)) {
            editor.remove(oldName + "_ingredients");
            editor.remove(oldName + "_steps");
            editor.remove(oldName + "_photo");
        }

        if (editor.commit()) {
            Toast.makeText(this, "Recette modifiée avec succès", Toast.LENGTH_SHORT).show();
            finish(); // Retourner à l'écran précédent
        } else {
            Toast.makeText(this, "Erreur lors de la modification", Toast.LENGTH_SHORT).show();
        }
    }
}
