package com.example.nutrichef;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Locale;

public class RecipeActivity extends AppCompatActivity {

        private static final String TAG = "RecipeActivity";
        RecyclerView recyclerView;
        ArrayList<Recipe> recipeList;
        RecipeAdapter adapter;
        SearchView searchView;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_recipe);

                recyclerView = findViewById(R.id.recipeRecycler);
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
                searchView = findViewById(R.id.searchView);

                recipeList = new ArrayList<>();

                recipeList.add(new Recipe(
                        "Vegetable Khichdi",
                        "• Rice\n• Moong Dal\n• Carrot\n• Peas\n• Turmeric\n• Salt\n• Ghee",
                        "1. Wash rice and dal.\n2. Add chopped veggies.\n3. Pressure cook for 3 whistles.\n4. Add ghee and mix well.",
                        "Calories: 280 kcal\nProtein: 10g\nCarbs: 40g\nFat: 7g",
                        R.drawable.veg_khichdi
                ));

                recipeList.add(new Recipe(
                        "Hakka Noodles",
                        "• Noodles\n• Capsicum\n• Cabbage\n• Soy Sauce\n• Chili Sauce\n• Garlic",
                        "1. Boil noodles.\n2. Stir-fry garlic and veggies.\n3. Add sauces.\n4. Mix noodles and toss.",
                        "Calories: 350 kcal\nProtein: 9g\nCarbs: 55g\nFat: 8g",
                        R.drawable.hakka_noodles
                ));

                recipeList.add(new Recipe(
                        "Chicken Salad",
                        "• Grilled Chicken\n• Lettuce\n• Cucumber\n• Olive Oil\n• Lemon\n• Pepper",
                        "1. Grill chicken and slice.\n2. Toss veggies and chicken.\n3. Add lemon dressing.",
                        "Calories: 220 kcal\nProtein: 25g\nCarbs: 8g\nFat: 10g",
                        R.drawable.chicken_salad
                ));

                recipeList.add(new Recipe(
                        "Masala Omelette",
                        "• 2 Eggs\n• Onion\n• Tomato\n• Coriander\n• Green Chilli",
                        "1. Beat eggs.\n2. Add chopped veggies.\n3. Cook both sides.",
                        "Calories: 180 kcal\nProtein: 12g\nCarbs: 3g\nFat: 12g",
                        R.drawable.masala_omelette
                ));

                recipeList.add(new Recipe(
                        "Dal Tadka",
                        "• Toor Dal\n• Ghee\n• Garlic\n• Cumin\n• Onion\n• Tomato",
                        "1. Boil dal.\n2. Prepare tadka.\n3. Pour tadka over dal.\n4. Simmer 5 mins.",
                        "Calories: 260 kcal\nProtein: 13g\nCarbs: 30g\nFat: 9g",
                        R.drawable.dal_tadka
                ));

                recipeList.add(new Recipe(
                        "Chole Masala",
                        "• Chickpeas\n• Onion\n• Tomato\n• Chole Masala",
                        "1. Pressure cook chickpeas.\n2. Make onion-tomato gravy.\n3. Add chole and cook.",
                        "Calories: 340 kcal\nProtein: 14g\nCarbs: 45g\nFat: 9g",
                        R.drawable.chole_masala
                ));

                recipeList.add(new Recipe(
                        "Veg Sandwich",
                        "• Bread\n• Cucumber\n• Tomato\n• Cheese\n• Butter",
                        "1. Chop veggies.\n2. Layer on bread.\n3. Grill if desired.",
                        "Calories: 230 kcal\nProtein: 8g\nCarbs: 28g\nFat: 9g",
                        R.drawable.veg_sandwich
                ));

                adapter = new RecipeAdapter(recipeList);
                recyclerView.setAdapter(adapter);

                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                        @Override
                        public boolean onQueryTextSubmit(String query) {
                                filterList(query);
                                return true;
                        }

                        @Override
                        public boolean onQueryTextChange(String newText) {
                                filterList(newText);
                                return true;
                        }
                });
        }

        private void filterList(String query) {
                if (query == null) query = "";

                String q = query.trim().toLowerCase(Locale.ROOT);
                ArrayList<Recipe> filtered = new ArrayList<>();

                for (Recipe recipe : recipeList) {
                        if (recipe.getTitle().toLowerCase(Locale.ROOT).contains(q)) {
                                filtered.add(recipe);
                        }
                }

                if (q.isEmpty()) {
                        adapter = new RecipeAdapter(recipeList);
                } else {
                        adapter = new RecipeAdapter(filtered);
                }

                recyclerView.setAdapter(adapter);
                Log.d(TAG, "Results found: " + filtered.size());
        }
}