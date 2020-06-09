package com.example.b10309005hw2;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;

import com.google.android.material.shape.CornerFamily;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.shape.MaterialShapeDrawable;
import com.google.android.material.shape.ShapeAppearanceModel;

public class MainActivity extends AppCompatActivity {
    SharedPreferences pref;
    DataAdapter dataAdapter;
    DataHelper db;
    RecyclerView recyclerView;
    Guest lastSwipedGuest;

    void updateBackground()
    {
        ShapeAppearanceModel shapeAppearanceModel = new ShapeAppearanceModel()
                .toBuilder()
                .setAllCorners(CornerFamily.ROUNDED, 99999)
                .build();

        MaterialShapeDrawable bg = new MaterialShapeDrawable(shapeAppearanceModel);
        bg.setFillColor(ColorStateList.valueOf(Color.parseColor(pref.getString("color", "#dddddd"))));
        dataAdapter.bg = bg;
        dataAdapter.cl = Color.parseColor(pref.getString("color", "#dddddd"));
        dataAdapter.notifyDataSetChanged();
    }

    void alertToDelete(Guest guest)
    {
        lastSwipedGuest = guest;
        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        adb.setTitle("Are you sure you want to remove " + guest.name + "?");
        adb.setIcon(android.R.drawable.ic_dialog_alert);
        adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                db.delGuest(lastSwipedGuest);
                int pos = dataAdapter.guests.indexOf(lastSwipedGuest);
                dataAdapter.guests.remove(lastSwipedGuest);
                dataAdapter.notifyItemRemoved(pos);
            }
        });
        adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dataAdapter.notifyDataSetChanged();
            }
        });
        adb.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DataHelper(getApplicationContext());
        recyclerView = (RecyclerView)findViewById(R.id.listview);

        dataAdapter = new DataAdapter();
        dataAdapter.guests = db.getGuests();
        recyclerView.setAdapter(dataAdapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(
                /*ItemTouchHelper.UP | ItemTouchHelper.DOWN*/ 0,
                ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                //  上下拖移callback
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                alertToDelete(((DataAdapter.GuestViewHolder)viewHolder).guest);
            }
        }).attachToRecyclerView(recyclerView);

        setSupportActionBar((Toolbar)findViewById(R.id.toolbar));

        this.pref = PreferenceManager.getDefaultSharedPreferences(this);

        updateBackground();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        pref.registerOnSharedPreferenceChangeListener(new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                if(key.equals("color")) {
                    updateBackground();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.action_settings:
                startActivityForResult(new Intent(MainActivity.this, SettingsActivity.class), 0);
                return true;
            case R.id.action_add:
                startActivityForResult(new Intent(MainActivity.this, AddActivity.class), 0);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        dataAdapter.guests = db.getGuests();
        dataAdapter.notifyDataSetChanged();
        updateBackground();
    }
}