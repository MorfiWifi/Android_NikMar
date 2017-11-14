package com.atahani.retrofit_sample.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.atahani.retrofit_sample.R;
import com.atahani.retrofit_sample.models.CallModel;
import com.atahani.retrofit_sample.models.ErrorModel;
import com.atahani.retrofit_sample.models.SupplierModel;
import com.atahani.retrofit_sample.network.FakeDataProvider;
import com.atahani.retrofit_sample.network.FakeDataService;
import com.atahani.retrofit_sample.utility.ErrorUtils;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by m.hosein on 11/10/2017.
 */

public class CreateorEditSupplier extends AppCompatActivity {


    private EditText mETxCompanyName;
    private TextView mTxCompanyName;
    private EditText mETxName;
    private TextView mTxName;
    private EditText mETxadress;
    private TextView mTxadress;
    private EditText mETxPhoneNumber;
    private TextView mTxPhoneNumber;
    private int mActionToDo = 3;
    private String IdInEditMode;
    private FakeDataService mTService;
    private TextView mTxprice;
    private TextView mTxstock;
    private TextView mTxviewprice;
    private TextView mTxviewstock;

    private Bundle args;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_or_edit_supplier);
        //get argument and check is in edit mode
        args = getIntent().getExtras();
        if (args != null) {
            mActionToDo = args.getInt("ACTION_TO_DO_KEY");
            if (mActionToDo == 2) {
                IdInEditMode = args.getString("ID_KEY");
            }
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.supplier_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mETxCompanyName = (EditText) findViewById(R.id.etx_camponyname);
        mTxCompanyName=(TextView) findViewById(R.id.tx_camponyname);
        mETxName = (EditText) findViewById(R.id.etx_name);
        mTxName=(TextView) findViewById(R.id.tx_name);
        mETxPhoneNumber = (EditText) findViewById(R.id.etx_PhoneNumber);
        mTxPhoneNumber=(TextView) findViewById(R.id.tx_PhoneNumber);
        mETxadress = (EditText) findViewById(R.id.etx_adress);
        mTxadress=(TextView) findViewById(R.id.tx_adress);
        mTxprice=(TextView) findViewById(R.id.tx_price);
        mTxstock=(TextView) findViewById(R.id.tx_stock);
        mTxviewprice=(TextView)findViewById(R.id.tx_viewprice);
        mTxviewstock=(TextView)findViewById(R.id.tx_viewstock);

        //first create new instant of FakeDataProvider
        FakeDataProvider provider = new FakeDataProvider();
        //get the FakeDataService interface to call API routes
        mTService = provider.getTService();

        //check if in edit mode get  information from server and assign in
        //NOTE: this is just for test , in real world should save  in db and now get from db !
        if (mActionToDo == 2 && !IdInEditMode.equals("")) {
            //get tweet by id from server
            Call<SupplierModel> call = mTService.getSupplierById(IdInEditMode);
            call.enqueue(new Callback<SupplierModel>() {
                @Override
                public void onResponse(Call<SupplierModel> call, Response<SupplierModel> response) {
                    if (response.isSuccessful()) {
                        //bind value to fields
                        mETxCompanyName.setText(response.body().CompanyName);
                        mTxCompanyName.setText(response.body().CompanyName);
                        mETxName.setText(response.body().Name);
                        mTxName.setText(response.body().Name);
                        mETxadress.setText(response.body().Address);
                        mTxadress.setText(response.body().Address);
                        mETxPhoneNumber.setText(response.body().PhoneNumber+"");

                       mTxPhoneNumber.setText(response.body().PhoneNumber+"");
                        mTxstock.setText(response.body().UnitsInStock+"");
                        mTxprice.setText(response.body().Price+"");

                    } else {
                        ErrorModel errorModel = ErrorUtils.parseError(response);
                        Toast.makeText(getBaseContext(), "Error type is " + errorModel.Message + " , description " + errorModel.MessageDetail, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<SupplierModel> call, Throwable t) {
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
            getSupportActionBar().setTitle("تغییر تولید کننده");
            return super.onCreateOptionsMenu(menu);

        }
        int a=args.getInt("detail");
        if (a==01){
            getMenuInflater().inflate(R.menu.menu, menu);

            getSupportActionBar().setTitle("جزئیات تولید کننده");
            mETxCompanyName.setVisibility(View.GONE);
            mTxCompanyName.setVisibility(View.VISIBLE);
            mETxName.setVisibility(View.GONE);
            mTxName.setVisibility(View.VISIBLE);
            mETxPhoneNumber.setVisibility(View.GONE);
            mTxPhoneNumber.setVisibility(View.VISIBLE);
            mETxadress.setVisibility(View.GONE);
            mTxadress.setVisibility(View.VISIBLE);
            return super.onCreateOptionsMenu(menu);

        }

        else {
            getMenuInflater().inflate(R.menu.menu_send, menu);
            getSupportActionBar().setTitle("ایجاد تولید کننده");
            mTxprice.setVisibility(View.GONE);
            mTxstock.setVisibility(View.GONE);
            mTxviewstock.setVisibility(View.GONE);
            mTxviewprice.setVisibility(View.GONE);
            return super.onCreateOptionsMenu(menu);

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_send) {

            //create  Model
            SupplierModel supplierModel = new SupplierModel();
            //assign  model values
            supplierModel.CompanyName = mETxCompanyName.getText().toString();
            supplierModel.Address = mETxadress.getText().toString();
            supplierModel.Name = mETxName.getText().toString();
            supplierModel.PhoneNumber = Integer.parseInt(mETxPhoneNumber.getText().toString());

            //create  generic class to send request to server
            Call<SupplierModel> call = mTService.createNewSupplier(supplierModel);
            //Async request
            //NOTE: you should always send Async request since the sync request cause crash in your application
            call.enqueue(new Callback<SupplierModel>() {
                @Override
                public void onResponse(Call<SupplierModel> call, Response<SupplierModel> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(getBaseContext(), "موفق در ایجاد تهیه کننده", Toast.LENGTH_LONG).show();
                        //finish this activity with result OK to refresh the data from server
                        setResult(-1);
                        finish();
                    } else {
                        ErrorModel errorModel = ErrorUtils.parseError(response);
                        Toast.makeText(getBaseContext(), "Error type is " + errorModel.Message + " , description " + errorModel.MessageDetail, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<SupplierModel> call, Throwable t) {
                    //occur when fail to deserialize || no network connection || server unavailable
                    Toast.makeText(getBaseContext(), "Fail it >> " + t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });

        } else if (id == R.id.action_edit) {
            //create  Model
            SupplierModel supplierModel = new SupplierModel();
            //assign  model values
            supplierModel.CompanyName = mETxCompanyName.getText().toString();
            supplierModel.Address = mETxadress.getText().toString();
            supplierModel.Name = mETxName.getText().toString();
            supplierModel.PhoneNumber = Integer.parseInt(mETxPhoneNumber.getText().toString());

            Call<SupplierModel> call = mTService.updateSupplierById(IdInEditMode, supplierModel);
            call.enqueue(new Callback<SupplierModel>() {
                @Override
                public void onResponse(Call<SupplierModel> call, Response<SupplierModel> response) {
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
                public void onFailure(Call<SupplierModel> call, Throwable t) {
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
