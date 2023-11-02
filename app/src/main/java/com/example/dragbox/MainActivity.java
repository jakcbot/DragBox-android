package com.example.dragbox;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private FrameLayout container;
    private ArrayList<BoxView> boxes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        container = findViewById(R.id.container);

        container.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    float x = motionEvent.getX();
                    float y = motionEvent.getY();

                    // Check if tapped on any existing box
                    boolean tappedOnBox = false;
                    for (BoxView box : boxes) {
                        if (box.isInside(x, y)) {
                            tappedOnBox = true;
                            break;
                        }
                    }

                    if (!tappedOnBox) {
                        createBox(x, y);
                    }
                }
                return true;
            }
        });
    }

    private void createBox(float x, float y) {
        BoxView boxView = new BoxView(this);
        container.addView(boxView);

        // Set initial position
        boxView.setX(x - boxView.getWidth() / 2);
        boxView.setY(y - boxView.getHeight() / 2);

        boxes.add(boxView);
    }

    public class BoxView extends View {

        private float lastX, lastY;

        public BoxView(Context context) {
            super(context);
            int size = (int) (Math.min(container.getWidth(), container.getHeight()) * 0.1);
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(size, size);
            setLayoutParams(params);
            setBackgroundColor(Color.BLUE);
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    // Save the initial touch point
                    lastX = event.getRawX();
                    lastY = event.getRawY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    float deltaX = event.getRawX() - lastX;
                    float deltaY = event.getRawY() - lastY;

                    // Update the box position
                    float newX = getX() + deltaX;
                    float newY = getY() + deltaY;

                    // Restrict movement within window boundaries
                    newX = Math.max(0, Math.min(newX, container.getWidth() - getWidth()));
                    newY = Math.max(0, Math.min(newY, container.getHeight() - getHeight()));

                    setX(newX);
                    setY(newY);

                    // Save the current touch point for the next move event
                    lastX = event.getRawX();
                    lastY = event.getRawY();
                    break;
            }
            return true;
        }

        public boolean isInside(float x, float y) {
            return x >= getX() && x <= getX() + getWidth() && y >= getY() && y <= getY() + getHeight();
        }
    }
}
