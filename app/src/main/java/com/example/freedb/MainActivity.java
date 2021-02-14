package com.example.freedb;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.os.Bundle;

import com.example.freedb.BBDD.InterficieBBDD;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener{

    private RecyclerView recyclerView;
    private PeliAdapter adapter;
    private Paint p = new Paint();
    private RecyclerView.LayoutManager layoutManager;
    private InterficieBBDD bd;
    private int ADD_CODE = 1;
    private int DETAIL_CODE = 2;
    private int RESULT_OK = 1;

    private View.OnClickListener onItemClickListener;

    private ArrayList<Pelicula> llistaPelicules;
    private ArrayList<Long> id_peli_eliminar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Inicialització variables
        id_peli_eliminar = new ArrayList<>();

        //Declaració de ToolBar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar que mola molt
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                startActivityForResult(new Intent(view.getContext(), AfegeixPelicula.class),ADD_CODE);
            }
        });

        llistaPelis();
    }

    @Override
    public void onStop(){
        bd = new InterficieBBDD(this.getApplicationContext());
        bd.obre();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            id_peli_eliminar.forEach((id) -> bd.esborraPelicula(id));
        }
        bd.tanca();
        super.onStop();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        llistaPelis();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        getMenuInflater().inflate(R.menu.menu_cerca, menu);

        MenuItem searchItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.afegeixPelicula) {
            startActivityForResult(new Intent(getApplicationContext(), AfegeixPelicula.class),ADD_CODE);
            return true;
        }else if(id == R.id.afegeixBSO){
            startActivityForResult(new Intent(getApplicationContext(), AfegeixBSO.class),ADD_CODE);
            return true;
        }else if(id == R.id.mostraBSO){

        }else if(id == R.id.ajuda){

        }

        return super.onOptionsItemSelected(item);
    }

    public void llistaPelis() {
        // Obrim la base de dades
        bd = new InterficieBBDD(this);
        bd.obre();
        // Obtenim tots els vins
        llistaPelicules = bd.getAllPelicula();
        bd.tanca();

        actualitzaRecycler();


    }

    private void actualitzaRecycler() {
        // specify an adapter (see also next example)
        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        //layoutManager = new LinearLayoutManager(this);
        //recyclerView.setLayoutManager(layoutManager);

        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));



        adapter = new PeliAdapter(getApplicationContext(), llistaPelicules, new PeliAdapter.OnItemClickListener() {
            @Override public void onItemClick(Pelicula Peli) {
                //Toast.makeText(getApplicationContext(), "Item Clicked"+Peli.getNom(), Toast.LENGTH_LONG).show();
                //Quan clicam damunt un element de la llista
                Intent intent = new Intent(getApplicationContext(),DetallPelicula.class);
                intent.putExtra("idPelicula",Peli.getId());
                startActivityForResult(intent,DETAIL_CODE);
            }
        });

        recyclerView.setAdapter(adapter);
        enableSwipe();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        // User pressed the search button
        if (query.isEmpty()) {
            llistaPelis();
            return false;
        }
        InterficieBBDD bd;
        bd = new InterficieBBDD(this);
        bd.obre();
        // Obtenim tots els vins
        llistaPelicules = bd.consultaPelis(query);
        bd.tanca();
        actualitzaRecycler();
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        // User changed the text
        if (newText.isEmpty()) {
            llistaPelis();
            return false;
        }
        InterficieBBDD bd;
        bd = new InterficieBBDD(this);
        bd.obre();
        // Obtenim tots els vins
        llistaPelicules = bd.consultaPelis(newText);
        bd.tanca();
        actualitzaRecycler();
        return false;
    }

    private void enableSwipe(){
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();

                if (direction == ItemTouchHelper.LEFT){
                    final Pelicula peliEsborrada = llistaPelicules.get(position);
                    final int deletedPosition = position;
                    //guardam l'id de la pel·licula
                    id_peli_eliminar.add(peliEsborrada.getId());
                    adapter.eliminaPeli(position);


                    // showing snack bar with Undo option
                    Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), " Pel·lícula eliminada!", Snackbar.LENGTH_LONG);
                    snackbar.setAction("Desfés", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // undo is selected, restore the deleted item
                            adapter.retornaPeli(peliEsborrada, deletedPosition);
                            id_peli_eliminar.remove(id_peli_eliminar.size()-1);
                        }
                    });
                    snackbar.setActionTextColor(Color.YELLOW);
                    snackbar.show();
                }
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

                Bitmap icon;
                if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE){

                    View itemView = viewHolder.itemView;
                    float height = (float) itemView.getBottom() - (float) itemView.getTop();
                    float width = height / 3;
                        p.setColor(Color.parseColor("#D32F2F"));
                        RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(),(float) itemView.getRight(), (float) itemView.getBottom());
                        c.drawRect(background,p);
                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.elimina);
                        RectF icon_dest = new RectF((float) itemView.getRight() - 2*width ,(float) itemView.getTop() + width,(float) itemView.getRight() - width,(float)itemView.getBottom() - width);
                        c.drawBitmap(icon,null,icon_dest,p);
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }
}

