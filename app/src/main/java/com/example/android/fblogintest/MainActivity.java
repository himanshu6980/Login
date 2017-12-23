package com.example.android.fblogintest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,GoogleApiClient.OnConnectionFailedListener{

    TextView txtstatus;
LoginButton loginButton;
CallbackManager callbackManager;

public  LinearLayout profile;
Button Signout;
SignInButton Signin;
TextView Name,Email;
ImageView profilepic;
GoogleApiClient googleApiClient;
static final int REQ_CODE=9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_main);
        initializecontrols();
        Loginwithfb();
        profile=(LinearLayout)findViewById(R.id.profile_section);
        Signout=(Button)findViewById(R.id.sign_out);
        Signin=(SignInButton)findViewById(R.id.sign_in_button);
        Name=(TextView)findViewById(R.id.name);
        Email=(TextView)findViewById(R.id.email);
        profilepic=(ImageView)findViewById(R.id.profile_pic);
        Signin.setOnClickListener(this);
        Signout.setOnClickListener(this);
        profile.setVisibility(View.GONE);
        GoogleSignInOptions signInOptions= new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleApiClient=new GoogleApiClient.Builder(this).enableAutoManage(this,this).addApi(Auth.GOOGLE_SIGN_IN_API,signInOptions).build();


    }
    private void initializecontrols(){
        loginButton=(LoginButton)findViewById(R.id.login_button);
        txtstatus=(TextView)findViewById(R.id.txtstatus);
        callbackManager=CallbackManager.Factory.create();
    }
    private void Loginwithfb(){
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
txtstatus.setText("success\n"+loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
txtstatus.setText("cancelled");
            }

            @Override
            public void onError(FacebookException error) {

            }
        });
    }
   @Override
   protected void onActivityResult(int requestCode, int result, Intent data) {
        callbackManager.onActivityResult(requestCode, result, data);
        super.onActivityResult(requestCode, result, data);
        if(requestCode==REQ_CODE){
            GoogleSignInResult result1= Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleResult(result1);
        }
    }


    @Override
    public void onClick(View view) {
switch (view.getId()){
    case R.id.sign_in_button:
        signin();
        break;
    case R.id.sign_out:
        signout();
        break;
}
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
    private void signin(){
Intent intent= Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
startActivityForResult(intent,REQ_CODE);
    }
    private void signout(){
Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
    @Override
    public void onResult(@NonNull Status status) {
     updateUI(false);
    }
});
    }
    private void handleResult(GoogleSignInResult result){
if(result.isSuccess()){
    GoogleSignInAccount signInAccount=result.getSignInAccount();
    String name=signInAccount.getDisplayName();
    String email=signInAccount.getEmail();
    String img_url=signInAccount.getPhotoUrl().toString();
    Name.setText(name);
    Email.setText(email);
    Glide.with(this).load(img_url).into(profilepic);
    updateUI(true);

}
else {
    updateUI(false);
}

    }
    private void updateUI(boolean isLogin){
if(isLogin){
    profile.setVisibility(View.VISIBLE);
    Signin.setVisibility(View.GONE);
    loginButton.setVisibility(View.GONE);
    txtstatus.setVisibility(View.GONE);
}
else{
    profile.setVisibility(View.GONE);
    Signin.setVisibility(View.VISIBLE);
    loginButton.setVisibility(View.VISIBLE);
    txtstatus.setVisibility(View.VISIBLE);
}
    }




}
