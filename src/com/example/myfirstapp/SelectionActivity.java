package com.example.myfirstapp;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class SelectionActivity extends Activity
{

    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_selection );
        ArrayList< String > list = new ArrayList< String >();

        Bundle extras = getIntent().getExtras();
        if ( extras != null )
        {
            list = extras.getStringArrayList( "conversationAddresses" );
        }

        ListView conversationList = ( ListView ) findViewById( R.id.listView1 );

        final ArrayAdapter< String > adapter = new ArrayAdapter< String >( this,
            android.R.layout.simple_list_item_1, list );
        conversationList.setAdapter( adapter );

    }

    @Override
    public boolean onCreateOptionsMenu( Menu menu )
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate( R.menu.selection, menu );
        return true;
    }

}
