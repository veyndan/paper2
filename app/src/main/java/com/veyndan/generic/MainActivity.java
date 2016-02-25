package com.veyndan.generic;

import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.widget.ImageView;

import uk.co.senab.photoview.PhotoViewAttacher;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = LogUtils.makeLogTag(MainActivity.class);

    private PhotoViewAttacher attacher;

    private ImageView profile;
    private ImageView image;
    private ImageButton heart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        heart = (ImageButton) findViewById(R.id.heart);
        profile = (ImageView) findViewById(R.id.profile);
        image = (ImageView) findViewById(R.id.image);

        attacher = new PhotoViewAttacher(image);

        heart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                heart.setSelected(!heart.isSelected());
            }
        });

        final DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        ViewTreeObserver vto = image.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                image.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                final int width = image.getMeasuredWidth();
                final int height = image.getMeasuredHeight();
                attacher.setOnScaleChangeListener(new PhotoViewAttacher.OnScaleChangeListener() {
                    @Override
                    public void onScaleChange(float scaleFactor, float focusX, float focusY) {
//                        if (width * scaleFactor <= metrics.widthPixels) {
//                            ViewGroup.LayoutParams params = image.getLayoutParams();
//                            params.width = (int) (width * scaleFactor);
//                            params.height = (int) (height * scaleFactor);
//                            image.setLayoutParams(params);
//                        }
                    }
                });
            }
        });


        // Make profile picture black and white
        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(0);
        profile.setColorFilter(new ColorMatrixColorFilter(matrix));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        attacher.cleanup();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
