package com.freshlancers.exportasimage;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.snatik.storage.Storage;
import com.vipul.hp_hp.library.Layout_to_Image;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.linear_layout)
    LinearLayout linearLayout;
    @BindView(R.id.savedImage)
    ImageView savedImageView;
    private Storage storage;
    private String newDir;
    private int WRITE_EXTERNAL_STORAGE = 111;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        //init
        storage = new Storage(getApplicationContext());
        // get external storage
        String path = storage.getExternalStorageDirectory();

        // new dir
        newDir = path + File.separator + "Convert to Png";
        storage.createDirectory(newDir);

        boolean hasPermission = (ContextCompat.checkSelfPermission(getBaseContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);

        if (!hasPermission) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.ACCESS_NETWORK_STATE,
                            Manifest.permission.RECORD_AUDIO, Manifest.permission.MODIFY_AUDIO_SETTINGS,
                            Manifest.permission.INTERNET
                    }, WRITE_EXTERNAL_STORAGE);
        }
    }

    @OnClick(R.id.convertToPng)
    public void onViewClicked() {
        Layout_to_Image layout_to_image = new Layout_to_Image(MainActivity.this, linearLayout);
        Bitmap bitmap = layout_to_image.convert_layout();
        savedImageView.setImageBitmap(bitmap);
        boolean success = storage.createFile(newDir + File.separator + "image.jpg", bitmap);
        if (success)
            Toast.makeText(this, "Image saved to " + newDir + File.separator + "image.jpg", Toast.LENGTH_LONG).show();
        else
            Toast.makeText(this, "Image couldn't be saved to " + newDir + File.separator + "image.jpg", Toast.LENGTH_LONG).show();
    }
}
