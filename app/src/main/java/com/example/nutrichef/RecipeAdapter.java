package com.example.nutrichef;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder> {

    private static final String TAG = "RecipeAdapter";
    private ArrayList<Recipe> list;

    public RecipeAdapter(ArrayList<Recipe> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recipe, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Recipe recipe = list.get(position);

        holder.textRecipeName.setText(recipe.getTitle());
        try {
            holder.recipeImage.setImageResource(recipe.getImageResId());
        } catch (Exception e) {
            holder.recipeImage.setImageResource(R.drawable.ic_launcher_background);
        }

        holder.itemView.setOnClickListener(v -> {
            try {
                Intent intent = new Intent(v.getContext(), RecipeDetails.class);
                intent.putExtra("title", recipe.getTitle());
                intent.putExtra("ingredients", recipe.getIngredients());
                intent.putExtra("steps", recipe.getSteps());
                intent.putExtra("image", recipe.getImageResId());
                intent.putExtra("nutrition", recipe.getNutrition());
                Log.d(TAG, "Launching RecipeDetails for: " + recipe.getTitle() + " imageId=" + recipe.getImageResId());
                v.getContext().startActivity(intent);
            } catch (Exception e) {
                Log.e(TAG, "Error launching RecipeDetails", e);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textRecipeName;
        ImageView recipeImage;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textRecipeName = itemView.findViewById(R.id.textRecipeName);
            recipeImage = itemView.findViewById(R.id.recipeImage);
        }
    }
}


