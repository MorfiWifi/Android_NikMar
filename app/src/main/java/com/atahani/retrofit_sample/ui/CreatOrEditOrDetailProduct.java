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
import com.atahani.retrofit_sample.models.ProductModel;
import com.atahani.retrofit_sample.network.FakeDataProvider;
import com.atahani.retrofit_sample.network.FakeDataService;
import com.atahani.retrofit_sample.utility.ErrorUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreatOrEditOrDetailProduct extends AppCompatActivity {

    private EditText mETxProductName;
    private TextView mTxProductName;
    private EditText mETxprice;
    private TextView mTxPrice;
    private EditText mETxStock;
    private TextView mTxStock;

    private int mActionToDo = 3;
    private String IdInEditMode;
    private FakeDataService mTService;


    private Bundle args;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creat_or_edit_or_detail_product);
        //get argument and check is in edit mode
        args = getIntent().getExtras();
        if (args != null) {
            mActionToDo = args.getInt("ACTION_TO_DO_KEY");
            if (mActionToDo == 2) {
                IdInEditMode = args.getString("ID_KEY");
            }
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.Product_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mETxProductName = (EditText) findViewById(R.id.etx_NameProduct);
        mTxProductName=(TextView) findViewById(R.id.tx_NameProduct);
        mETxStock = (EditText) findViewById(R.id.etx_Stock);
        mTxStock=(TextView) findViewById(R.id.tx_Stock);
        mETxprice = (EditText) findViewById(R.id.etx_Price);
        mTxPrice=(TextView) findViewById(R.id.tx_Price);


        //first create new instant of FakeDataProvider
        FakeDataProvider provider = new FakeDataProvider();
        //get the FakeDataService interface to call API routes
        mTService = provider.getTService();

        //check if in edit mode get  information from server and assign in
        //NOTE: this is just for test , in real world should save  in db and now get from db !
        if (mActionToDo == 2 && !IdInEditMode.equals("")) {
            //get tweet by id from server
            Call<ProductModel> call = mTService.getProductById(IdInEditMode);
            call.enqueue(new Callback<ProductModel>() {
                @Override
                public void onResponse(Call<ProductModel> call, Response<ProductModel> response) {
                    if (response.isSuccessful()) {
                        //bind value to fields
                        mETxProductName.setText(response.body().Name);
                        mTxProductName.setText(response.body().Name);
                        mETxprice.setText(response.body().UnitPrice+"");
                        mTxPrice.setText(response.body().UnitPrice+"");
                        mETxStock.setText(response.body().UnitsInStock+"");
                        mTxStock.setText(response.body().UnitsInStock+"");


                    } else {
                        ErrorModel errorModel = ErrorUtils.parseError(response);
                        Toast.makeText(getBaseContext(), "Error type is " + errorModel.Message + " , description " + errorModel.MessageDetail, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ProductModel> call, Throwable t) {
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
            getSupportActionBar().setTitle("تغییر کالا");
            return super.onCreateOptionsMenu(menu);

        }
        int a=args.getInt("detail");
        if (a==01){
            getMenuInflater().inflate(R.menu.menu, menu);

            getSupportActionBar().setTitle("جزئیات کالا");
            mETxStock.setVisibility(View.GONE);
            mTxStock.setVisibility(View.VISIBLE);
            mETxprice.setVisibility(View.GONE);
            mTxPrice.setVisibility(View.VISIBLE);
            mETxProductName.setVisibility(View.GONE);
            mTxProductName.setVisibility(View.VISIBLE);

            return super.onCreateOptionsMenu(menu);

        }

        else {
            getMenuInflater().inflate(R.menu.menu_send, menu);
            getSupportActionBar().setTitle("ایجاد کالا");

            return super.onCreateOptionsMenu(menu);

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_send) {

            //create  Model
            ProductModel productModel = new ProductModel();
            //assign  model values
            productModel.Name = mETxProductName.getText().toString();
            productModel.UnitPrice = Integer.parseInt(mETxprice.getText().toString());

            productModel.UnitsInStock = Integer.parseInt(mETxStock.getText().toString());

            //create  generic class to send request to server
            Call<ProductModel> call = mTService.createNewProduct(productModel);
            //Async request
            //NOTE: you should always send Async request since the sync request cause crash in your application
            call.enqueue(new Callback<ProductModel>() {
                @Override
                public void onResponse(Call<ProductModel> call, Response<ProductModel> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(getBaseContext(), "موفق در ایجاد کالا", Toast.LENGTH_LONG).show();
                        //finish this activity with result OK to refresh the data from server
                        setResult(-1);
                        finish();
                    } else {
                        ErrorModel errorModel = ErrorUtils.parseError(response);
                        Toast.makeText(getBaseContext(), "Error type is " + errorModel.Message + " , description " + errorModel.MessageDetail, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ProductModel> call, Throwable t) {
                    //occur when fail to deserialize || no network connection || server unavailable
                    Toast.makeText(getBaseContext(), "Fail it >> " + t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });

        } else if (id == R.id.action_edit) {
            //create  Model
            ProductModel productModel = new ProductModel();
            //assign  model values
            productModel.Name = mETxProductName.getText().toString();
            productModel.UnitPrice = Integer.parseInt(mETxprice.getText().toString());

            productModel.UnitsInStock = Integer.parseInt(mETxStock.getText().toString());

            Call<ProductModel> call = mTService.updateProductById(IdInEditMode, productModel);
            call.enqueue(new Callback<ProductModel>() {
                @Override
                public void onResponse(Call<ProductModel> call, Response<ProductModel> response) {
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
                public void onFailure(Call<ProductModel> call, Throwable t) {
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
