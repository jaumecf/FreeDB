package com.example.freedb;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BSOAdapter extends RecyclerView.Adapter<BSOAdapter.PeliViewHolder>{

    private Context mContext;
    private final ArrayList<BSO> mBSO;
    private final OnItemClickListener listener;

    public BSOAdapter(Context mContext, ArrayList<BSO> mBSO, OnItemClickListener listener) {
        this.mContext = mContext;
        this.mBSO = mBSO;
        this.listener = listener;
    }

    @NonNull
    @Override
    public BSOAdapter.PeliViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull BSOAdapter.PeliViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public interface OnItemClickListener {
        void onItemClick(BSO bso);
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class PeliViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case

        @BindView(R.id.imatge)
        ImageView mImatge;
        @BindView(R.id.nom)
        TextView mNom;
        @BindView(R.id.comentari) TextView mComentari;
        @BindView(R.id.data) TextView mData;
        @BindView(R.id.nota) TextView mNota;
        @BindView(R.id.valoracio)
        RatingBar mValoracio;

        private Context mContext;

        public PeliViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
            mContext = itemView.getContext();
            itemView.setTag(this);
        }

        public void bindPelicula(final Pelicula pelicula, final PeliAdapter.OnItemClickListener listener) {

            mNom.setText(pelicula.getNom());
            mComentari.setText(pelicula.getComentari());
            mData.setText(pelicula.getData());
            mNota.setText("Rate: "+pelicula.getValoracio());
            mValoracio.setRating(pelicula.getValoracio());
            pinta_imatge(pelicula);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(pelicula);
                }
            });

        }

        public void pinta_imatge(Pelicula pelicula){
            if(pelicula.getFoto() == null){
                mImatge.setImageResource(R.drawable.no_image);
            }else{
                Bitmap bmp = BitmapFactory.decodeByteArray(pelicula.getFoto(),0,pelicula.getFoto().length);
                mImatge.setImageBitmap(Bitmap.createScaledBitmap(bmp, 100, 100, false));
            }
            mImatge.setAdjustViewBounds(true);
            mImatge.setScaleType(ImageView.ScaleType.FIT_CENTER);
        }

    }

}
