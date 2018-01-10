package utry.com.myapp;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class SecondActivity extends AppCompatActivity {

    private ImageView imageView;

    private Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        imageView = findViewById(R.id.imageFrame);

        findViewById(R.id.button_2).setOnClickListener(v -> {

            File file = new File(getExternalCacheDir(),"output_image.jpg");

            if (file.exists()) {
                file.delete();
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            uri = Build.VERSION.SDK_INT >= 24 ?
                    FileProvider.getUriForFile(this, "com.demo.zzz.fileProvider", file):
                    Uri.fromFile(file);

            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            startActivityForResult(intent, 1);

        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            imageView.setImageBitmap(BitmapFactory.decodeStream(getContentResolver().openInputStream(uri)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }
}
