package com.example.gridview;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.lifecycle.ViewModelProvider;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.Intent;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import sokoban.*;

public class GameActivity extends AppCompatActivity {
    int[] colorIndex;
    char[] charIndex;
    int squareSize;
    int[] ids;
    SokobanViewModel viewModel;
    List<View> rows;
    View thePlayer;
    List<MovableView> theCrates;
    Boolean canMove;
    Timer timer;
    public GameActivity(){
        charIndex = new char[]{'#', '.', '+', 'x', 'w','X','W'};
        colorIndex = new int[]{0xFF414141, 0xFFd4d4d4, 0xFF737048, 0xBC28549c, 0xBC82c142,0xBC28549c};
        rows = null;
        canMove = true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //setup screen
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //create toolbar
        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        viewModel = new ViewModelProvider(this).get(SokobanViewModel.class);
        if (!viewModel.dataAdded) {
            viewModel.levelData = parseLevels("levels.csv");
        }
        loadLevel(viewModel.levelIndex);
        ids = new int[]{R.id.up,R.id.down,R.id.left,R.id.right};
        startTimer();
    }
    @Override
    protected void onPause () {
        super.onPause();
        viewModel.pause();
    }
    @Override
    protected void onResume () {
        super.onResume();
        viewModel.resume();
    }
    @Override
    protected void onDestroy (){
        super.onDestroy();
    }
    public void loadLevel(int id){
        if(rows != null){
            viewModel.seconds = -1;
            viewModel.minutes = 0;
            for (View aView: rows){
                ((ViewGroup) aView.getParent()).removeView(aView);
            }
            ConstraintLayout mainLayout = findViewById(R.id.main_activity_layout);
            mainLayout.removeView(thePlayer);
            for(View v: theCrates){
                mainLayout.removeView(v);
            }
        }
        List<String> theLevel = viewModel.levelData.get(id);
        String theName = theLevel.get(3);
        if(!viewModel.getLevelByString(theName)){
            viewModel.addLevel(theLevel);
        }
        Objects.requireNonNull(getSupportActionBar()).setTitle(theName);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;
        int theWidth = viewModel.getLevelWidth();
        int theHeight = viewModel.getLevelHeight();
        int viewWidth;
        if (height > width){
            if (theWidth > theHeight){
                System.out.println(theWidth);
                viewWidth = (width - 64) / theWidth;
            }
            else{
                viewWidth = (height / 2) / theHeight;
            }
        }
        else {
            viewWidth = (height - height/4) / theWidth;
        }
        squareSize = viewWidth;
        rows = makeLevel(viewModel.getLevelSchema(),theWidth,
                theHeight, viewWidth);
    }

    public List<List<String>> parseLevels (String file) {
        List<List<String>> allLevels = new ArrayList<>();
        try {
            BufferedReader reader;
            reader = new BufferedReader(new InputStreamReader(getAssets().open(file)));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] levels = line.split("\n");
                for (String aLevel: levels){
                    List<String> asList = Arrays.asList(aLevel.split(","));
                    allLevels.add(asList);
                }
            }
        } catch (IOException ex){
            ex.printStackTrace();
        }
        return allLevels;
    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && data != null){
            int X = data.getIntExtra("index",-1);
            if (X != viewModel.levelIndex){
                viewModel.levelIndex = X;
                loadLevel(X);
            } else{
                loadLevel(viewModel.levelIndex);
            }
        }
    }

    public void openList(MenuItem item) {
        Intent theIntent = new Intent(this, listOf.class);
        theIntent.putExtra("list", viewModel.getLevels());
        theIntent.putExtra("current", viewModel.levelIndex);
        startActivityForResult(theIntent,1);
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.themenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public List<View> makeLevel (String inp,int theWidth, int theHeight, int viewWidth) {

        ConstraintLayout mainLayout = findViewById(R.id.main_activity_layout);
        List<View> theRows = new ArrayList<>();
        theCrates = new ArrayList<>();
        int colorInt;
        TableLayout aTable = findViewById(R.id.sokobanGrid);
        if (viewModel.paused){
            aTable.setVisibility(View.INVISIBLE);
        }
        TableRow.LayoutParams theParams = new TableRow.LayoutParams(viewWidth, viewWidth);
        int count = 0;
        for (int y = 0; y < theHeight ;y++) {
            TableRow aRow = new TableRow(this);
            aRow.setId(View.generateViewId());
            aTable.addView(aRow);
            theRows.add(aRow);
            for (int x = 0; x < theWidth; x++) {
                char theChar = inp.charAt(count);
                int theindex = 1;
                for (int charId = 0; charId < charIndex.length; charId++){
                    if (charIndex[charId] == theChar){
                        theindex = charId;
                    }
                }
                colorInt = colorIndex[theindex];

                if (theindex == 4 || theindex == 3 || theindex == 5|| theindex == 6){
                    MovableView newView = new MovableView(this,x,y);
                    newView.setId(View.generateViewId());
                    newView.setBackgroundColor(colorInt);
                    newView.setLayoutParams(new ConstraintLayout.LayoutParams(viewWidth,viewWidth));
                    mainLayout.addView(newView,0);

                    ConstraintSet theSet = new ConstraintSet();
                    theSet.clone(mainLayout);
                    theSet.connect(newView.getId(), ConstraintSet.TOP, R.id.sokobanGrid,
                            ConstraintSet.TOP, y * viewWidth);
                    theSet.connect(newView.getId(), ConstraintSet.LEFT, R.id.sokobanGrid,
                            ConstraintSet.LEFT, x * viewWidth);
                    theSet.applyTo(mainLayout);

                    newView.bringToFront();
                    if (viewModel.paused) {
                        newView.setVisibility(View.INVISIBLE);
                    }
                    if (theindex == 4){
                        thePlayer = newView;
                    }else {
                        theCrates.add(newView);
                    }
                    if(theindex == 5|| theindex == 6){
                        colorInt = colorIndex[2];
                    } else {
                        colorInt = colorIndex[1];
                    }

                }
                View aView = makeView(colorInt);
                aView.setLayoutParams(theParams);
                aView.setId(View.generateViewId());
                aRow.addView(aView);
                count++;
            }
        }
        updateLabels();
        return theRows;
    }

    public View makeView (int color) {
        View theView = new View(this);
        theView.setId(View.generateViewId());
        theView.setBackgroundColor(color);
        return theView;
    }

    public void onArrowClick(View view){
        //check if move animation is finished
        if (canMove) {
            Direction theDirection = null;
            Character directionStr = null;
            int dimension = this.squareSize;
            int id = 0;
            int count = 0;
            //find the id of the relevant direction
            for (int x : ids) {
                if (x == view.getId()) {
                    id = count;
                }
                count++;
            }
            //determine direction
            switch (id) {
                case 0:
                    directionStr = 'y';
                    theDirection = Direction.UP;
                    dimension = dimension * -1;
                    break;
                case 1:
                    directionStr = 'y';
                    theDirection = Direction.DOWN;
                    break;
                case 2:
                    directionStr = 'x';
                    theDirection = Direction.LEFT;
                    dimension = dimension * -1;
                    break;
                case 3:
                    directionStr = 'x';
                    theDirection = Direction.RIGHT;
                    break;
            }
            //make move
            Move theMove = viewModel.move(theDirection);
            //make animation
            makeAnimation(dimension, directionStr, theMove, false);
            //change displayed info
            updateLabels();
        }

    }

    public void makeAnimation(int dimension, Character direction,Move theMove,boolean reverse){
        Animator theAnimator = null;
        if (theMove != null && theMove.hasMove) {
            int animLength;
            if (direction == 'x') {
                animLength = ((int) thePlayer.getX() + dimension);
            } else {
                animLength = ((int) thePlayer.getY() + dimension);
            }
            new ObjectAnimator();
            ObjectAnimator playerAnimator = ObjectAnimator.ofFloat(thePlayer, direction.toString(), animLength);
            playerAnimator.setDuration(200);

            if (!theMove.includesCrate) {
                theAnimator = playerAnimator;
            }else{
                for (MovableView v : theCrates){
                    if ( (v.X == theMove.worker.x && v.Y == theMove.worker.y && !reverse)
                            || (reverse && v.X == theMove.crate.x + theMove.moveX &&
                            v.Y == theMove.crate.y + theMove.moveY)
                    ){
                        if (!reverse) {
                            animLength += dimension;
                            v.X += theMove.moveX;
                            v.Y += theMove.moveY;
                        }
                        else {
                            animLength -= dimension;
                            v.X -= theMove.moveX;
                            v.Y -= theMove.moveY;
                        }
                        new ObjectAnimator();
                        ObjectAnimator crateAnimator = ObjectAnimator.ofFloat(v,
                                direction.toString(),
                                animLength);
                        crateAnimator.setDuration(200);
                        AnimatorSet theSet = new AnimatorSet();
                        theSet.playTogether(crateAnimator, playerAnimator);
                        theAnimator = theSet;
                    }
                }
            }
            theAnimator.addListener(new AnimatorListenerAdapter() {
                public void onAnimationEnd(Animator animation) {
                    canMove = true;
                }
            });
            canMove = false;
            theAnimator.start();
        }
    }

    public void onUndoClick (MenuItem item) {
        if (canMove){
            Move theMove = viewModel.undo();
            if (theMove != null && theMove.hasMove) {
                Character directionStr = null;
                int dimension = this.squareSize;
                switch (theMove.direction) {
                    case UP:
                        directionStr = 'y';
                        break;
                    case DOWN:
                        directionStr = 'y';
                        dimension = dimension * -1;
                        break;
                    case LEFT:
                        directionStr = 'x';
                        break;
                    case RIGHT:
                        directionStr = 'x';
                        dimension = dimension * -1;
                        break;
                }
                updateLabels();
                //animates undo
                makeAnimation(dimension, directionStr, theMove,true);
            }
        }
    }

    public void updateLabels () {
        TextView theCount = findViewById(R.id.theCount);
        TextView theCompleted = findViewById(R.id.theCompleted);
        theCount.setText(String.valueOf(viewModel.moves()));
        int completed = viewModel.completed();
        int total = viewModel.total();
        theCompleted.setText((completed + "/"  + total));
        if (total == completed) {
            Snackbar.make(findViewById(R.id.main_activity_layout), R.string.completed,
                    BaseTransientBottomBar.LENGTH_LONG)
                    .show();
        }
    }

    public void startTimer () {
        TextView elapsed = findViewById(R.id.timeElapsed);
        TimerTask repeatedTask = new TimerTask() {
            public void run() {
                String time = String.valueOf(viewModel.timer());
                runOnUiThread(() -> elapsed.setText(time));
            }
        };
        timer = new Timer("Timer");
        timer.scheduleAtFixedRate(repeatedTask, 0, 1000);
    }

    public void onPlayPauseClick (View view) {
        ImageButton theButton = (ImageButton) view;
        if (!viewModel.paused){
            timer.cancel();
            theButton.setImageResource(R.drawable.ic_play);
            viewModel.pause();
            findViewById(R.id.sokobanGrid).setVisibility(View.INVISIBLE);
            thePlayer.setVisibility(View.INVISIBLE);
            for (View v: theCrates){
                v.setVisibility(View.INVISIBLE);
            }
        } else {
            startTimer();
            theButton.setImageResource(R.drawable.ic_pause);
            viewModel.resume();
            findViewById(R.id.sokobanGrid).setVisibility(View.VISIBLE);
            thePlayer.setVisibility(View.VISIBLE);
            for (View v: theCrates){
                v.setVisibility(View.VISIBLE);
            }
        }
    }

    public void onRestartClick (MenuItem item){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.dialog_title)
                .setPositiveButton(R.string.ok, (dialog, id) -> {
                    dialog.dismiss();
                    viewModel.closeLevel();
                    loadLevel(viewModel.levelIndex);
                })
                .setNegativeButton(R.string.cancel, (dialog, id) -> {
                    dialog.dismiss();
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
