package com.atahani.retrofit_sample.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.atahani.retrofit_sample.R;
import com.atahani.retrofit_sample.models.ErrorModel;
import com.atahani.retrofit_sample.models.RoleModel;
import com.atahani.retrofit_sample.models.UserModel;
import com.atahani.retrofit_sample.network.FakeDataProvider;
import com.atahani.retrofit_sample.network.FakeDataService;
import com.atahani.retrofit_sample.utility.ErrorUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreatOrEditOrDetailUser extends AppCompatActivity {

    private EditText mETxemail;
    private TextView mTxemail;
    private EditText mETxpassword;
    private EditText mETxsalary;

    private TextView mTxsalary;
    private EditText mETxbenefit;
    private TextView mTxbenefit;
    private RadioGroup radio_group;
    private RadioButton radio_StoreKeeper;
    private RadioButton radio_Administrator;
    private RadioButton radio_Accountant;
     private RadioButton radio_Secretary;
    private int mActionToDo = 3;
    private String IdInEditMode;
    private FakeDataService mTService;
    private TextView tx_AbsentDays;
    private TextView tx_view_AbsentDays;
    private TextView tx_view_OverTime;
    private TextView tx_OverTime;
    private TextView tx_Role;
    private Bundle args;
   private UserModel userModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creat_or_edit_or_detail_user);
        //get argument and check is in edit mode
        args = getIntent().getExtras();
        if (args != null) {
            mActionToDo = args.getInt("ACTION_TO_DO_KEY");
            if (mActionToDo == 2) {
                IdInEditMode = args.getString("ID_KEY");
            }
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.User_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mETxemail = (EditText) findViewById(R.id.etx_email);
        mTxemail=(TextView) findViewById(R.id.tx_email);
        mETxpassword = (EditText) findViewById(R.id.etx_password);
        mETxsalary = (EditText) findViewById(R.id.etx_salary);
        mTxsalary=(TextView) findViewById(R.id.tx_salary);
        mETxbenefit = (EditText) findViewById(R.id.etx_Benefits);
        mTxbenefit=(TextView) findViewById(R.id.tx_Benefits);
        tx_AbsentDays=(TextView) findViewById(R.id.tx_AbsentDays);
        tx_view_AbsentDays=(TextView) findViewById(R.id.tx_view_AbsentDays);
        tx_view_OverTime=(TextView)findViewById(R.id.tx_view_OverTime);
        tx_OverTime=(TextView)findViewById(R.id.tx_OverTime);
        tx_Role=(TextView)findViewById(R.id.tx_role);
        radio_group=(RadioGroup)findViewById(R.id.radio_group);
        radio_StoreKeeper=(RadioButton)findViewById(R.id.radio_StoreKeeper);
        radio_Administrator=(RadioButton)findViewById(R.id.radio_Administrator);
        radio_Accountant=(RadioButton)findViewById(R.id.radio_Accountant);
        radio_Secretary=(RadioButton)findViewById(R.id.radio_Secretary);
        //first create new instant of FakeDataProvider
        FakeDataProvider provider = new FakeDataProvider();
        //get the FakeDataService interface to call API routes
        mTService = provider.getTService();

        //check if in edit mode get  information from server and assign in
        //NOTE: this is just for test , in real world should save  in db and now get from db !
        if (mActionToDo == 2 && !IdInEditMode.equals("")) {
            //get  by id from server
            Call<UserModel> call = mTService.getUserById(IdInEditMode);
            call.enqueue(new Callback<UserModel>() {
                @Override
                public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                    if (response.isSuccessful()) {
                        //bind value to fields
                        mETxemail.setText(response.body().UserName);
                        mTxemail.setText(response.body().UserName);
                       // mETxpassword.setText(response.body().);
                        mTxsalary.setText(response.body().Salary+"");
                        mETxsalary.setText(response.body().Salary+"");
                        mTxbenefit.setText(response.body().Benefits+"");
                        mETxbenefit.setText(response.body().Benefits+"");

                        tx_AbsentDays.setText(response.body().AbsentDays+"");
                        tx_OverTime.setText(response.body().OverTime+"");
                      //  tx_Role.setText((CharSequence) response.body().Roles.get(0)).;

                    } else {
                        ErrorModel errorModel = ErrorUtils.parseError(response);
                        Toast.makeText(getBaseContext(), "Error type is " + errorModel.Message + " , description " + errorModel.MessageDetail, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<UserModel> call, Throwable t) {
                    //occur when fail to deserialize || no network connection || server unavailable
                    Toast.makeText(getBaseContext(), "Fail it >> " + t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (mActionToDo == 2 && args.getInt("detail")!=01) {
            getMenuInflater().inflate(R.menu.menu_edit, menu);
            getSupportActionBar().setTitle("تغییر کاربر");
            return super.onCreateOptionsMenu(menu);

        }
        int a=args.getInt("detail");
        if (a==01){
            getMenuInflater().inflate(R.menu.menu, menu);

            getSupportActionBar().setTitle("جزئیات کاربر");
            mETxbenefit.setVisibility(View.GONE);
            mTxbenefit.setVisibility(View.VISIBLE);
            mETxsalary.setVisibility(View.GONE);
            mTxsalary.setVisibility(View.VISIBLE);
            mETxpassword.setVisibility(View.GONE);
            mETxemail.setVisibility(View.GONE);
            tx_Role.setVisibility(View.VISIBLE);
            radio_group.setVisibility(View.GONE);
            return super.onCreateOptionsMenu(menu);

        }

        else {
            getMenuInflater().inflate(R.menu.menu_send, menu);
            getSupportActionBar().setTitle("ایجاد کاربر");
            tx_AbsentDays.setVisibility(View.GONE);
            tx_view_AbsentDays.setVisibility(View.GONE);
            tx_view_OverTime.setVisibility(View.GONE);
            tx_OverTime.setVisibility(View.GONE);
            return super.onCreateOptionsMenu(menu);

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_send) {

            //create  Model
             userModel = new UserModel();
            //assign  model values
           // userrModel.UserName = mETxemail.getText().toString();
            userModel.Salary = Integer.parseInt(mETxsalary.getText().toString());
            userModel.Benefits = Integer.parseInt(mETxbenefit.getText().toString());
//            userModel.PhoneNumber = Integer.parseInt(mETxPhoneNumber.getText().toString());


            //create  generic class to send request to server
            Call<UserModel> call = mTService.createNewUser(userModel);
            //Async request
            //NOTE: you should always send Async request since the sync request cause crash in your application
            call.enqueue(new Callback<UserModel>() {
                @Override
                public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(getBaseContext(), "موفق در ایجاد کاربر", Toast.LENGTH_LONG).show();
                        //finish this activity with result OK to refresh the data from server
                        setResult(-1);
                        finish();
                    } else {
                        ErrorModel errorModel = ErrorUtils.parseError(response);
                        Toast.makeText(getBaseContext(), "Error type is " + errorModel.Message + " , description " + errorModel.MessageDetail, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<UserModel> call, Throwable t) {
                    //occur when fail to deserialize || no network connection || server unavailable
                    Toast.makeText(getBaseContext(), "Fail it >> " + t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });

        } else if (id == R.id.action_edit) {
            //create  Model
             userModel = new UserModel();
            //assign  model values
            // userrModel.UserName = mETxemail.getText().toString();
            userModel.Salary = Integer.parseInt(mETxsalary.getText().toString());
            userModel.Benefits = Integer.parseInt(mETxbenefit.getText().toString());

            Call<UserModel> call = mTService.updateUserById(IdInEditMode, userModel);
            call.enqueue(new Callback<UserModel>() {
                @Override
                public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(getBaseContext(), "Successfully updated", Toast.LENGTH_LONG).show();
                        //finish this activity with result OK to refresh the data from server
                        setResult(-1);
                        finish();
                    } else {
                        ErrorModel errorModel = ErrorUtils.parseError(response);
                        Toast.makeText(getBaseContext(), "Error type is " + errorModel.Message + " , description " + errorModel.MessageDetail, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<UserModel> call, Throwable t) {
                    //occur when fail to deserialize || no network connection || server unavailable
                    Toast.makeText(getBaseContext(), "Fail it >> " + t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });

        } else if (id == android.R.id.home) {
            //back to main activity
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radio_Accountant:
                if (checked)
                   // userModel.Roles.set(0, );
                    break;
            case R.id.radio_Administrator:
                if (checked)
                    // Ninjas rule
                    break;
            case R.id.radio_Secretary:
                if (checked)
                    // Ninjas rule
                    break;
            case R.id.radio_StoreKeeper:
                if (checked)
                    // Ninjas rule
                    break;
        }
    }
}
