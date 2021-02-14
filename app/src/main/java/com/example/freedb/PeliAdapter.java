package com.example.freedb;

import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ColorSpace;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;



public class PeliAdapter extends RecyclerView.Adapter<PeliAdapter.PeliViewHolder> {

    private final ArrayList<Pelicula> mPelicules;

    //Arraylist d'objectes a declarar
    private Context mContext;

    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Pelicula peli);
    }

    // Adapter per a les pelis
    public PeliAdapter(Context context, ArrayList<Pelicula> myDataset, OnItemClickListener listener) {
        mPelicules = myDataset;
        mContext = context;
        this.listener = listener;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public PeliAdapter.PeliViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_peli_adapter, parent, false);
        PeliViewHolder vh = new PeliViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(PeliViewHolder holder, int position) {
        holder.bindPelicula(mPelicules.get(position),listener);
    }

    @Override
    public int getItemCount() {
        return mPelicules.size();
    }

    public void eliminaPeli(int position) {
        mPelicules.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mPelicules.size());
    }

    public void retornaPeli(Pelicula peli, int position) {
        mPelicules.add(position, peli);
        // notify item added by position
        notifyItemInserted(position);
    }


    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class PeliViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case

        @BindView(R.id.imatge) ImageView mImatge;
        @BindView(R.id.nom) TextView mNom;
        @BindView(R.id.comentari) TextView mComentari;
        @BindView(R.id.data) TextView mData;
        @BindView(R.id.nota) TextView mNota;
        @BindView(R.id.valoracio) RatingBar mValoracio;

        private Context mContext;

        public PeliViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
            mContext = itemView.getContext();
            itemView.setTag(this);
        }

        public void bindPelicula(final Pelicula pelicula, final OnItemClickListener listener) {

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
