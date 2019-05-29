package com.mad.appetit;

import static com.mad.mylibrary.SharedClass.ROOT_UID;
import static com.mad.mylibrary.SharedClass.GOOGLE_SIGIN;
import static com.mad.mylibrary.SharedClass.SIGNUP;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

public class MainActivity extends AppCompatActivity {

    private String email, password, errMsg = "";
    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Facebook
        AccessToken accessToken = AccessToken.getCurrentAccessToken();

        FacebookSdk.sdkInitialize(getApplicationContext());
        //AppEventsLogger.activateApp(getApplicationContext());

        // Google
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

        // Firebase
        FirebaseAuth auth = FirebaseAuth.getInstance();

        if(auth.getCurrentUser() == null && account == null && !(accessToken != null && !accessToken.isExpired())){
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Logging...");

            LoginButton fbLogInButton = findViewById(R.id.login_facebook);
            fbLogInButton.setReadPermissions("email");
            callbackManager = CallbackManager.Factory.create();
            fbLogInButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    ROOT_UID = AccessToken.getCurrentAccessToken().getUserId();

                    Intent fragment = new Intent(getApplicationContext(), FragmentManager.class);
                    startActivity(fragment);
                    finish();
                }

                @Override
                public void onCancel() {
                    Snackbar.make(findViewById(R.id.email), "Canceled.", Snackbar.LENGTH_SHORT).show();
                }

                @Override
                public void onError(FacebookException exception) {
                    //Log.w("FACEBOOK LOGIN", "signIn:failure", exception.getMessage());
                    Snackbar.make(findViewById(R.id.email), "Some errors occurred. Try again.", Snackbar.LENGTH_SHORT).show();
                }
            });

            findViewById(R.id.login_google).setOnClickListener(g -> {
                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestEmail()
                        .build();

                GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(this, gso);

                Intent googleLogIn = googleSignInClient.getSignInIntent();
                startActivityForResult(googleLogIn, GOOGLE_SIGIN);
            });

            findViewById(R.id.sign_up).setOnClickListener(e -> {
                Intent login = new Intent(this, SignUp.class);
                startActivityForResult(login, SIGNUP);
            });

            findViewById(R.id.login).setOnClickListener(h -> {
                if(checkFields()){
                    progressDialog.show();

                    auth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(this, task -> {
                                if(task.isSuccessful()) {
                                    ROOT_UID = auth.getUid();

                                    progressDialog.dismiss();

                                    Intent fragment = new Intent(this, FragmentManager.class);
                                    startActivity(fragment);
                                    finish();
                                }
                                else {
                                    //Log.w("LOGIN", "signInWithCredential:failure", task.getException());
                                    progressDialog.dismiss();
                                    Snackbar.make(findViewById(R.id.email), "Authentication Failed. Try again.", Snackbar.LENGTH_SHORT).show();
                                }
                            });
                }
                else{
                    Snackbar.make(findViewById(R.id.email), errMsg, Snackbar.LENGTH_SHORT).show();
                }
            });
        }
        else{
            if(auth.getCurrentUser() != null)
                ROOT_UID = auth.getCurrentUser().getUid();
            else if(account != null)
                ROOT_UID = account.getId();
            else
                ROOT_UID = accessToken.getUserId();

            Intent fragment = new Intent(this, FragmentManager.class);
            startActivity(fragment);
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

        if(data != null && resultCode == SIGNUP){
            Intent fragment = new Intent(this, FragmentManager.class);
            startActivity(fragment);
            finish();
        }

        if(resultCode == Activity.RESULT_OK && requestCode == GOOGLE_SIGIN){
            try{
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                GoogleSignInAccount account = task.getResult(ApiException.class);

                ROOT_UID = account.getId();

                Intent fragment = new Intent(this, FragmentManager.class);
                startActivity(fragment);
                finish();
            }
            catch(ApiException e){
                //Log.w(GOOGLE LOGIN, "signInResult:failed code=" + e.getStatusCode());
                Snackbar.make(findViewById(R.id.email), "Some errors occurred. Try again.", Snackbar.LENGTH_SHORT).show();
            }
        }
    }

    public boolean checkFields(){
        email = ((EditText)findViewById(R.id.email)).getText().toString();
        password = ((EditText)findViewById(R.id.password)).getText().toString();

        if(email.trim().length() == 0 || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            errMsg = "Invalid Mail";
            return false;
        }

        if(password.trim().length() == 0){
            errMsg = "Fill password";
            return false;
        }

        return true;
    }
}