package com.example.agbas.royalsstats3;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.PlayerTile>
{
    private static final String TAG = "RecyclerViewAdapter";

    private ArrayList<Bitmap> mBitmapArray = new ArrayList<>();
    private ArrayList<String> mPlayerNames = new ArrayList<>();
    private ArrayList<String> mPlayerEndings = new ArrayList<>();

    private Context mContext;

    public RecyclerViewAdapter( Context context, ArrayList<Bitmap> graphics, ArrayList<String> playerNames, ArrayList<String> playerEndings )
    {
        mContext = context;
        mBitmapArray = graphics;
        mPlayerNames = playerNames;
        mPlayerEndings = playerEndings;
    }

    @NonNull
    @Override
    public PlayerTile onCreateViewHolder( @NonNull ViewGroup viewGroup, int i )
    {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.player_info, viewGroup, false);
        return new PlayerTile(view);
    }

    @Override
    public void onBindViewHolder( @NonNull final PlayerTile playerTile, final int i )
    {
        playerTile.textView.setText( mPlayerNames.get(i) );
        playerTile.imageView.setImageBitmap( mBitmapArray.get(i) );

        playerTile.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent( mContext, PlayerPage.class );
                intent.putExtra( mContext.getString( R.string.name_message ), mPlayerNames.get(i) );
                intent.putExtra( mContext.getString( R.string.url_message ), mPlayerEndings.get(i) );
                mContext.startActivity( intent );
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return mPlayerNames.size();
    }

    public class PlayerTile extends RecyclerView.ViewHolder
    {
        ImageView imageView;
        TextView textView;
        LinearLayout parentLayout;

        public PlayerTile( @NonNull View itemView )
        {
            super( itemView );
            imageView = itemView.findViewById( R.id.profile_pic );
            textView = itemView.findViewById( R.id.player_name );
            parentLayout = itemView.findViewById( R.id.parent_layout );
        }
    }
}
