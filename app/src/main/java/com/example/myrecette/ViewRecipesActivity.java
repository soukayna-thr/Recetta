package com.example.myrecette;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class ViewRecipesActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecipeAdapter recipeAdapter;
    private List<Recipe> recipeList;
    private List<Recipe> filteredList;
    private Spinner spCategoryFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_recipes);
        recyclerView = findViewById(R.id.recycler_view);
        spCategoryFilter = findViewById(R.id.spCategoryFilter);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,
                new String[]{"Toutes", "Entrée", "Plat principal", "Dessert"});
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCategoryFilter.setAdapter(adapter);

        spCategoryFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String category = parent.getItemAtPosition(position).toString();
                filterRecipesByCategory(category);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        loadRecipes();
    }

    private void loadRecipes() {
        SharedPreferences sharedPreferences = getSharedPreferences("RecettesPrefs", MODE_PRIVATE);
        recipeList = new ArrayList<>();

        for (String key : sharedPreferences.getAll().keySet()) {
            if (key.endsWith("_ingredients")) {
                String name = key.substring(0, key.length() - 12);
                String ingredients = sharedPreferences.getString(name + "_ingredients", "");
                String steps = sharedPreferences.getString(name + "_steps", "");
                String photoUri = sharedPreferences.getString(name + "_photo", null);
                String category = sharedPreferences.getString(name + "_category", "Autre");

                recipeList.add(new Recipe(name, ingredients, steps, photoUri, category));
            }
        }

        if (recipeList.isEmpty()) {
            Toast.makeText(this, "Aucune recette disponible", Toast.LENGTH_SHORT).show();
        }

        filteredList = new ArrayList<>(recipeList);
        recipeAdapter = new RecipeAdapter(filteredList, recipe -> {
            Intent intent = new Intent(ViewRecipesActivity.this, RecipeDetailsActivity.class);
            intent.putExtra("RECIPE_NAME", recipe.getName());
            intent.putExtra("RECIPE_INGREDIENTS", recipe.getIngredients());
            intent.putExtra("RECIPE_STEPS", recipe.getSteps());
            intent.putExtra("RECIPE_PHOTO_URI", recipe.getPhotoUri());
            intent.putExtra("RECIPE_CATEGORY", recipe.getCategory());
            startActivity(intent);
        });

        recyclerView.setAdapter(recipeAdapter);
    }

    private void filterRecipesByCategory(String category) {
        filteredList.clear();

        if (category.equals("Toutes")) {
            filteredList.addAll(recipeList);
        } else {
            for (Recipe recipe : recipeList) {
                if (recipe.getCategory().equals(category)) {
                    filteredList.add(recipe);
                }
            }
        }

        recipeAdapter.notifyDataSetChanged();
    }
}
