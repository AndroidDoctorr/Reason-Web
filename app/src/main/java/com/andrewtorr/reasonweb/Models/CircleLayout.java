package com.andrewtorr.reasonweb.Models;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.elevenfifty.reasonweb.R;

import java.util.ArrayList;

/**
 * Created by Andrew on 10/20/2015.
 *
 */
public class CircleLayout extends RelativeLayout {
    Canvas canvas;
    Bitmap bitmap;
    LayoutInflater inflater;

    //ItemType type;
    String centerText;
    String type;
    TypedArray a;
    float cRadius;
    float viewWidth;
    float viewHeight;
    Drawable centerDrawable;
    Drawable background;
    Paint paint;
    ArrayList<Float> points;

    float textScale = 16;
    float maxSize = 0;
    float density;
    boolean firstTime = true;

    ArrayList<Integer> angles;
    ArrayList<CircleItem> circles;
    int numCircles = 0;

    DisplayMetrics displayMetrics;

    String TAG = "CircleLayout: ";

    public CircleLayout(Context context) {
        super(context);
        inflater = LayoutInflater.from(context);
        init();
    }

    public CircleLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        inflater = LayoutInflater.from(context);

        a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CircleLayout, 0, 0);
        init();
    }

    public CircleLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflater = LayoutInflater.from(context);

        a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CircleLayout, 0, 0);
        init();
    }

    public void init() {
        inflater.inflate(R.layout.circle_layout, this, true);
        angles = new ArrayList<>();
        circles = new ArrayList<>();
        displayMetrics = getContext().getResources().getDisplayMetrics();
        //density = getResources().getDisplayMetrics().density;
        Log.d(TAG, "density: " + density);
        paint = new Paint();
        paint.setColor(getResources().getColor(R.color.green));
        paint.setStrokeWidth(10);

        cRadius = todp(a.getDimension(R.styleable.CircleLayout_cl_cRadius, 0));
        Log.d(TAG, "cRadius: " + cRadius);
        //type = ItemType.valueOf(a.getString(R.styleable.CircleLayout_type));
        type = a.getString(R.styleable.CircleLayout_cl_type);
        Log.d(TAG, "type: " + type);
        centerText = a.getString(R.styleable.CircleLayout_cl_center_text);
        Log.d(TAG, "center text: " + centerText);
        centerDrawable = a.getDrawable(R.styleable.CircleLayout_cl_center_drawable);

        switch (type) {
            case "Evid":
                background = ContextCompat.getDrawable(this.getContext(), R.drawable.circle_orange);
                break;
            case "Prop":
                background = ContextCompat.getDrawable(this.getContext(), R.drawable.circle_purple);
                break;
            case "Syll":
                background = ContextCompat.getDrawable(this.getContext(), R.drawable.circle_blue);
                break;
            case "Term":
                background = ContextCompat.getDrawable(this.getContext(), R.drawable.circle_green);
                break;
        }
    }

    public void setCircle(float relSize, String text, @Nullable Bitmap image) {
        CircleItem circleItem = new CircleItem();
        circleItem.setText(text);
        circleItem.setRelSize(relSize);
        circleItem.setImage(image);

        ++numCircles;

        circles.add(circleItem);
    }

    //TODO: Make this add a circle to an existing (drawn) view
    public void showCircle(float relSize, String text, @Nullable Bitmap image) {
        // Add a circle item to the layout
        // Circle Item = circular imageview, a textview on top, and a line drawn on the canvas from
        // the center of the layout to the center of the circle item.

        if (relSize > maxSize) {
            maxSize = relSize;
        } else {
            relSize = relSize/maxSize;
        }

        ++numCircles;

        //Shift all view angles, and (later) reset their parameters accordingly
        getAngles();

        int angle = angles.get(angles.size() - 1);

        //The maximum radii the circle item imageview can have vertically and horizontally
        float vertMaxRadius = viewHeight/4 - cRadius/2;
        float horizMaxRadius = viewWidth/4 - cRadius/2;
        float maxRadius = (float) (horizMaxRadius + (vertMaxRadius - horizMaxRadius) * Math.cos(angle * Math.PI / 180));
        float radius = 1 + relSize * (maxRadius - 1);

        // dist represents the distance to the CENTER of the circle item
        float minDist = cRadius + 20;
        float vertMaxDist = viewHeight/2 - radius;
        float horizMaxDist = viewWidth/2 - radius;
        float maxDist = (float) (horizMaxDist + (vertMaxDist - horizMaxDist) * Math.cos(angle * Math.PI / 180));

        float dist = (float) (minDist + relSize * (maxDist - minDist) * Math.cos(angle * Math.PI / 180));
    }

    public void setCircles(ArrayList<CircleItem> circles) {
        this.circles = circles;
    }

    public void showCircles() {
        Log.d(TAG, "showing circles");
        this.removeAllViewsInLayout();
        points = new ArrayList<>();

        // Add the center circle
        ImageView centerCircle = new ImageView(this.getContext());
        TextView centerTextView = new TextView(this.getContext());

        // Set up the center circle imageview parameters
        RelativeLayout.LayoutParams ccParams = new RelativeLayout.LayoutParams((int) topx(cRadius) * 2, (int) topx(cRadius) * 2);
        ccParams.addRule(CENTER_IN_PARENT);
        centerCircle.setLayoutParams(ccParams);
        centerCircle.setBackground(centerDrawable);

        // Set up the center circle text parameters
        RelativeLayout.LayoutParams ctParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ctParams.addRule(CENTER_IN_PARENT);
        centerTextView.setText(centerText);
        centerTextView.setLayoutParams(ctParams);

        // Add center views to the layout
        this.addView(centerCircle);
        this.addView(centerTextView);


        // Set up the outer circles
        getAngles();

        //First normalize the relative sizes by finding the maximum
        for (int i=0; i < circles.size(); i++) {
            CircleItem circle = circles.get(i);
            float relSize = circle.getRelSize();

            if (relSize > maxSize) {
                maxSize = relSize;
            }
        }

        // Then add all the circle items
        for (int i=0; i < circles.size(); i++) {
            CircleItem circle = circles.get(i);

            String text = circle.getText();
            Bitmap image = circle.getImage();
            float relSize = circle.getRelSize();
            int angle = angles.get(i);

            relSize = relSize/maxSize;

            Log.d(TAG, "adding a circle item, relSize: " + relSize + ", angle: " + angle );

            ImageView circleIv = new ImageView(this.getContext());
            TextView textView = new TextView(this.getContext());

            // The maximum radii the circle item imageview can have vertically and horizontally
            Log.d(TAG, "view dimens: " + viewHeight + ", " + viewWidth);
            float vertMaxRadius = viewHeight/4 - cRadius/2 - 30;
            Log.d(TAG, "VmaxRadius: " + vertMaxRadius);
            float horizMaxRadius = viewWidth/4 - cRadius/2 - 30;
            Log.d(TAG, "HmaxRadius: " + horizMaxRadius);
            float maxRadius = (float) (horizMaxRadius + (vertMaxRadius - horizMaxRadius) * Math.abs(Math.cos(angle * Math.PI / 180)));
            Log.d(TAG, "cos of angle: " + Math.cos(angle * Math.PI / 180));
            Log.d(TAG, "maxRadius: " + maxRadius);
            float radius = 1 + relSize * (maxRadius - 5);
            Log.d(TAG, "radius: " + radius);

            // dist represents the distance to the CENTER of the circle item
            float minDist = cRadius + 40;
            Log.d(TAG, "cRadius: " + cRadius);
            Log.d(TAG, "minDist: " + minDist);
            float vertMaxDist = viewHeight/4 + cRadius/2;
            float horizMaxDist = viewWidth/4 + cRadius/2;
            float maxDist = (float) (horizMaxDist + (vertMaxDist - horizMaxDist) * Math.abs(Math.cos(angle * Math.PI / 180)));
            Log.d(TAG, "maxDist: " + maxDist);
            float dist = (float) (minDist + relSize * (maxDist - minDist) * Math.abs(Math.cos(angle * Math.PI / 180)));
            Log.d(TAG, "dist: " + dist);

            // Set layout parameters for the circle
            RelativeLayout.LayoutParams cParams = new RelativeLayout.LayoutParams((int) topx(radius) * 2, (int) topx(radius) * 2);
            circleIv.setLayoutParams(cParams);

            this.addView(circleIv);

            // Set the circle's coordinates (in px)
            circleIv.setX(topx(viewWidth / 2 - (float) (Math.sin(angles.get(i) * Math.PI / 180.0) * dist) - radius));
            Log.d(TAG, viewWidth / 2 + " - " + (Math.sin(angles.get(i) * Math.PI / 180.0) * dist) + " - " + radius);
            Log.d(TAG, "x: " + (viewWidth / 2 - (float) (Math.sin(angles.get(i) * Math.PI / 180.0) * dist) - radius));
            Log.d(TAG, "x px: " + topx(viewWidth / 2 - (float) (Math.sin(angles.get(i) * Math.PI / 180.0) * dist) - radius));
            circleIv.setY(topx(viewHeight / 2 - (float) (Math.cos(angles.get(i) * Math.PI / 180.0) * dist) - radius));
            Log.d(TAG, viewHeight / 2 + " - " + (Math.cos(angles.get(i) * Math.PI / 180.0) * dist) + " - " + radius);
            Log.d(TAG, "y: " + (viewHeight / 2 - (float) (Math.cos(angles.get(i) * Math.PI / 180.0) * dist) - radius));
            Log.d(TAG, "y px: " + topx(viewHeight / 2 - (float) (Math.cos(angles.get(i) * Math.PI / 180.0) * dist) - radius));

            points.add(viewWidth / 2);
            points.add(viewHeight / 2);
            points.add(viewWidth / 2 - (float) (Math.sin(angles.get(i) * Math.PI / 180.0) * dist));
            points.add(viewHeight / 2 - (float) (Math.cos(angles.get(i) * Math.PI / 180.0) * dist));

            // Set the image as the background if it has one
            if (image == null) {
                circleIv.setBackground(background);
            } else {
                Log.d(TAG, "item has an image");
                circleIv.setImageBitmap(image);
            }

            // Set layout parameters for the textview
            RelativeLayout.LayoutParams tParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            textView.setText(text);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, textScale);
            textView.setLayoutParams(tParams);

            this.addView(textView);

            // Set the textview's coordinates (in px)
            textView.addOnLayoutChangeListener(new OnLayoutChangeListener() {
                @Override
                public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                    Log.d(TAG, "textview dimens: " + v.getHeight() + ", " + v.getWidth());
                    v.setX(v.getX() - v.getWidth()/2);
                    v.setY(v.getY() - v.getHeight()/2);
                }
            });

            textView.setX(topx(viewWidth / 2 - (float) (Math.sin(angles.get(i) * Math.PI / 180.0) * dist)) - textView.getWidth());
            textView.setY(topx(viewHeight / 2 - (float) (Math.cos(angles.get(i) * Math.PI / 180.0) * dist)) - textView.getHeight());

            // Draw connecting line
            //drawLines();
            //canvas.drawLine(viewWidth/2, viewHeight/2,
            //        topx(viewWidth / 2 - (float) (Math.sin(angles.get(i) * Math.PI / 180.0) * dist) - radius),
            //        topx(viewHeight / 2 - (float) (Math.cos(angles.get(i) * Math.PI / 180.0) * dist) - radius), paint);
        }
    }

    @Override
    protected void onFinishInflate() {
        Log.d(TAG, "On finish inflate");
        super.onFinishInflate();

        //showCircles();

        //canvas.drawBitmap(bitmap, 0, 0, null);
        //this.setBackground(new BitmapDrawable(getResources(), bitmap));
    }

    private void getAngles() {
        angles = new ArrayList<>();

        if (numCircles == 0) {
            Log.d(TAG, "no circles added");
        } else if (numCircles == 1) {
            Log.d(TAG, "one circle");
            angles.add(0);
        } else if (numCircles == 2) {
            Log.d(TAG, "two circles");
            angles.add(0);
            angles.add(180);
        } else {
            Log.d(TAG, "more than two circles");
            for (int i = 0; i < numCircles; i++) {
                angles.add(180 / numCircles + (i * 360 / numCircles));
            }
        }
    }

    @Override
    protected void onSizeChanged(int xNew, int yNew, int xOld, int yOld){
        Log.d(TAG, "size changed");
        super.onSizeChanged(xNew, yNew, xOld, yOld);

        //Use density pixels for calculations
        viewWidth = todp(xNew);
        viewHeight = todp(yNew);

        Log.d(TAG, "dimensions - " + viewWidth + " by " + viewHeight);

        numCircles = circles.size();

        if (firstTime) {
            showCircles();

            bitmap = Bitmap.createBitmap((int) viewWidth, (int) viewHeight, Bitmap.Config.ARGB_8888);
            canvas = new Canvas(bitmap);

            canvas.drawBitmap(bitmap, 0, 0, null);

            if (!points.isEmpty()) {
                for (int i=0; i < points.size()/4; i++) {
                    Log.d(TAG, "drawing a line");
                    canvas.drawLine(points.get(i*4), points.get(i*4+1), points.get(i*4+2), points.get(i*4+3), paint);
                }
            }

            this.setBackground(new BitmapDrawable(getResources(), bitmap));
            firstTime = false;
        }
    }

    private float todp(float px) {
        //Resources r = getResources();
        //return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, p, r.getDisplayMetrics());
        return Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    private float topx(float dp) {
        //DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        //return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.d(TAG, "drawing lines");
        super.onDraw(canvas);

        //showCircles();

        //if (!points.isEmpty()) {
        //    for (int i=0; i < points.size()/4; i++) {
        //        canvas.drawLine(points.get(i*4), points.get(i*4+1), points.get(i*4+2), points.get(i*4+3), paint);
        //    }
        //}
    }
}
