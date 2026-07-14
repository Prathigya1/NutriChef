package com.example.nutrichef;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import java.util.*;

public class NutritionCalculatorActivity extends AppCompatActivity {

    // per-100g nutrition values: calories, protein, carbs, fat
    private static final LinkedHashMap<String, Nutrition> FOOD_DB = createFoodDb();

    private LinearLayout ingredientsContainer;
    private Button btnAddIngredient, btnCalculate, btnDone;
    private TextView tvCalories, tvProtein, tvCarbs, tvFat;

    private Totals lastTotals = new Totals();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nutrition_calculator);

        ingredientsContainer = findViewById(R.id.ingredientsContainer);
        btnAddIngredient = findViewById(R.id.btnAddIngredient);
        btnCalculate = findViewById(R.id.btnCalculate);
        btnDone = findViewById(R.id.btnDone);
        tvCalories = findViewById(R.id.tvCalories);
        tvProtein = findViewById(R.id.tvProtein);
        tvCarbs = findViewById(R.id.tvCarbs);
        tvFat = findViewById(R.id.tvFat);

        btnAddIngredient.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) { addIngredientRow(null, 0); }
        });

        btnCalculate.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) { calculateTotals(); }
        });

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                calculateTotals(); // ensure latest
                Intent result = new Intent();
                result.putExtra("tot_calories", lastTotals.calories);
                result.putExtra("tot_protein", lastTotals.protein);
                result.putExtra("tot_carbs", lastTotals.carbs);
                result.putExtra("tot_fat", lastTotals.fat);
                setResult(Activity.RESULT_OK, result);
                finish();
            }
        });

        // start with one row
        addIngredientRow(null, 0);
    }

    /**
     * Add a dynamic ingredient row.
     * If preselectName != null, it will select that food and set grams.
     */
    private void addIngredientRow(String preselectName, int gramsValue) {
        // Horizontal container for row
        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setGravity(Gravity.CENTER_VERTICAL);
        LinearLayout.LayoutParams rowParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        rowParams.topMargin = dpToPx(8);
        row.setLayoutParams(rowParams);

        // Spinner for food
        final Spinner spinner = new Spinner(this);
        LinearLayout.LayoutParams spParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 2f);
        spinner.setLayoutParams(spParams);
        List<String> foodNames = new ArrayList<>(FOOD_DB.keySet());
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, foodNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        row.addView(spinner);

        // EditText for grams
        final EditText etGrams = new EditText(this);
        LinearLayout.LayoutParams etParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
        etParams.leftMargin = dpToPx(8);
        etGrams.setLayoutParams(etParams);
        etGrams.setHint("g");
        etGrams.setInputType(android.text.InputType.TYPE_CLASS_NUMBER | android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL);
        row.addView(etGrams);

        // Row calories TextView
        final TextView tvRowCalories = new TextView(this);
        LinearLayout.LayoutParams tvParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        tvParams.leftMargin = dpToPx(8);
        tvRowCalories.setLayoutParams(tvParams);
        tvRowCalories.setText("0 kcal");
        row.addView(tvRowCalories);
        // Remove button
        ImageButton btnRemove = new ImageButton(this);
        LinearLayout.LayoutParams btnParams = new LinearLayout.LayoutParams(dpToPx(40), dpToPx(40));
        btnParams.leftMargin = dpToPx(8);
        btnRemove.setLayoutParams(btnParams);
        btnRemove.setImageResource(android.R.drawable.ic_menu_close_clear_cancel);

        // set selectableItemBackgroundBorderless as background properly
        TypedValue outValue = new TypedValue();
        boolean resolved = getTheme().resolveAttribute(android.R.attr.selectableItemBackgroundBorderless, outValue, true);
        if (resolved) {
            btnRemove.setBackgroundResource(outValue.resourceId);
        } else {
            btnRemove.setBackgroundResource(0);
        }

        row.addView(btnRemove);

        // Place preselected values if provided
        if (preselectName != null) {
            int idx = foodNames.indexOf(preselectName);
            if (idx >= 0) spinner.setSelection(idx);
            etGrams.setText(String.valueOf(gramsValue));
        }

        // Update function for this row
        final Runnable updateRow = new Runnable() {
            @Override public void run() {
                String selected = (String) spinner.getSelectedItem();
                if (selected == null) return;
                double grams = parseDoubleSafe(etGrams.getText().toString());
                Nutrition n = FOOD_DB.get(selected);
                if (n == null) return;
                double kcal = n.calories * (grams / 100.0);
                tvRowCalories.setText(String.format(Locale.getDefault(), "%.0f kcal", kcal));
            }
        };

        // listeners
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> parent, View view, int position, long id) { updateRow.run(); }
            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });

        etGrams.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override public void afterTextChanged(Editable s) { updateRow.run(); }
        });

        btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) { ingredientsContainer.removeView(row); }
        });

        ingredientsContainer.addView(row);
    }

    private void calculateTotals() {
        double totalCalories = 0.0;
        double totalProtein = 0.0;
        double totalCarbs = 0.0;
        double totalFat = 0.0;

        for (int i = 0; i < ingredientsContainer.getChildCount(); i++) {
            View child = ingredientsContainer.getChildAt(i);
            if (!(child instanceof LinearLayout)) continue;
            LinearLayout row = (LinearLayout) child;

            Spinner spinner = findSpinnerInRow(row);
            EditText etGrams = findEditTextInRow(row);

            if (spinner == null || etGrams == null) continue;
            String selected = (String) spinner.getSelectedItem();
            if (selected == null) continue;
            Nutrition n = FOOD_DB.get(selected);
            if (n == null) continue;

            double grams = parseDoubleSafe(etGrams.getText().toString());
            double m = grams / 100.0;
            totalCalories += n.calories * m;
            totalProtein += n.protein * m;
            totalCarbs += n.carbs * m;
            totalFat += n.fat * m;
        }

        lastTotals.calories = totalCalories;
        lastTotals.protein = totalProtein;
        lastTotals.carbs = totalCarbs;
        lastTotals.fat = totalFat;

        tvCalories.setText(String.format(Locale.getDefault(), "Calories: %.0f kcal", totalCalories));
        tvProtein.setText(String.format(Locale.getDefault(), "Protein: %.1f g", totalProtein));
        tvCarbs.setText(String.format(Locale.getDefault(), "Carbs: %.1f g", totalCarbs));
        tvFat.setText(String.format(Locale.getDefault(), "Fat: %.1f g", totalFat));
    }

    // helpers

    private static Spinner findSpinnerInRow(LinearLayout row) {
        for (int j = 0; j < row.getChildCount(); j++) {
            View v = row.getChildAt(j);
            if (v instanceof Spinner) return (Spinner) v;
        }
        return null;
    }

    private static EditText findEditTextInRow(LinearLayout row) {
        for (int j = 0; j < row.getChildCount(); j++) {
            View v = row.getChildAt(j);
            if (v instanceof EditText) return (EditText) v;
        }
        return null;
    }

    private static double parseDoubleSafe(String s) {
        if (s == null) return 0.0;
        s = s.trim();
        if (s.isEmpty()) return 0.0;
        try { return Double.parseDouble(s); } catch (NumberFormatException e) { return 0.0; }
    }

    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }

    private static LinkedHashMap<String, Nutrition> createFoodDb() {
        LinkedHashMap<String, Nutrition> m = new LinkedHashMap<>();
        m.put("Chicken breast (100g)", new Nutrition(165.0, 31.0, 0.0, 3.6));
        m.put("Rice (cooked) (100g)", new Nutrition(130.0, 2.7, 28.0, 0.3));
        m.put("Egg (100g)", new Nutrition(155.0, 13.0, 1.1, 11.0));
        m.put("Olive oil (per 100g)", new Nutrition(884.0, 0.0, 0.0, 100.0));
        m.put("Potato (100g)", new Nutrition(77.0, 2.0, 17.0, 0.1));
        m.put("Broccoli (100g)", new Nutrition(34.0, 2.8, 6.6, 0.4));
        m.put("Almonds (100g)", new Nutrition(579.0, 21.0, 22.0, 50.0));
        return m;
    }

    private static class Nutrition {
        final double calories, protein, carbs, fat;
        Nutrition(double c, double p, double ca, double f) { calories = c; protein = p; carbs = ca; fat = f; }
    }

    private static class Totals {
        double calories = 0.0, protein = 0.0, carbs = 0.0, fat = 0.0;
    }
}



