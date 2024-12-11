package com.example.myrecette;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button addRecipeButton = findViewById(R.id.btnAddRecipe);
        Button viewRecipesButton = findViewById(R.id.btnViewRecipes);
        addRecipeButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddRecipeActivity.class);
            startActivity(intent);
        });
        viewRecipesButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ViewRecipesActivity.class);
            startActivity(intent);
        });
    }
}


