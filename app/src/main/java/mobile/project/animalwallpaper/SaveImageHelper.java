package mobile.project.animalwallpaper;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.provider.MediaStore;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.lang.ref.WeakReference;

public class SaveImageHelper implements Target {

    private WeakReference<AlertDialog> alertDialogWeakReference;
    private WeakReference<ContentResolver> contentResolverWeakReference;
    private String name;
    private String desc;
    Context context;

    public SaveImageHelper (Context baseContext, AlertDialog alertDialog, ContentResolver contentResolver, String name, String desc){
        this.alertDialogWeakReference = new WeakReference<AlertDialog>(alertDialog);
        this.contentResolverWeakReference = new WeakReference<ContentResolver>(contentResolver);
        this.name = name;
        this.desc = desc;
        this.context = baseContext;
    }

    @Override
    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
        ContentResolver r = contentResolverWeakReference.get();
        AlertDialog dialog = alertDialogWeakReference.get();

        if (r != null){
            MediaStore.Images.Media.insertImage(r,bitmap,name,desc);
            dialog.dismiss();
            Toast.makeText(context, "Wallpaper Downloaded Succes", Toast.LENGTH_SHORT).show();

        }else{
            dialog.dismiss();
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBitmapFailed(Drawable errorDrawable) {
        AlertDialog dialog = alertDialogWeakReference.get();
        dialog.dismiss();
        Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPrepareLoad(Drawable placeHolderDrawable) {

    }
}
