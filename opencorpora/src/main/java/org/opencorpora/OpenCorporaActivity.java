package org.opencorpora;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.opencorpora.authenticator.AuthHelper;
import org.opencorpora.authenticator.Authenticator;
import org.opencorpora.authenticator.IAuthListener;


public class OpenCorporaActivity extends Activity implements IAuthListener {
    public Authenticator mAuthenticator;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuthenticator = new Authenticator(this);
        setContentView(R.layout.opencorpora_layout);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onSuccess() {
        TextView textView = (TextView) findViewById(R.id.text_view);
        SharedPreferences prefs = getSharedPreferences("AuthPrefs", MODE_PRIVATE);
        String token = prefs.getString("token", "not found :(");
        Log.d("prefs result", token);

        textView.setText(getString(R.string.TokenReceivedPrefix) + token);
        AuthHelper.getInstance().unSubscribe(this);
    }

    @Override
    public void onFail() {
        TextView textView = (TextView) findViewById(R.id.text_view);
        textView.setText(R.string.RequestFailed);
        AuthHelper.getInstance().unSubscribe(this);
    }

    public void onClick(View view){
        AuthHelper.getInstance().authorize("login", "password", this);
        AuthHelper.getInstance().subscribe(this);
    }

    public void tryAuth(View view){
        Account[] accounts = AccountManager.get(this).getAccountsByType("org.opencorpora");
        TextView textView = (TextView) findViewById(R.id.text_view);
        textView.setText("Found: " + accounts.length + " opencorpora accounts.");
    }
}
