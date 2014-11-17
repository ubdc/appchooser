package com.example.chooser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

public class ChooserDialog extends AlertDialog {
	private Activity activity;
	private List<Intent> intents;
	private String title;
	private GridView appGridView;
	private Map<ResolveInfo, Intent> map = new HashMap<ResolveInfo, Intent>();
	private List<ResolveInfo> list = new ArrayList<ResolveInfo>();
	private OnNoAppMatchListener noAppMatchListener;
	
	public ChooserDialog(Context context) {
		super(context);
	}
	
	private ChooserDialog(Builder builder) {
		super(builder.activity, builder.themeId);
		activity = builder.activity;
		intents = builder.intents;
		title = builder.title;
		noAppMatchListener = builder.noAppMatchListener;
		for (int i = intents.size() - 1; i >= 0; i--) {
			Intent intent = intents.get(i);
			List<ResolveInfo> apps = activity.getPackageManager().queryIntentActivities(intent, 0);
			if (apps != null && apps.size() > 0) {
				list.addAll(0, apps);
				
				for (ResolveInfo app : apps) {
					map.put(app, intent);
				}
			}
		}
	}
	
	@Override
	public void show() {
		if (list.size() == 0) {
			if (noAppMatchListener != null)
				noAppMatchListener.onNoAppMatch();
			return;
		}
		super.show();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chooser_layout);
		TextView titleView = (TextView) findViewById(R.id.title);
		titleView.setText(title);
		appGridView = (GridView) findViewById(R.id.appList);
		appGridView.setAdapter(new AppAdapter());
		appGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				ResolveInfo app = list.get(position);
				Intent intent = map.get(app);
				intent.setPackage(app.activityInfo.packageName);
				try {
					activity.startActivity(intent);
				} catch (Exception e) {
					e.printStackTrace();
				}
				dismiss();
			}
		});
	}

	public static class Builder {
		private Activity activity;
		private List<Intent> intents;
		private String title;
		private OnNoAppMatchListener noAppMatchListener;
		private int themeId;
		public Builder(Activity a) {
			this.activity = a;
			noAppMatchListener = new OnNoAppMatchListener() {
				
				@Override
				public void onNoAppMatch() {
					Toast.makeText(activity, "没有找到相关应用", Toast.LENGTH_LONG).show();
				}
			};
		}
		
		public Builder setTitle(String title) {
			this.title = title;
			return this;
		}
		
		public Builder setIntents(List<Intent> intents) {
			this.intents = intents;
			return this;
		}
		
		public Builder addIntent(Intent intent) {
			if (intent == null) {
				return this;
			}
			if (intents == null) {
				intents = new ArrayList<Intent>();
			}
			intents.add(intent);
			return this;
		}
		
		public Builder setOnNoAppMatchListener(OnNoAppMatchListener noAppMatchListener) {
			this.noAppMatchListener = noAppMatchListener;
			return this;
		}
		
		public Builder setTheme(int themeId) {
			this.themeId = themeId;
			return this;
		}
		
		public Dialog create() {
			return new ChooserDialog(this);
		}
	}
	
	private class AppAdapter extends BaseAdapter {
		private LayoutInflater inflater;
		
		public AppAdapter() {
			inflater = LayoutInflater.from(activity);
		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			TextView app = null;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.chooser_app_item, parent, false);
				app = (TextView) convertView.findViewById(R.id.app);
				convertView.setTag(app);
			} else {
				app = (TextView) convertView.getTag();
			}
			ResolveInfo appInfo = list.get(position);
			app.setText(appInfo.loadLabel(activity.getPackageManager()));
			app.setCompoundDrawablesWithIntrinsicBounds(null, appInfo.loadIcon(activity.getPackageManager()), null, null);
			return convertView;
		}
	}
	
	public interface OnNoAppMatchListener {
		void onNoAppMatch();
	}
}
