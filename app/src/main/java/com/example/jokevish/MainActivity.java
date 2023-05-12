package com.example.jokevish;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {


    private RequestQueue requestQueue;
    private String jokeText;
    private int btnClickCount = 0;
    long lastClickTime = 0;
    TextView jokeBox;
    Button getBtn;
    ImageButton copyBtn, shareBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        jokeBox = findViewById(R.id.jokeBox);
        getBtn = findViewById(R.id.getBtn);
        copyBtn = findViewById(R.id.copyBtn);
        shareBtn = findViewById(R.id.shareBtn);

        requestQueue = Volley.newRequestQueue(this);
        getBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeApiCall();
                jokeBox.setText(jokeText);
                copyBtn.setVisibility(View.VISIBLE);
                shareBtn.setVisibility(View.VISIBLE);
                //System.out.println(btnClickCount);
                long currentTime = System.currentTimeMillis();
                if (lastClickTime == 0) {
                    lastClickTime = currentTime;
                }
                if(btnClickCount > 0 && (currentTime - lastClickTime) < 500) {
                //System.out.println("Woah Dude, Hold up!");
                    Toast.makeText(MainActivity.this, "Woah Dude, Hold up!", Toast.LENGTH_SHORT).show();
                } else {
                    lastClickTime = currentTime;
                    btnClickCount = 0;
                }
                btnClickCount++;
            }
        });

        // lets see 2
        // Copy Joke
        copyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText(null, jokeBox.getText());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(MainActivity.this, "Joke Copied", Toast.LENGTH_SHORT).show();
            }
        });


        // Share Joke
        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, jokeBox.getText());
                sendIntent.setType("text/plain");

                Intent shareIntent = Intent.createChooser(sendIntent, null);
                startActivity(shareIntent);
            }
        });

    }

    private void makeApiCall() {
        String apiurl = "https://icanhazdadjoke.com/slack";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, apiurl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Gson gson = new Gson();
                JokeResponse jokeResponse = gson.fromJson(response.toString(), JokeResponse.class);
                jokeText = jokeResponse.getAttachments().get(0).getText();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                jokeBox.setText("Volley Error! Retry.");
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Accept","application/json");
                return headers;
            }
        };
        requestQueue.add(jsonObjectRequest);
    }

}

//class JokeResponse {
//    private boolean ok;
//    private JokeDataResponse data;
//}

class JokeDataResponse {
    private String text;
}