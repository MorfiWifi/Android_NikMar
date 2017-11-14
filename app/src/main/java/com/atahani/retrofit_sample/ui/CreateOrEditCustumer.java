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
import com.atahani.retrofit_sample.models.ErrorModel;
import com.atahani.retrofit_sample.models.CustomerModel;
import com.atahani.retrofit_sample.network.FakeDataProvider;
import com.atahani.retrofit_sample.network.FakeDataService;
import com.atahani.retrofit_sample.utility.ErrorUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateOrEditCustumer extends AppCompatActivity {

    private EditText mETxCompanyName;
    private TextView mTxCompanyName;
    private EditText mETxNameCostumer;
    private TextView mTxNameCostumer;
    private EditText mETxPhoneNumber;
    private TextView mTxPhoneNumber;
    private int mActionToDo = 3;
    private String IdInEditMode;
    private FakeDataService mTService;
    private TextView mTxallpricecustom;
    private TextView mTxallproductcostum;
    private TextView mTxviewpallpricecustom;
    private TextView mTxviewallproductcostum;

    private Bundle args;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_or_edit_custumer);
        //get argument and check is in edit mode
        args = getIntent().getExtras();
        if (args != null) {
            mActionToDo = args.getInt("ACTION_TO_DO_KEY");
            if (mActionToDo == 2) {
                IdInEditMode = args.getString("ID_KEY");
            }
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.costumer_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mETxCompanyName = (EditText) findViewById(R.id.etx_companynamecostum);
        mTxCompanyName=(TextView) findViewById(R.id.tx_companynamecostum);
        mETxNameCostumer = (EditText) findViewById(R.id.etx_namecostumer);
        mTxNameCostumer=(TextView) findViewById(R.id.tx_namecostumercostum);
        mETxPhoneNumber = (EditText) findViewById(R.id.etx_PhoneNumbercostum);
        mTxPhoneNumber=(TextView) findViewById(R.id.tx_PhoneNumbercostum);

        mTxallproductcostum=(TextView) findViewById(R.id.tx_allproductcostum);
        mTxallpricecustom=(TextView) findViewById(R.id.tx_allpricecustom);
        mTxviewallproductcostum=(TextView)findViewById(R.id.tx_viewallproductcostum);
        mTxviewpallpricecustom=(TextView)findViewById(R.id.tx_viewallpricecustom);

        //first create new instant of FakeDataProvider
        FakeDataProvider provider = new FakeDataProvider();
        //get the FakeDataService interface to call API routes
        mTService = provider.getTService();

        //check if in edit mode get  information from server and assign in
        //NOTE: this is just for test , in real world should save  in db and now get from db !
        if (mActionToDo == 2 && !IdInEditMode.equals("")) {
            //get tweet by id from server
            Call<CustomerModel> call = mTService.getCustomerById(IdInEditMode);
            call.enqueue(new Callback<CustomerModel>() {
                @Override
                public void onResponse(Call<CustomerModel> call, Response<CustomerModel> response) {
                    if (response.isSuccessful()) {
                        //bind value to fields
                        mETxCompanyName.setText(response.body().CompanyName);
                        mTxCompanyName.setText(response.body().CompanyName);
                        mETxNameCostumer.setText(response.body().Name);
                        mTxNameCostumer.setText(response.body().Name);
                        mETxPhoneNumber.setText(response.body().PhoneNumber+"");
                        mTxPhoneNumber.setText(response.body().PhoneNumber+"");
                        mTxallpricecustom.setText(response.body().PriceOfAllProductsPurchased+"");
                        mTxallproductcostum.setText(response.body().AllProductsPurchased+"");

                    } else {
                        ErrorModel errorModel = ErrorUtils.parseError(response);
                        Toast.makeText(getBaseContext(), "Error type is " + errorModel.Message + " , description " + errorModel.MessageDetail, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<CustomerModel> call, Throwable t) {
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
            getSupportActionBar().setTitle("تغییر مشتری");
            return super.onCreateOptionsMenu(menu);

        }
        int a=args.getInt("detail");
        if (a==01){
            getMenuInflater().inflate(R.menu.menu, menu);

            getSupportActionBar().setTitle("جزئیات مشتری");
            mETxCompanyName.setVisibility(View.GONE);
            mTxCompanyName.setVisibility(View.VISIBLE);
            mETxNameCostumer.setVisibility(View.GONE);
            mTxNameCostumer.setVisibility(View.VISIBLE);
            mETxPhoneNumber.setVisibility(View.GONE);
            mTxPhoneNumber.setVisibility(View.VISIBLE);

            return super.onCreateOptionsMenu(menu);

        }

        else {
            getMenuInflater().inflate(R.menu.menu_send, menu);
            getSupportActionBar().setTitle("ایجاد مشتری");
            mTxallproductcostum.setVisibility(View.GONE);
            mTxallpricecustom.setVisibility(View.GONE);
            mTxviewpallpricecustom.setVisibility(View.GONE);
            mTxviewallproductcostum.setVisibility(View.GONE);
            return super.onCreateOptionsMenu(menu);

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_send) {

            //create  Model
            CustomerModel customerModel = new CustomerModel();
            //assign  model values
            customerModel.CompanyName = mETxCompanyName.getText().toString();
            customerModel.Name = mETxNameCostumer.getText().toString();
            customerModel.PhoneNumber = Integer.parseInt(mETxPhoneNumber.getText().toString());

            //create  generic class to send request to server
            Call<CustomerModel> call = mTService.createNewCustomer(customerModel);
            //Async request
            //NOTE: you should always send Async request since the sync request cause crash in your application
            call.enqueue(new Callback<CustomerModel>() {
                @Override
                public void onResponse(Call<CustomerModel> call, Response<CustomerModel> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(getBaseContext(), "موفق در ایجاد مشتری", Toast.LENGTH_LONG).show();
                        //finish this activity with result OK to refresh the data from server
                        setResult(-1);
                        finish();
                    } else {
                        ErrorModel errorModel = ErrorUtils.parseError(response);
                        Toast.makeText(getBaseContext(), "Error type is " + errorModel.Message + " , description " + errorModel.MessageDetail, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<CustomerModel> call, Throwable t) {
                    //occur when fail to deserialize || no network connection || server unavailable
                    Toast.makeText(getBaseContext(), "Fail it >> " + t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });

        } else if (id == R.id.action_edit) {
            //create  Model
            CustomerModel customerModel = new CustomerModel();
            //assign  model values
            customerModel.CompanyName = mETxCompanyName.getText().toString();
            customerModel.Name = mETxNameCostumer.getText().toString();
            customerModel.PhoneNumber = Integer.parseInt(mETxPhoneNumber.getText().toString());

            Call<CustomerModel> call = mTService.updatecostumerById(IdInEditMode, customerModel);
            call.enqueue(new Callback<CustomerModel>() {
                @Override
                public void onResponse(Call<CustomerModel> call, Response<CustomerModel> response) {
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
                public void onFailure(Call<CustomerModel> call, Throwable t) {
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
