package tsd.boss_launcher.home_screen;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.TextViewCompat;
import androidx.fragment.app.Fragment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;

import tsd.boss_launcher.R;
import tsd.boss_launcher.app_list_screen.AppListActivity;
import tsd.boss_launcher.utils.Constant;

/**
 * home screen
 *
 * @author gaopeng
 */
public class HomeFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_home, container, false);
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		handleTimeTextView();
		handleDateTextView();
		handleMainUIButtonText();
		handleMainUIButton();
		//handleTempTextView();
	}

	private void handleMainUIButton() {
		View.OnClickListener onClickListener = new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent();
				switch (view.getId()) {
				case R.id.button1:
					intent.setComponent(AppInfo.whiteBoardComponent);
					break;
				case R.id.button2:
					intent.setComponent(AppInfo.signalComponent);
					break;
				case R.id.button3:
					intent.setComponent(AppInfo.screenCastComponent);
					break;
				case R.id.button4:
					intent.setClass(getContext(), AppListActivity.class);
					Bundle bundle = getArguments();
					ArrayList<ResolveInfo> appInfos = bundle
							.getParcelableArrayList(Constant.ACTION_BROADCAST_APP_LIST_CHANGE);
					intent.putParcelableArrayListExtra(Constant.ACTION_BROADCAST_APP_LIST_CHANGE, appInfos);
					break;
				}
				try {
					startActivity(intent);
				} catch (ActivityNotFoundException e) {
				}
			}
		};
		getView().findViewById(R.id.button1).setOnClickListener(onClickListener);
		getView().findViewById(R.id.button2).setOnClickListener(onClickListener);
		getView().findViewById(R.id.button3).setOnClickListener(onClickListener);
		getView().findViewById(R.id.button4).setOnClickListener(onClickListener);
	}

	private void handleMainUIButtonText() {
		AssetManager mgr = getActivity().getAssets();
		Typeface tf = Typeface.createFromAsset(mgr, "fonts/Nunito-Bold.ttf");
		setTypeface(R.id.textView1, tf);
		setTypeface(R.id.textView2, tf);
		setTypeface(R.id.textView3, tf);
		setTypeface(R.id.textView4, tf);
	}

	private void setTypeface(@IdRes int id, Typeface typeface) {
		TextView textView = getView().findViewById(id);
		textView.setTypeface(typeface);
	}

	private void handleDateTextView() {
		AssetManager mgr = getActivity().getAssets();
		Typeface tf = Typeface.createFromAsset(mgr, "fonts/Nunito-Light.ttf");
		TextView textView = getView().findViewById(R.id.dataTextView);
		textView.setTypeface(tf);
		TextViewCompat.setAutoSizeTextTypeWithDefaults(textView, TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM);
	}

	private void handleTimeTextView() {
		AssetManager mgr = getActivity().getAssets();
		Typeface tf = Typeface.createFromAsset(mgr, "fonts/Nunito-Bold.ttf");
		TextView textView = getView().findViewById(R.id.timeTextView);
		TextViewCompat.setAutoSizeTextTypeWithDefaults(textView, TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM);

		textView.setTypeface(tf);
		textView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent();
				intent.setComponent(AppInfo.systemClockComponent);
				try {
					startActivity(intent);
				} catch (ActivityNotFoundException e) {
					intent.setComponent(AppInfo.systemClockComponent2);
					try {
						startActivity(intent);
					} catch (ActivityNotFoundException ee) {
					}
				}
			}
		});
	}

	private void handleTempTextView() {
		AssetManager mgr = getActivity().getAssets();
		Typeface tf = Typeface.createFromAsset(mgr, "fonts/PINGFANG EXTRALIGHT.TTF");
		TextView textView = getView().findViewById(R.id.textView5);
		textView.setTypeface(tf);
		textView.setText(getFileContent());
		Log.d("testtttt",getFileContent());
		TextViewCompat.setAutoSizeTextTypeWithDefaults(textView, TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM);
	}



	protected String getFileContent() {
		String wendu = "";
		try {
			File file = new File("proc/mstar_dvfs/temperature");//文件路径
			FileReader fileReader = new FileReader(file);
			LineNumberReader reader = new LineNumberReader(fileReader);
			int number = 1;//设置指定行数
			String txt = "";
			int lines = 0;
			while (txt != null) {
				lines++;
				txt = reader.readLine();
				if (lines == number) {

					wendu = txt.substring(11, 13) + "℃";

				}
			}
			reader.close();
			fileReader.close();
		} catch (FileNotFoundException e) {

		} catch (IOException e) {

		}
		return wendu;
	}
}
