package com.example.myfirstapp;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class SelectionActivity extends Activity
{

    ArrayList< String > list = new ArrayList< String >();

    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_selection );
        ArrayList< String > countList = new ArrayList< String >();
        ArrayList< String > displayList = new ArrayList< String >();

        Bundle extras = getIntent().getExtras();
        if ( extras != null )
        {
            list = extras.getStringArrayList( "conversationAddresses" );
            countList = extras.getStringArrayList( "conversationCount" );

        }
        displayList = merge( list, countList );

        final ListView conversationList = ( ListView ) findViewById( R.id.listView1 );

        conversationList.setOnItemClickListener( new OnItemClickListener()
        {

            @Override
            public void onItemClick( AdapterView< ? > arg0, View arg1, int selection, long arg3 )
            {
                Log.d( "SELECTED", ( String ) list.get( selection ) );

                Intent returnIntent = new Intent( SelectionActivity.this, MainActivity.class );
                returnIntent.putExtra( "result", list.get( selection ) );
                setResult( RESULT_OK, returnIntent );
                finish();
            }
        } );

        final ArrayAdapter< String > adapter = new ArrayAdapter< String >( this,
            android.R.layout.simple_list_item_1, displayList );
        conversationList.setAdapter( adapter );

    }

    private ArrayList< String > merge( ArrayList< String > list, ArrayList< String > countList )
    {
        ArrayList< String > mergeList = new ArrayList< String >();
        if ( list.size() == countList.size() )
        {
            for ( int i = 0; i < list.size(); i++ )
            {
                mergeList.add( list.get( i ) + " (" + countList.get( i ) + ")" );

            }
        }
        else
        {
            return list;
        }
        return mergeList;
    }

    @Override
    public boolean onCreateOptionsMenu( Menu menu )
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate( R.menu.selection, menu );
        return true;
    }

}
