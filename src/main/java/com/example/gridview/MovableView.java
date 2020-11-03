package com.example.gridview;

import android.content.Context;
import android.view.View;

public class MovableView extends View {
    int X;
    int Y;
    public MovableView(Context context) {
        super(context);
    }
    public MovableView(Context context, int x, int y){
        super(context);
        X = x;
        Y = y;
    }
}
