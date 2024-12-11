package com.example.myrecette;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    private List<Recipe> recipeList;
    private OnRecipeClickListener listener;
    public interface OnRecipeClickListener {
        void onRecipeClick(Recipe recipe);
    }
    public RecipeAdapter(List<Recipe> recipeList, OnRecipeClickListener listener) {
        this.recipeList = recipeList;
        this.listener = listener;
    }
    public static class RecipeViewHolder extends RecyclerView.ViewHolder {
        public TextView recipeTitle;
        public TextView recipePreview;
        public ImageView recipeImage;
        public RecipeViewHolder(View view) {
            super(view);
            recipeTitle = view.findViewById(R.id.tv_recipe_title);
            recipePreview = view.findViewById(R.id.tv_recipe_preview);
            recipeImage = view.findViewById(R.id.ivRecipeThumbnail);
        }
        public void bind(final Recipe recipe, final OnRecipeClickListener listener) {
            recipeTitle.setText(recipe.getName());
            recipePreview.setText(recipe.getIngredients().split("\n")[0] + "...");

            if (recipe.getPhotoUri() != null && !recipe.getPhotoUri().isEmpty()) {
                recipeImage.setImageURI(Uri.parse(recipe.getPhotoUri()));
            } else {
                recipeImage.setImageResource(android.R.drawable.ic_menu_gallery);
            }

            itemView.setOnClickListener(v -> listener.onRecipeClick(recipe));
        }
    }

    @Override
    public RecipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recipe, parent, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecipeViewHolder holder, int position) {
        holder.bind(recipeList.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }
}
