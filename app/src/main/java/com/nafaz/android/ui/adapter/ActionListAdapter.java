package com.nafaz.android.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.nafaz.android.R;
import com.nafaz.android.ui.view.button.LoadingMaterialButton;
import org.jetbrains.annotations.NotNull;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ActionListAdapter extends RecyclerView.Adapter<ActionListAdapter.ViewHolder> {

    private final OnItemClickListener mListener;
    private List<String> listData;
    private Context mContext;

    public ActionListAdapter(Context context, List<String> items, OnItemClickListener listener) {
        listData = items;
        mListener = listener;
        mContext = context;
    }

    @NotNull
    @Override
    public ActionListAdapter.ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_action, parent, false);
        return new ActionListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NotNull final ActionListAdapter.ViewHolder holder, int position) {
        holder.btAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onItemClick(listData.get(position), position);
            }
        });

        String str;
        if(listData.get(position).equals("RESET_PASSWORD")){
            str = mContext.getResources().getString(R.string.reset_password);
        }else if(listData.get(position).equals("RESET_MOBILE")){
            str = mContext.getResources().getString(R.string.reset_mobile);
        }else if(listData.get(position).equals("REENROLL_TOTP")){
            str = mContext.getResources().getString(R.string.reenroll_totp);
        }else{
            str = mContext.getResources().getString(R.string.enroll_totp);
        }

        holder.btAction.setText(str);
    }

    @Override
    public int getItemCount() {
        if(listData == null)
            return 0;
        return listData.size();
    }

    public interface OnItemClickListener{
        void onItemClick(String text, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.btnAction)
        LoadingMaterialButton btAction;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}

