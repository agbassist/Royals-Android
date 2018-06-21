package com.example.agbas.royalsstats3;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class PlayerPage extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.player_stats );

        Intent intent = getIntent();
        String playerName = intent.getStringExtra( RecyclerViewAdapter.EXTRA_MESSAGE );

        TextView title = findViewById( R.id.player_title );
        title.setText( playerName );
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
    }
}
