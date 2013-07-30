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
import android.widget.ListView;

public class SelectionActivity extends Activity
{

    ArrayList< SmsInfoUnit > smsUnitList = new ArrayList< SmsInfoUnit >();

    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_selection );

        Bundle extras = getIntent().getExtras();
        if ( extras != null )
        {

            smsUnitList = ( ArrayList< SmsInfoUnit > ) extras.get( "conversationInfoUnits" );

        }

        final ListView conversationList = ( ListView ) findViewById( R.id.listView1 );

        conversationList.setOnItemClickListener( new OnItemClickListener()
        {

            @Override
            public void onItemClick( AdapterView< ? > arg0, View arg1, int selection, long arg3 )
            {
                Log.d( "SELECTED", smsUnitList.get( selection ).getThreadId() + "" );

                Intent returnIntent = new Intent( SelectionActivity.this, MainActivity.class );
                returnIntent.putExtra( "result", smsUnitList.get( selection ).getThreadId() );
                setResult( RESULT_OK, returnIntent );
                finish();
            }
        } );

        ConversationListAdapater adapter =
            new ConversationListAdapater( this, R.layout.all_conversation_list_item, smsUnitList );
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
