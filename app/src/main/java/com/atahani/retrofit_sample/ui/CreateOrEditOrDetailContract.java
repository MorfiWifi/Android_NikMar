package com.atahani.retrofit_sample.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.atahani.retrofit_sample.R;
import com.atahani.retrofit_sample.models.ContractModel;
import com.atahani.retrofit_sample.models.ErrorModel;
import com.atahani.retrofit_sample.network.FakeDataProvider;
import com.atahani.retrofit_sample.network.FakeDataService;
import com.atahani.retrofit_sample.utility.ErrorUtils;



import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateOrEditOrDetailContract extends AppCompatActivity {

    private EditText mETxContract;
    private TextView mTxContract;
    private int mActionToDo = 3;
    private String IdInEditMode;
    private FakeDataService mTService;
    private Bundle args;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_or_edit_or_detail_contract);
        //get argument and check is in edit mode
        args = getIntent().getExtras();
        if (args != null) {
            mActionToDo = args.getInt("ACTION_TO_DO_KEY");
            if (mActionToDo == 2) {
                IdInEditMode = args.getString("ID_KEY");
            }
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.contract_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mETxContract = (EditText) findViewById(R.id.etx_contract);
        mTxContract=(TextView) findViewById(R.id.tx_contract);
        //the default mode is happy so we selected

        //first create new instant of FakeDataProvider
        FakeDataProvider provider = new FakeDataProvider();
        //get the FakeDataService interface to call API routes
        mTService = provider.getTService();

        //check if in edit mode get tweet information from server and assign in
        //NOTE: this is just for test , in real world should save tweet in db and now get from db !
        if (mActionToDo == 2 && !IdInEditMode.equals("")) {
            //get tweet by id from server
            Call<ContractModel> call = mTService.getContractById(IdInEditMode);
            call.enqueue(new Callback<ContractModel>() {
                @Override
                public void onResponse(Call<ContractModel> call, Response<ContractModel> response) {
                    if (response.isSuccessful()) {
                        //bind value to fields
                        mETxContract.setText(response.body().Content);
                        mTxContract.setText(response.body().Content);
                        //get code point of emoji

                    } else {
                        ErrorModel errorModel = ErrorUtils.parseError(response);
                        Toast.makeText(getBaseContext(), "Error type is " + errorModel.Message + " , description " + errorModel.MessageDetail, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ContractModel> call, Throwable t) {
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
            getSupportActionBar().setTitle("تغییر قرارداد");
            return super.onCreateOptionsMenu(menu);

        }
        int a=args.getInt("detail");
        if (a==01){
            getMenuInflater().inflate(R.menu.menu, menu);

            getSupportActionBar().setTitle("جزئیات قرارداد");
            mETxContract.setVisibility(View.GONE);
            mTxContract.setVisibility(View.VISIBLE);
            return super.onCreateOptionsMenu(menu);

        }

        else {
            getMenuInflater().inflate(R.menu.menu_send, menu);
            getSupportActionBar().setTitle("ایجاد قرارداد");
            return super.onCreateOptionsMenu(menu);

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_send) {

            //create call Model
            ContractModel contractModel = new ContractModel();
            //assign tweet model values
            contractModel.Content= mETxContract.getText().toString();
            //create call generic class to send request to server
            Call<ContractModel> call = mTService.createNewContract(contractModel);
            //Async request
            //NOTE: you should always send Async request since the sync request cause crash in your application
            call.enqueue(new Callback<ContractModel>() {
                @Override
                public void onResponse(Call<ContractModel> call, Response<ContractModel> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(getBaseContext(), "موفق در ایجاد قرارداد", Toast.LENGTH_LONG).show();
                        //finish this activity with result OK to refresh the data from server
                        setResult(-1);
                        finish();
                    } else {
                        ErrorModel errorModel = ErrorUtils.parseError(response);
                        Toast.makeText(getBaseContext(), "Error type is " + errorModel.Message + " , description " + errorModel.MessageDetail, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ContractModel> call, Throwable t) {
                    //occur when fail to deserialize || no network connection || server unavailable
                    Toast.makeText(getBaseContext(), "Fail it >> " + t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });

        } else if (id == R.id.action_edit) {
            //create call Model
            ContractModel contractModel = new ContractModel();
            //assign tweet model values
            contractModel.Content= mETxContract.getText().toString();
            //create call generic class to send request to server
            Call<ContractModel> call = mTService.updateContractById(IdInEditMode, contractModel);
            call.enqueue(new Callback<ContractModel>() {
                @Override
                public void onResponse(Call<ContractModel> call, Response<ContractModel> response) {
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
                public void onFailure(Call<ContractModel> call, Throwable t) {
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
}
