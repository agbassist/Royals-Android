package com.example.agbas.royalsstats3;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class PlayerPage extends AppCompatActivity
{
    TableLayout table;
    String playerUrl;
    String playerName;
    Context thisContext;
    Elements stats;

    ArrayList<String> dataTypes = new ArrayList<>();
    ArrayList<String> dataValues = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.player_stats );

        Intent intent = getIntent();
        playerName = intent.getStringExtra( getString( R.string.name_message ) );
        playerUrl = intent.getStringExtra( getString( R.string.url_message ) );

        TextView title = findViewById( R.id.player_title );
        title.setText( playerName );

        table = findViewById( R.id.stats_table );
        thisContext = this;

        new getData().execute();
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
    }

    private class getData extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids)
        {
            try
            {
                Document playerPage = Jsoup.connect( getString( R.string.url_start ) + playerUrl ).get();
                Elements stats = playerPage.select( "table#batting_standard tr[id=batting_standard.2018] > *.right" );

                for( Element stat : stats )
                {
                    // System.out.print( stat.attr("data-stat") + " : ");
                    // System.out.println( stat.text() );
                    dataTypes.add( stat.attr("data-stat") );
                    dataValues.add( stat.text() );
                }
            }
            catch( IOException e )
            {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            super.onPostExecute(aVoid);

            for( int i = 0; i < dataTypes.size(); i++ )
            {
                TableRow row = new TableRow( thisContext );

                TextView statName = new TextView( thisContext );
                statName.setText( dataTypes.get( i ) );

                TextView value = new TextView( thisContext );
                value.setText( dataValues.get( i ) );

                row.addView( statName );
                row.addView( value );
                table.addView( row );
            }
        }
    }
}
