package com.nafaz.android.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.nafaz.android.R;
import com.nafaz.android.entity.AccountHistory;
import com.nafaz.android.ui.fragment.AccountHistoryFragment.OnListFragmentInteractionListener;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * {@link RecyclerView.Adapter} that can display a {@link AccountHistory} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class AccountHistoryAdapter extends RecyclerView.Adapter<AccountHistoryAdapter.ViewHolder> {

    private final List<AccountHistory> mValues;
    private final OnListFragmentInteractionListener mListener;

    public AccountHistoryAdapter(List<AccountHistory> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_account_history_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NotNull final ViewHolder holder, int position) {
//        holder.item = mValues.get(position);
//        holder.tvServiceProvider.setText(mValues.get(position).id);
//        holder.tvDate.setText(mValues.get(position).content);

        holder.mView.setOnClickListener(v -> {
            if (null != mListener) {
                // Notify the active callbacks interface (the activity, if the
                // fragment is attached to one) that an item has been selected.
                mListener.onListFragmentInteraction(holder.item);
            }
        });
    }

    // TODO: 2019-06-26 return list size
    @Override
    public int getItemCount() {
        return /*mValues.size()*/25;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;

        @BindView(R.id.tv_service_provider)
        TextView tvServiceProvider;

        @BindView(R.id.tv_date)
        TextView tvDate;

        public AccountHistory item;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            ButterKnife.bind(this, view);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + tvDate.getText() + "'";
        }
    }
}
