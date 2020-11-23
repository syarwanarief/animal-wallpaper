package mobile.project.animalwallpaper.ui.home;

import android.app.ActionBar;
import android.app.StatusBarManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import mobile.project.animalwallpaper.ImagePreview;
import mobile.project.animalwallpaper.firebase.AdapterList;
import mobile.project.animalwallpaper.firebase.FirebaseModel;
import mobile.project.animalwallpaper.R;

import static android.app.Activity.RESULT_OK;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    ImageView tampil;
    Uri filePath;

    private final int PICK_IMAGE_REQUEST = 71;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    List<FirebaseModel> list;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        final RecyclerView recyclerView = (RecyclerView) root.findViewById(R.id.tampil);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Animal Wallpaper");
        reference.keepSynced(true);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list = new ArrayList<FirebaseModel>();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    FirebaseModel model = new FirebaseModel();
                    FirebaseModel value = dataSnapshot1.getValue(FirebaseModel.class);
                    String image = value.getImage();
                    model.setImage(image);
                    list.add(model);
                    AdapterList adapterList = new AdapterList(list, getActivity());
                    RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), 3);
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setAdapter(adapterList);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return root;
    }

}