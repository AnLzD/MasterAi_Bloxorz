package com.docik.bloxorz;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class Main extends Activity implements OnClickListener{
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        findViewById(R.id.Change_player).setOnClickListener(this);
        findViewById(R.id.Play).setOnClickListener(this);
    }

	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()){
			case R.id.Play:
			{
				Intent intent = new Intent();
				intent.setClass(this, Play.class);

				startActivity(intent);
				break;
			}
			case R.id.ChooseLevel:{
				
			}
			case R.id.Change_player:
			{
				Intent intent = new Intent();
				intent.setClass(this, ChangePlayer.class);

				startActivity(intent);
				break;
			}
			case R.id.Sound:{
				
			}
			case R.id.HighScores:{
				
			}
			case R.id.Credits:{
				
			}
			case R.id.Exit:
			{
				finish();
				break;
			}
		}
	}

}