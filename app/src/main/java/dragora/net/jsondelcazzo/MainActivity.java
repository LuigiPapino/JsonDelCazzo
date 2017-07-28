package dragora.net.jsondelcazzo;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
    fab.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        new CopyTask().execute();
      }
    });
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    //noinspection SimplifiableIfStatement
    if (id == R.id.action_settings) {
      return true;
    }

    return super.onOptionsItemSelected(item);
  }

  private String readTextFile(String path) {
    BufferedReader reader = null;
    StringBuilder builder = new StringBuilder();
    try {
      reader = new BufferedReader(new FileReader(path));
      String line = "";

      while ((line = reader.readLine()) != null) {
        builder.append(line);
      }
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if (reader != null) {
        try {
          reader.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
    return builder.toString();
  }

  private class CopyTask extends AsyncTask<Void, Void, Void> {

    @Override
    protected Void doInBackground(Void... voids) {

      try {
        InputStream in = getResources().openRawResource(R.raw.jsondelcazzo);
        File cacheFile = new File(getApplicationContext().getCacheDir(), "/rinkeby_wallet");
        FileOutputStream out = new FileOutputStream(cacheFile);
        byte[] buff = new byte[1024];
        int read = 0;

        while ((read = in.read(buff)) > 0) {
          out.write(buff, 0, read);
        }
        in.close();
        out.close();

        //Is it working? Try to read the copied file
        Log.d("MYLOG", readTextFile(cacheFile.getAbsolutePath()));
      } catch (Exception e) {
        Log.e("MYLOG", e.getMessage(), e);
      }
      return null;
    }
  }
}