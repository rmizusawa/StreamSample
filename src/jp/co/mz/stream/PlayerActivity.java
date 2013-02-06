package jp.co.mz.stream;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.MediaController;
import android.widget.VideoView;

public class PlayerActivity extends Activity {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        FileBean bean = null;

		if (getIntent().getExtras() != null) {
			bean = (FileBean)getIntent().getExtras().getSerializable("detail");
		}
        VideoView viview = (VideoView)findViewById(R.id.viview);
        viview.requestFocus();
        viview.setMediaController(new MediaController(this));
        viview.setVideoPath(bean.getUri());
        viview.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }


}
