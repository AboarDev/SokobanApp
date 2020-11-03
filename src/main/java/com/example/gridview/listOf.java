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
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
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

        recyclerView = (RecyclerView) findViewById(R.id.level_view);

        //recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        Intent theIntent = getIntent();
        theLevels = theIntent.getStringArrayExtra("list");
        int theX = theIntent.getIntExtra("index",0);
        mAdapter = new PopulateList(theLevels,new Payload() {

            @Override
            public void setPayload(int i) {
                System.out.println(i);
                Intent theResults = new Intent();
                theResults.putExtra("index",i);
                setResult(1,theResults);
                finish();
            }
        });
        recyclerView.setAdapter(mAdapter);

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

