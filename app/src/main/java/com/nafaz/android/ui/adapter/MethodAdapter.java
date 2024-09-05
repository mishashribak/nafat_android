package com.nafaz.android.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.nafaz.android.R;
import com.nafaz.android.app.NafazConfig;
import com.nafaz.android.entity.VerifyMethodModel;
import com.nafaz.android.ui.view.button.LoadingMaterialButton;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MethodAdapter extends RecyclerView.Adapter<MethodAdapter.ViewHolder> {

    private final OnItemClickListener mListener;
    private List<VerifyMethodModel> listData;
    private Context mContext;

    public MethodAdapter(Context context,  OnItemClickListener listener) {
        mListener = listener;
        mContext = context;
    }

    @NotNull
    @Override
    public MethodAdapter.ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_method, parent, false);
        return new MethodAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NotNull final MethodAdapter.ViewHolder holder, int position) {
        VerifyMethodModel verifyMethodModel = listData.get(position);
        holder.btnMethod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onItemClick(verifyMethodModel);
            }
        });

//        if(verifyMethodModel.stepName.equals("OTP")){
//            holder.btnMethod.setIcon(mContext.getResources().getDrawable(R.drawable.img_mobile_verification_green));
//        } else  if(verifyMethodModel.stepName.equals("CHECK_FACE")){
//            holder.btnMethod.setIcon(mContext.getResources().getDrawable(R.drawable.img_face_fingerprint_green));
//        }else if(verifyMethodModel.stepName.equals("UP")){
//            holder.btnMethod.setIcon(mContext.getResources().getDrawable(R.drawable.img_face_fingerprint_green));
//        }else if(verifyMethodModel.stepName.equals("CHECK_TOTP")) {
//            holder.btnMethod.setIcon(mContext.getResources().getDrawable(R.drawable.img_mobile_verification_green));
//        }

        if(NafazConfig.isEnglish()){
            holder.btnMethod.setText(verifyMethodModel.englishLabel);
        }else{
            holder.btnMethod.setText(verifyMethodModel.arabicLabel);
        }
    }

    @Override
    public int getItemCount() {
        if(listData == null)
            return 0;
        return listData.size();
    }

    public void setData(List<VerifyMethodModel> verifyMethodModelList){
        listData = verifyMethodModelList;
        notifyDataSetChanged();
    }

    public interface OnItemClickListener{
        void onItemClick(VerifyMethodModel verifyMethodModel);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.btMethod)
        LoadingMaterialButton btnMethod;

//        @BindView(R.id.tvMethod)
//        TextView tXtMethod;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}

