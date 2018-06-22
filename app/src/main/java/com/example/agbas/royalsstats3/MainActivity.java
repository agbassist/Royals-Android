package com.example.agbas.royalsstats3;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
{
    Context thisContext = this;

    private ArrayList<Bitmap> bitmapArray = new ArrayList<>();
    private ArrayList<String> playerNames = new ArrayList<>();
    private ArrayList<String> playerEndings = new ArrayList<>();

    private static final String teamPage = "/teams/KCR/2018.shtml";

    private TextView text;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        text = findViewById( R.id.text1 );

        new getData().execute();
    }

    private class getData extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            text.setText("Loading...");
        }

        @Override
        protected Void doInBackground(Void... voids)
        {
            try
            {
                //Connect to the website
                Document document = Jsoup.connect( getString( R.string.url_start ) + teamPage ).get();
                Elements elements = document.select( "table#team_batting [data-stat=player]" );

                for( Element playerInfo : elements )
                {
                    String tempName = playerInfo.attr( "csk" );

                    if( !tempName.isEmpty() )
                    {
                        String[] playerName = tempName.split( "," );
                        String formattedName = playerName[1] + " " + playerName[0];
                        playerNames.add( formattedName );

                        String tempEnding = playerInfo.select("a[href]").attr("href" );
                        playerEndings.add( tempEnding );

                        Document playerPage = Jsoup.connect( getString( R.string.url_start ) + tempEnding ).get();
                        String imageUrl = playerPage.select( "div.media-item img" ).attr( "src" );
                        InputStream input = new java.net.URL( imageUrl ).openStream();
                        bitmapArray.add( BitmapFactory.decodeStream( input ) );
                    }
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

            recyclerView = findViewById( R.id.recycler_view );
            RecyclerViewAdapter adapter = new RecyclerViewAdapter( thisContext, bitmapArray, playerNames, playerEndings );
            recyclerView.setAdapter( adapter );
            recyclerView.setLayoutManager(new LinearLayoutManager(thisContext));

            text.setText( "" );
        }
    }
}

