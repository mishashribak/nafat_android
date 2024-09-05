package com.nafaz.android.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.nafaz.android.R;
import com.nafaz.android.entity.AuthenticationMethod;
import com.nafaz.android.entity.TagId;
import com.nafaz.android.entity.listener.OnItemClickListener;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import lombok.Setter;

public class ManageAccountAdapter extends RecyclerView.Adapter<ManageAccountAdapter.ViewHolder> {

    private final List<AuthenticationMethod> authenticationMethods;

    @Setter
    private OnItemClickListener onItemDeleteListener;
    @Setter
    private OnItemClickListener onItemEditListener;
    @Setter
    private OnItemClickListener onItemVerifiedListener;

    public ManageAccountAdapter(List<AuthenticationMethod> items) {
        authenticationMethods = items;
    }

    @NotNull
    @Override
    public ManageAccountAdapter.ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_manage_account_item, parent, false);
        return new ManageAccountAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NotNull final ManageAccountAdapter.ViewHolder holder, int position) {
        AuthenticationMethod authenticationMethod = authenticationMethods.get(position);

        holder.tvManageAccItem.setText(authenticationMethod.name);

        holder.imgDelete.setVisibility(authenticationMethod.isActive() ? View.VISIBLE : View.GONE);

        holder.imgDelete.setTag(TagId.POSITION, position);
        holder.imgEdit.setTag(TagId.POSITION, position);
        holder.imgVerified.setTag(TagId.POSITION, position);
    }

    @Override
    public int getItemCount() {
        return authenticationMethods.size();
    }

    public void addAll(List<AuthenticationMethod> methodList) {
        authenticationMethods.clear();
        authenticationMethods.addAll(methodList);
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_manage_acc_item)
        TextView tvManageAccItem;

        @BindView(R.id.img_manage_acc_delete)
        ImageView imgDelete;

        @BindView(R.id.img_manage_acc_edit)
        ImageView imgEdit;

        @BindView(R.id.img_manage_acc_verified)
        ImageView imgVerified;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);

            if (null != onItemDeleteListener)
                imgDelete.setOnClickListener(v -> onItemDeleteListener.onItemClick(v));

            if (null != onItemEditListener)
                imgEdit.setOnClickListener(v -> onItemEditListener.onItemClick(v));

            if (null != onItemVerifiedListener)
                imgVerified.setOnClickListener(v -> onItemVerifiedListener.onItemClick(v));
        }
    }
}