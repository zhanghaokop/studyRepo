package utry.com.myapp;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

public class FirstActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_layout);

        Button button1 = findViewById(R.id.button_1);
        button1.setOnClickListener(v -> {
            Intent intent = new Intent(FirstActivity.this, SecondActivity.class);
            intent.putExtra("hello","world");
            startActivityForResult(intent,1);
        });

        Button button3 = findViewById(R.id.button_3);
        button3.setOnClickListener((v) -> {
            Intent intent = new Intent(this, WebFrameActivity.class);
            startActivity(intent);
        });


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_item:
                Toast.makeText(this, "menu[ADD]", Toast.LENGTH_SHORT).show();
            case R.id.delete_item:
                Toast.makeText(this, "menu[DELETE]", Toast.LENGTH_SHORT).show();
            default:
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

}
