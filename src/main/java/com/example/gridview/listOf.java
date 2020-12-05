package com.example.gridview;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.transition.Explode;

public class listOf extends AppCompatActivity {
    String[] theLevels;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar_list);
        setSupportActionBar(myToolbar);

        ActionBar theBar = getSupportActionBar();
        theBar.setDisplayHomeAsUpEnabled(true);
        theBar.setTitle("Chose Level");

        getWindow().setExitTransition(new Explode());

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.level_view);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        Intent theIntent = getIntent();
        theLevels = theIntent.getStringArrayExtra("list");
        recyclerView.setAdapter(new PopulateList(theLevels, new Payload() {
            @Override
            public void setPayload(int i) {
                System.out.println(i);
                Intent theResults = new Intent();
                theResults.putExtra("index", i);
                setResult(1, theResults);
                finish();
            }
        }));

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

}

