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

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{
    Context thisContext = this;

    ArrayList<Bitmap> bitmapArray;
    ArrayList<String> playerNames;
    ArrayList<String> playerEndings;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        new getData().execute();
    }

    @Override
    public void onBackPressed()
    {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        if(drawer.isDrawerOpen(GravityCompat.START))
        {
            drawer.closeDrawer(GravityCompat.START);
        }
        else
        {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item)
    {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.Players)
        {

        }
        else if (id == R.id.nav_gallery)
        {

        }
        else if (id == R.id.nav_slideshow)
        {

        }
        else if (id == R.id.nav_manage)
        {

        }
        else if (id == R.id.nav_share)
        {

        }
        else if (id == R.id.nav_send)
        {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private class getData extends AsyncTask<Void, Void, Void>
    {
        TextView text;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();

            bitmapArray = new ArrayList<Bitmap>();
            playerNames = new ArrayList<String>();
            playerEndings = new ArrayList<String>();

            text = findViewById( R.id.text1 );
            text.setText( "Loading..." );
        }

        @Override
        protected Void doInBackground(Void... voids)
        {
            try
            {
                //Connect to the website
                Document document = Jsoup.connect("https://www.baseball-reference.com/teams/KCR/2018.shtml").get();

                Element battingTable = document.getElementById( "all_team_batting" );
                Elements players = battingTable.getElementsByAttributeValue( "data-stat", "player" );

                for( Element player : players )
                {
                    Elements tester = player.getElementsByAttribute( "href" );

                    if( !tester.isEmpty() )
                    {
                        String ending = tester.first().attr("href");
                        Document playerPage = Jsoup.connect("https://www.baseball-reference.com" + ending).get();
                        Elements profilePics = playerPage.getElementsByAttributeValue("class", "media-item");

                        if( !profilePics.isEmpty() )
                        {
                            Element profilePic = profilePics.first();
                            profilePic = profilePic.child(0);
                            InputStream input = new java.net.URL(profilePic.attr("src")).openStream();

                            String[] playerName = player.attr( "csk" ).split( "," );
                            String formattedName = playerName[1] + " " + playerName[0];

                            bitmapArray.add(BitmapFactory.decodeStream(input));
                            playerNames.add( formattedName );
                            playerEndings.add( ending );
                        }
                    }
                }

            } catch (IOException e)
            {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            super.onPostExecute(aVoid);

            RecyclerView recyclerView = findViewById( R.id.recycler_view );
            RecyclerViewAdapter adapter = new RecyclerViewAdapter( thisContext, bitmapArray, playerNames, playerEndings );
            recyclerView.setAdapter( adapter );
            recyclerView.setLayoutManager(new LinearLayoutManager(thisContext));

            text.setText( "" );
        }
    }
}

