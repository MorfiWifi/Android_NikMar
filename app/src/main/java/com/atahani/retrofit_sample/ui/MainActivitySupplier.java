package com.atahani.retrofit_sample.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.atahani.retrofit_sample.R;
import com.atahani.retrofit_sample.adapter.DataAdapterSupplier;
import com.atahani.retrofit_sample.models.CallModel;
import com.atahani.retrofit_sample.models.ErrorModel;
import com.atahani.retrofit_sample.models.SupplierModel;
import com.atahani.retrofit_sample.network.FakeDataProvider;
import com.atahani.retrofit_sample.network.FakeDataService;
import com.atahani.retrofit_sample.utility.ErrorUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by m.hosein on 11/10/2017.
 */

public class MainActivitySupplier  extends AppCompatActivity {
    private DataAdapterSupplier mAdapter;
    private FakeDataService mTService;
    private RecyclerView mRylist;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.default_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("لیست تهیه کنندگان");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        FakeDataProvider provider = new FakeDataProvider();
        mTService = provider.getTService();
        //config recycler view
        mRylist = (RecyclerView) findViewById(R.id.ry_list);
        mRylist.setLayoutManager(new LinearLayoutManager(this));

        mAdapter = new DataAdapterSupplier(this, new DataAdapterSupplier.DataEventHandler() {
            @Override
            public void onEditData(String Id, int position) {
                //start activity to edit
                Intent editIntent = new Intent(getBaseContext(), CreateorEditSupplier.class);
                editIntent.putExtra("ACTION_TO_DO_KEY", 2);
                editIntent.putExtra("ID_KEY", Id);
                startActivityForResult(editIntent, 7);
            }

            @Override
            public void onDeleteData(String Id, final int position) {
                Call<SupplierModel> call = mTService.deleteSupplierById(Id);
                //async request
                call.enqueue(new Callback<SupplierModel>() {
                    @Override
                    public void onResponse(Call<SupplierModel> call, Response<SupplierModel> response) {
                        if (response.isSuccessful()) {
                            //get  from server just
                            getSupplierFromServer();
                        } else {
                            ErrorModel errorModel = ErrorUtils.parseError(response);
                            Toast.makeText(getBaseContext(), "Error type is " + errorModel.Message + " description " + errorModel.MessageDetail, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<SupplierModel> call, Throwable t) {
                        //occur when fail to deserialize || no network connection || server unavailable
                        Toast.makeText(getBaseContext(), "Fail it >> " + t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onDetailData(String Id, int position) {
                Intent detailIntent = new Intent(getBaseContext(), CreateorEditSupplier.class);
                detailIntent.putExtra("ACTION_TO_DO_KEY", 2);
                detailIntent.putExtra("ID_KEY", Id);
                detailIntent.putExtra("detail",01);
                startActivity(detailIntent);

            }
        });
        mRylist.setAdapter(mAdapter);
        //get tweets in load
        getSupplierFromServer();

    }

    /**
     * get tweets from server
     */
    private void getSupplierFromServer() {
        Call<List<SupplierModel>> call = mTService.getSupplier();
        call.enqueue(new Callback<List<SupplierModel>>() {
            @Override
            public void onResponse(Call<List<SupplierModel>> call, Response<List<SupplierModel>> response) {

                if (response.isSuccessful()) {
                    //update the adapter data
                    mAdapter.updateAdapterData(response.body());
                    mAdapter.notifyDataSetChanged();
                } else {
                    ErrorModel errorModel = ErrorUtils.parseError(response);
                    Toast.makeText(getBaseContext(), "Error type is " + errorModel.Message + " , description " + errorModel.MessageDetail, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<SupplierModel>> call, Throwable t) {
                //occur when fail to deserialize || no network connection || server unavailable
                Toast.makeText(getBaseContext(), "Fail it >> " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 7 && resultCode == -1) {
            getSupplierFromServer();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_default, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_search) {
            EditText txsearch = (EditText) toolbar.findViewById(R.id.Etsearch);
            // txsearch.setVisibility(View.VISIBLE);

        }

        if (id == R.id.action_add) {
            //start new activity to send
            Intent postNewIntent = new Intent(this, CreateorEditSupplier.class);
            postNewIntent.putExtra("ACTION_TO_DO_KEY", 3);
            startActivityForResult(postNewIntent,7);
        }
        return super.onOptionsItemSelected(item);
    }

}
