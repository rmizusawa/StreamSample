package jp.co.mz.stream;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends ListActivity {

	private List<FileBean> filelists = new ArrayList<FileBean>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        (new Thread(runnable)).start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

	private Runnable runnable = new Runnable() {
		@Override
		public void run() {
			// ここに実際に行う処理を書く すぐ終わってしまうような処理だとダイアログは表示されない
			try {
				String path = Environment.getExternalStorageDirectory().getAbsolutePath();
				File fi = new File(path+"/DCIM/Camera/");
				File[] filists = fi.listFiles(new FilenameFilter() {

					@Override
					public boolean accept(File dir, String filename) {
						boolean ret = filename.endsWith(".mp4");
						return ret;
					}
				});

				filelists = new ArrayList<FileBean>();
				for (File temp:filists) {
					FileBean bean = new FileBean();
					bean.setUri(temp.getAbsolutePath());
					filelists.add(bean);
				}

			} catch (Exception e) {
				Log.e("Runnable", "Exception");
			}
			handler.sendEmptyMessage(0);

		}
	};

	/**
	 * 実際に行う処理でUIを書き換える必要がある場合はこのHandler内で行う
	 */
	private final Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {

			ListAdapter adapter = new ListAdapter(getApplicationContext(),	filelists);

			ListView listView = (ListView) findViewById(android.R.id.list);
			listView.setAdapter(adapter);
			listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
						ListView clickListView = (ListView) parent;
						// クリックされたアイテムを取得します
						FileBean item = (FileBean) clickListView.getItemAtPosition(position);

						Intent intent = new Intent(getApplicationContext(), PlayerActivity.class);
						intent.putExtra("detail", item);
						startActivity(intent);

					}

				});
		}
	};

	private class ListAdapter extends ArrayAdapter<FileBean> {

		private LayoutInflater mInflater;
		private TextView mContent;

		public ListAdapter(Context context, List<FileBean> objects) {
			super(context, 0, objects);
			mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		public View getView(final int position, View convertView,
				ViewGroup parent) {

			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.filelist_row, null);
			}

			final FileBean item = this.getItem(position);
			if (item != null) {

				mContent = (TextView) convertView.findViewById(R.id.filename);
				String s = item.getUri();
				mContent.setText(s);
			}
			return convertView;

		}
	}
}
