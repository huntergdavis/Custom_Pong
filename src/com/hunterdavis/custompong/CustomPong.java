package com.hunterdavis.custompong;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.google.ads.AdRequest;
import com.google.ads.AdView;

public class CustomPong extends Activity {

	Panel mypanel = null;
	int currentScore = 0;
	String lastHighScoreName = "";
	int SELECT_PICTURE = 22;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		mypanel = (Panel) findViewById(R.id.SurfaceView01);
		

		// set an adapter for our difficult level
		// no onclick or onselect necessary
		Spinner spinner = (Spinner) findViewById(R.id.difficultyspin);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, R.array.difficulty, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setOnItemSelectedListener(new MyUnitsOnItemSelectedListener());
		spinner.setAdapter(adapter);
		spinner.setSelection(0);

		// Create an anonymous implementation of OnClickListener
		OnClickListener resetButtonListner = new OnClickListener() {
			public void onClick(View v) {
				// do something when the button is clicked
				// Boolean didWeSave = saveImage(v.getContext());
				mypanel.reset();
			}
		};

		// Create an anonymous implementation of OnClickListener
		OnClickListener ballButtonListner = new OnClickListener() {
			public void onClick(View v) {
				// do something when the button is clicked
				// Boolean didWeSave = saveImage(v.getContext());

				// in onCreate or any event where your want the user to
				// select a file
				Intent intent = new Intent();
				intent.setType("image/*");
				intent.setAction(Intent.ACTION_GET_CONTENT);
				startActivityForResult(
						Intent.createChooser(intent, "Select Source Photo"),
						SELECT_PICTURE);

			}
		};

		Button resetButton = (Button) findViewById(R.id.resetButton);
		resetButton.setOnClickListener(resetButtonListner);

		Button loadImageButton = (Button) findViewById(R.id.loadButton);
		loadImageButton.setOnClickListener(ballButtonListner);

		// Toast.makeText(getBaseContext(),
		// "Draw a Line Around Everything Without Touching",
		// Toast.LENGTH_LONG).show();

		// Look up the AdView as a resource and load a request.
		AdView adView = (AdView) this.findViewById(R.id.adView);
		adView.loadAd(new AdRequest());

	} // end of oncreate

	protected void onPause() {
		super.onPause();
		mypanel.terminateThread();
		System.gc();
	}

	protected void onResume() {
		super.onResume();
		if (mypanel.surfaceCreated == true) {
			mypanel.createThread(mypanel.getHolder());
		}
	}

	// set up the listener class for spinner
	class MyUnitsOnItemSelectedListener implements OnItemSelectedListener {
		public void onItemSelected(AdapterView<?> parent, View view, int pos,
				long id) {
			mypanel.setDifficulty(pos);
		}

		public void onNothingSelected(AdapterView<?> parent) {
			// Do nothing.
		}
	}

	// this is called when the screen rotates.
	// (onCreate is no longer called when screen rotates due to manifest, see:
	// android:configChanges)
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// setContentView(R.layout.main);

		// InitializeUI();
	}


	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			if (requestCode == SELECT_PICTURE) {
				Uri selectedImageUri = data.getData();
				mypanel.setUri(selectedImageUri);
			}
		}
	}

}