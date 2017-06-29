package com.gp.smarthome.hamdy.smarthomegp.Activities;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.gp.smarthome.hamdy.smarthomegp.Controllers.LoginController;
import com.gp.smarthome.hamdy.smarthomegp.R;
import com.gp.smarthome.hamdy.smarthomegp.Utilities.AlertHelper;

import java.util.ArrayList;

import Vendor.Errors.Error;

public class LoginActivity extends AppCompatActivity {


    public EditText emailET;
    public EditText passwordET;
    public FrameLayout mProgressContainer;
    public View mLoginFormView;
    public Button signInBtn;
    public LoginController controller;
    public TextInputLayout passwordTIL;
    public TextInputLayout emailTIL;
    private RelativeLayout container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        container =(RelativeLayout) findViewById(R.id.activity_login_container_ll);
        emailET = (EditText) findViewById(R.id.email);
        passwordET = (EditText) findViewById(R.id.password);


        emailTIL = (TextInputLayout) findViewById(R.id.email_til);
        passwordTIL = (TextInputLayout) findViewById(R.id.password_til);


        signInBtn = (Button) findViewById(R.id.sign_in_btn);

        mLoginFormView = findViewById(R.id.login_form);
        mProgressContainer = (FrameLayout)findViewById(R.id.activity_login_progress_container_rl);

        controller = new LoginController(this);

        controller.actionListener();

    }


    public void displayValidationErrors(ArrayList<Error> errors) {

        for(int i = 0 ; i < errors.size() ; i++){
            errors.get(i).displayError();
        }

        progress(false);

    }

    public void displayLoginMessage(String message) {
//        loginErrorTV.setText(message);

        AlertHelper.showLongSnackBar(container , message);
        progress(false);

    }


    public static void start(Context context) {

        Intent i = new Intent(context, LoginActivity.class);
        context.startActivity(i);
        ((AppCompatActivity) context).finish();
    }

    public void progress(boolean b) {
        mProgressContainer.setVisibility(b ?  View.VISIBLE : View.GONE);
    }
}
