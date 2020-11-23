package mobile.project.animalwallpaper;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.WallpaperManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;

import android.Manifest;

import dmax.dialog.SpotsDialog;

public class ImagePreview extends AppCompatActivity {

    Button image, download, share, set;
    ImageView imageView;
    LinearLayout linearLayout;
    String bytes;

    private WeakReference<AlertDialog> alertDialogWeakReference;
    private WeakReference<ContentResolver> contentResolverWeakReference;

    //permission
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL = 1;
    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL = 1;
    private static final int MY_PERMISSIONS_REQUEST_SET_WALLPAPER = 1;
    private Object SpotsDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_preview);

        imageView = (ImageView) findViewById(R.id.imagePreview);
        download = (Button) findViewById(R.id.imageDownload);
        share = (Button) findViewById(R.id.imageFav);
        set = (Button) findViewById(R.id.imageSetWall);
        linearLayout = (LinearLayout) findViewById(R.id.linierLayout);

        //permission
        getPermissionsReadExternal();

        //FULLSCREEN LAYOUT
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        bytes = getIntent().getStringExtra("image");

        Picasso.with(this).load(bytes)
                .fit()
                .into(imageView);

        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //download gambar
                dmax.dialog.SpotsDialog dialog = new SpotsDialog(ImagePreview.this);
                dialog.show();
                dialog.setMessage("Downloading...");

                Picasso.with(getBaseContext())
                        .load(bytes)
                        .into(new SaveImageHelper(getBaseContext(),
                                dialog,
                                getApplicationContext().getContentResolver(),
                                bytes + ".jpg", ""));

                new CountDownTimer(2500, 1000) {

                    public void onTick(long millisUntilFinished) {

                    }

                    public void onFinish() {
                        finish();
                    }

                }.start();
            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //share link
            }
        });

        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dmax.dialog.SpotsDialog dialog = new SpotsDialog(ImagePreview.this);
                dialog.show();
                dialog.setMessage("Please Wait...");
                //set wallpaper
                if (ContextCompat.checkSelfPermission(ImagePreview.this, Manifest.permission.SET_WALLPAPER) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(ImagePreview.this,
                            new String[]{Manifest.permission.SET_WALLPAPER},
                            MY_PERMISSIONS_REQUEST_SET_WALLPAPER);
                    dialog.dismiss();
                }

                Picasso.with(ImagePreview.this).load(bytes).into(new Target(){

                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        WallpaperManager wallpaperManager = WallpaperManager.getInstance(ImagePreview.this);
                        try {
                            wallpaperManager.setBitmap(bitmap);
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                        Log.d("TAG", "onBitmapLoaded: ");
                        Toast.makeText(ImagePreview.this, "Wallpaper Changed", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        new CountDownTimer(2300, 1000) {

                            public void onTick(long millisUntilFinished) {

                            }

                            public void onFinish() {
                                finish();
                            }

                        }.start();
                    }

                    @Override
                    public void onBitmapFailed(final Drawable errorDrawable) {
                        Log.d("TAG", "FAILED");
                    }

                    @Override
                    public void onPrepareLoad(final Drawable placeHolderDrawable) {
                        Log.d("TAG", "Prepare Load");
                    }
                });
            }
        });
    }

    //GET PERMISSION
    public void getPermissionsReadExternal() {
        /* Check and Request permission */
        if (ContextCompat.checkSelfPermission(ImagePreview.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ImagePreview.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL);
        }
        if (ContextCompat.checkSelfPermission(ImagePreview.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ImagePreview.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {

            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission..
                    Toast.makeText(ImagePreview.this, "Permission denied", Toast.LENGTH_SHORT).show();

                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

}