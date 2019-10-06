package com.gomez.mealz;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    public List<HitsModel> recipeList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }


    public void searchForRecipe(View view){
        // Do something in response to button
        EditText editText = (EditText) findViewById(R.id.editText);
        String message = editText.getText().toString();

        String url = "https://api.edamam.com/search?app_id=736dba64&app_key=8b9c2a666b2c005a8c34b35a26063330&q=" + message;

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            recipeList = responseToRecipeList(response);
                            if (recipeList.size() > 0) {
                                for (int i = 0; i < recipeList.size(); i++) {
                                    System.out.println(recipeList.get(i).getRecipe().getLabel());
                                    System.out.println(recipeList.get(i).getRecipe().getIngredientLines());
                                }
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Rest Response", error.toString());
                    }
                }
        );
        requestQueue.add(stringRequest);

    }

    public List<HitsModel> responseToRecipeList(String response) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        ResponseModel r = objectMapper.readValue(response, ResponseModel.class);
        List<HitsModel> recipeList = r.getHits();
        return recipeList;
    }
}
