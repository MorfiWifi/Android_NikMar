package com.atahani.retrofit_sample.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.atahani.retrofit_sample.R;
import com.atahani.retrofit_sample.models.CustomerModel;
import com.atahani.retrofit_sample.utility.AppPreferenceTools;

import java.util.Collections;
import java.util.List;

/**
 * Created by m.hosein on 11/11/2017.
 */

public class DataAdapterCustomer extends RecyclerView.Adapter<DataAdapterCustomer.DataViewHolder> {
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private List<CustomerModel> mData = Collections.emptyList();
    private DataEventHandler mDataEventHandler;
    private AppPreferenceTools mAppPreferenceTools;

    public DataAdapterCustomer(Context context, DataEventHandler dataEventHandler) {
        this.mContext = context;
        this.mLayoutInflater = LayoutInflater.from(context);
        this.mDataEventHandler = dataEventHandler;
        mAppPreferenceTools = new AppPreferenceTools(this.mContext);
    }

    public void updateAdapterData(List<CustomerModel> data) {
        this.mData = data;
    }


    @Override
    public DataViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.data_row, parent, false);
        return new DataViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DataViewHolder holder, int position) {
        CustomerModel currentModel = mData.get(position);
        holder.mTxBody.setText(currentModel.Name);




    }


    @Override
    public int getItemCount() {
        return mData.size();
    }

    /**
     */
    public class DataViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout mLymain;
        private AppCompatTextView mTxBody;
        private AppCompatImageButton mImEdit;
        private AppCompatImageButton mImDelete;

        public DataViewHolder(View itemView) {
            super(itemView);
            mLymain = (LinearLayout) itemView.findViewById(R.id.ly_main);
            mTxBody = (AppCompatTextView) itemView.findViewById(R.id.tx_body);
            mImEdit = (AppCompatImageButton) itemView.findViewById(R.id.im_edit);
            mImDelete = (AppCompatImageButton) itemView.findViewById(R.id.im_delete);
            mImDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mDataEventHandler != null) {
                        mDataEventHandler.onDeleteData(String.valueOf(mData.get(getAdapterPosition()).Id), getAdapterPosition());
                    }
                }
            });

            mImEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mDataEventHandler != null) {
                        mDataEventHandler.onEditData(String.valueOf(mData.get(getAdapterPosition()).Id), getAdapterPosition());
                    }
                }
            });

            mLymain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mDataEventHandler != null) {
                        mDataEventHandler.onDetailData(String.valueOf(mData.get(getAdapterPosition()).Id), getAdapterPosition());
                    }
                }
            });
        }

    }



    /**
     * define interface to handle events
     */
    public interface DataEventHandler {
        void onEditData(String Id, int position);

        void onDeleteData(String Id, int position);
        void onDetailData(String Id, int position);
    }
}
