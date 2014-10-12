package com.example.babylog;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {
	Button btnLogin, btnRegist;
	EditText etUsername, etPassword;
	Thread Login;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_main);

		// 將介面和程式掛勾

		etUsername = (EditText) findViewById(R.id.etUsername);
		etPassword = (EditText) findViewById(R.id.etPassword);
		btnLogin = (Button) findViewById(R.id.btnLogin);
		// 建立按鈕的OnClickListener
		btnLogin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// 執行子程序
				Login = new Login();
				Login.start();
			}
		});
	};

	// 執行連接網絡所需的子程序

	class Login extends Thread {

		@Override
		public void run() {

			// 建立HttpClient用以跟伺服器溝通

			HttpClient client = new DefaultHttpClient();

			try {

				HttpPost post = new HttpPost(
						"http://120.119.77.53/~pauwhay/BabyLog/login.php");

				// 建立POST的變數
				List<NameValuePair> vars = new ArrayList<NameValuePair>();
				vars.add(new BasicNameValuePair("username", etUsername
						.getText().toString()));
				vars.add(new BasicNameValuePair("password", etPassword
						.getText().toString()));

				// 發出POST要求

				post.setEntity(new UrlEncodedFormEntity(vars, HTTP.UTF_8));

				// 建立ResponseHandler,以接收伺服器回傳的訊息
				ResponseHandler<String> h = new BasicResponseHandler();

				// 將回傳的訊息轉為String
				String response = new String(
						client.execute(post, h).getBytes(), HTTP.UTF_8);
				Looper.prepare();

				// 若回傳的訊息等於"Login Succeeded"，跳轉到另一個頁面

				if (response.equals("login success")) {

					Intent i = new Intent(MainActivity.this, Test.class);
					startActivity(i);
				}

				// 否則只顯示回傳訊息

				else {

					Toast.makeText(MainActivity.this, response,
							Toast.LENGTH_LONG).show();
				}

				Looper.loop();
			} catch (Exception ex) {

				// 若伺服器無法與PHP檔連接時的動作

			}

		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}