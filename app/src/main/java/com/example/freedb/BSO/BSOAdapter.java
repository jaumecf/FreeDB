package com.example.freedb.BSO;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.freedb.Pelicula.PeliAdapter;
import com.example.freedb.Pelicula.Pelicula;
import com.example.freedb.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BSOAdapter extends RecyclerView.Adapter<BSOAdapter.BSOViewHolder>{

    private Context mContext;
    private final ArrayList<BSO> mBSO;
    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(BSO bso);
    }

    public BSOAdapter(Context mContext, ArrayList<BSO> mBSO, OnItemClickListener listener) {
        this.mContext = mContext;
        this.mBSO = mBSO;
        this.listener = listener;
    }

    @NonNull
    @Override
    public BSOAdapter.BSOViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_b_s_o_adapter, parent, false);
        BSOAdapter.BSOViewHolder vh = new BSOAdapter.BSOViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull BSOAdapter.BSOViewHolder holder, int position) {
        holder.bindBSO(mBSO.get(position),listener);
    }

    @Override
    public int getItemCount() {
        return mBSO.size();
    }

    public void eliminaBSO(int position) {
        mBSO.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mBSO.size());
    }

    public void retornaPeli(BSO bso, int position) {
        mBSO.add(position, bso);
        // notify item added by position
        notifyItemInserted(position);
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class BSOViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case


        @BindView(R.id.titol) TextView mTitol;
        @BindView(R.id.autor) TextView mAutor;
        @BindView(R.id.duracio) TextView mDuracio;
        @BindView(R.id.data) TextView mData;
        @BindView(R.id.link) TextView mLink;

        private Context mContext;

        public BSOViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
            mContext = itemView.getContext();
            itemView.setTag(this);
        }

        public void bindBSO(final BSO bso, final BSOAdapter.OnItemClickListener listener) {

            mTitol.setText(bso.getTitol());
            mAutor.setText(bso.getAutor());
            mDuracio.setText(bso.getDuracio());
            mData.setText(bso.getData());
            mLink.setText(bso.getLink());


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(bso);
                }
            });

        }
    }

}
