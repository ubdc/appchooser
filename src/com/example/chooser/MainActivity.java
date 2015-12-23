package com.example.chooser;

import java.net.URISyntaxException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.chooser.ChooserDialog.OnNoAppMatchListener;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}
	
	public void go(View v) throws URISyntaxException {
		new ChooserDialog.Builder(this)
		.setTitle("请选择")
		.addIntent(Intent.getIntent("intent://map/direction?origin=latlng:34.264642646862,108.95108518068|name:我家&destination=大雁塔&mode=driving&region=西安&src=yourCompanyName|yourAppName#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end"))
//		.addIntent(Intent.getIntent("intent://map/marker?location=40.047669,116.313082&title=我的位置&content=百度奎科大厦&src=yourCompanyName|yourAppName#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end"))
		.addIntent(new Intent().setAction("android.intent.action.VIEW").addCategory("android.intent.category.DEFAULT").setData(Uri.parse("androidamap://viewMap?sourceApplication=appname&poiname=abc&lat=36.2&lon=116.1&dev=0")).setPackage("com.autonavi.minimap"))
		.setOnNoAppMatchListener(new OnNoAppMatchListener() {
			
			@Override
			public void onNoAppMatch() {
				Toast.makeText(MainActivity.this, "实在没找到", Toast.LENGTH_LONG).show();
			}
		})
		.create()
		.show();
	}
	
	public void baidu(View v) throws URISyntaxException {
//		Intent intent = Intent.getIntent("intent://map/direction?origin=latlng:34.264642646862,108.95108518068|name:我家&destination=大雁塔&mode=driving&region=西安&src=yourCompanyName|yourAppName#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end");
		Intent intent = Intent.getIntent("intent://map/direction?origin=latlng:39.991255,116.514222|name:我的位置&destination=latlng:39.904899,116.499342|name:北京朝阳区百子湾12号大成国际公寓3号楼&coord_type=bd09&mode=driving&referer=com.menu|menutuji;scheme=bdapp;package=com.baidu.BaiduMap;end");
		if (intent.resolveActivity(getPackageManager()) != null) {
			startActivity(intent);
		}
	}
	
	public void gaode(View v) {
		Intent intent = new Intent().setAction("android.intent.action.VIEW").addCategory("android.intent.category.DEFAULT").setData(Uri.parse("androidamap://viewMap?sourceApplication=appname&poiname=abc&lat=36.2&lon=116.1&dev=0")).setPackage("com.autonavi.minimap");
		if (intent.resolveActivity(getPackageManager()) != null) {
			startActivity(intent);
		}
	}
}
