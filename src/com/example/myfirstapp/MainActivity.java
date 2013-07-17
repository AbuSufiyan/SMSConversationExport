package com.example.myfirstapp;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract.PhoneLookup;
import android.view.Menu;
import android.view.View;

public class MainActivity extends Activity
{
    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );
    }

    public void getSMS( View v )
    {

        Intent intentSelectionActivity = new Intent( this, SelectionActivity.class );

        Uri SMS_INBOX = Uri.parse( "content://mms-sms/conversations/" );
        Cursor c = getContentResolver().query( SMS_INBOX, null, null, null, "date desc" );
        ArrayList< String > conversationAdresses = new ArrayList< String >();

        if ( c.moveToNext() )
        {
            do
            {
                String address = c.getString( c.getColumnIndex( "address" ) );
                // conversationAdresses.add( c.getString( c.getColumnIndex( "address" ) ) );
                // conversationAdresses
                // .add( c.getString( c.getColumnIndex( PhoneLookup.DISPLAY_NAME ) ) );

                String contact = address;
                Uri uri =
                    Uri.withAppendedPath( PhoneLookup.CONTENT_FILTER_URI, Uri.encode( address ) );
                Cursor cs = getContentResolver().query( uri, new String[]
                {
                    PhoneLookup.DISPLAY_NAME
                }, PhoneLookup.NUMBER + "='" + address + "'", null, null );

                if ( cs.getCount() > 0 )
                {
                    cs.moveToFirst();
                    contact = cs.getString( cs.getColumnIndex( PhoneLookup.DISPLAY_NAME ) );
                }
                conversationAdresses.add( contact );

            }
            while ( c.moveToNext() );

        }

        intentSelectionActivity.putStringArrayListExtra( "conversationAddresses",
            conversationAdresses );
        startActivity( intentSelectionActivity );

        // String number = "MD-IRCTCt";
        // int smsReceivedCount = 0;
        // int smsSentCount = 0;
        // TextView tv = ( TextView ) findViewById( R.id.smslist );
        //
        // StringBuilder smsBuilder = new StringBuilder();
        // final String SMS_URI_SENT = "content://sms/sent";
        // final String SMS_URI_INBOX = "content://sms/inbox";
        // try
        // {
        // Map< Long, Message > conversationMap = new TreeMap< Long, Message >();
        //
        // String[] projection = new String[]
        // {
        // "_id", "address", "person", "body", "date", "type"
        // };
        //
        // Uri uriInbox = Uri.parse( SMS_URI_INBOX );
        // Cursor curReceived =
        // getContentResolver()
        // // .query( uriInbox, projection, "PHONE_NUMBERS_EQUAL(address, '" + number +
        // // "')",
        //
        // // To get the sms of Names without numbers eg: SBT, IRCTC
        // // .query( uriInbox, projection, "address ='" + number + "'",
        //
        // .query( uriInbox, projection, "address ='" + number + "'",
        // null,
        // "date asc" );
        //
        // Uri uri = Uri.parse( SMS_URI_SENT );
        // Cursor curSent =
        // getContentResolver()
        // .query( uri, projection, "PHONE_NUMBERS_EQUAL(address, '" + number + "')",
        // null,
        // "date asc" );
        //
        // boolean sent = false;
        // boolean received = false;
        // sent = curSent.moveToFirst();
        // received = curReceived.moveToFirst();
        //
        // if ( received && sent )
        // {
        //
        // // Conversation
        // int indexAddress = curReceived.getColumnIndex( "address" );
        // int indexPerson = curReceived.getColumnIndex( "person" );
        // int indexBody = curReceived.getColumnIndex( "body" );
        // int indexDate = curReceived.getColumnIndex( "date" );
        // int indexType = curReceived.getColumnIndex( "type" );
        //
        // do
        // {
        // smsReceivedCount++;
        // String strAddress = curReceived.getString( indexAddress );
        // int intPerson = curReceived.getInt( indexPerson );
        // String strbody = curReceived.getString( indexBody );
        // long longDate = curReceived.getLong( indexDate );
        // int intType = curReceived.getInt( indexType );
        //
        // Message sms = new Message();
        // sms.setAddress( strAddress );
        // sms.setBody( strbody );
        // sms.setDate( longDate );
        // sms.setType( intType );
        // sms.setPerson( intPerson );
        //
        // conversationMap.put( longDate, sms );
        // //
        // // smsBuilder.append( "[ " );
        // // // smsBuilder.append( strAddress + ", " );
        // // // smsBuilder.append( intPerson + ", " );
        // // smsBuilder.append( strbody + " " );
        // // // smsBuilder.append( longDate + ", " );
        // // // smsBuilder.append( int_Type );
        // // smsBuilder.append( " ]\n\n" );
        // }
        // while ( curReceived.moveToNext() );
        //
        // indexAddress = curSent.getColumnIndex( "address" );
        // indexPerson = curSent.getColumnIndex( "person" );
        // indexBody = curSent.getColumnIndex( "body" );
        // indexDate = curSent.getColumnIndex( "date" );
        // indexType = curSent.getColumnIndex( "type" );
        //
        // do
        // {
        // smsSentCount++;
        // String strAddress = curSent.getString( indexAddress );
        // int intPerson = curSent.getInt( indexPerson );
        // String strbody = curSent.getString( indexBody );
        // long longDate = curSent.getLong( indexDate );
        // int intType = curSent.getInt( indexType );
        //
        // Message sms = new Message();
        // sms.setAddress( strAddress );
        // sms.setBody( strbody );
        // sms.setDate( longDate );
        // sms.setType( intType );
        // sms.setPerson( intPerson );
        //
        // conversationMap.put( longDate, sms );
        // //
        // // smsBuilder.append( "[ " );
        // // // smsBuilder.append( strAddress + ", " );
        // // // smsBuilder.append( intPerson + ", " );
        // // smsBuilder.append( strbody + " " );
        // // // smsBuilder.append( longDate + ", " );
        // // // smsBuilder.append( int_Type );
        // // smsBuilder.append( " ]\n\n" );
        // }
        // while ( curSent.moveToNext() );
        //
        // }
        //
        // else if ( received )
        // {
        // // Only Received
        // int indexAddress = curReceived.getColumnIndex( "address" );
        // int indexPerson = curReceived.getColumnIndex( "person" );
        // int indexBody = curReceived.getColumnIndex( "body" );
        // int indexDate = curReceived.getColumnIndex( "date" );
        // int indexType = curReceived.getColumnIndex( "type" );
        //
        // do
        // {
        // smsReceivedCount++;
        // String strAddress = curReceived.getString( indexAddress );
        // int intPerson = curReceived.getInt( indexPerson );
        // String strbody = curReceived.getString( indexBody );
        // long longDate = curReceived.getLong( indexDate );
        // int intType = curReceived.getInt( indexType );
        //
        // Message sms = new Message();
        // sms.setAddress( strAddress );
        // sms.setBody( strbody );
        // sms.setDate( longDate );
        // sms.setType( intType );
        // sms.setPerson( intPerson );
        //
        // conversationMap.put( longDate, sms );
        // //
        // // smsBuilder.append( "[ " );
        // // // smsBuilder.append( strAddress + ", " );
        // // // smsBuilder.append( intPerson + ", " );
        // // smsBuilder.append( strbody + " " );
        // // // smsBuilder.append( longDate + ", " );
        // // // smsBuilder.append( int_Type );
        // // smsBuilder.append( " ]\n\n" );
        // }
        // while ( curReceived.moveToNext() );
        // }
        //
        // else if ( sent )
        // {
        // // Only Sent
        // int indexAddress = curSent.getColumnIndex( "address" );
        // int indexPerson = curSent.getColumnIndex( "person" );
        // int indexBody = curSent.getColumnIndex( "body" );
        // int indexDate = curSent.getColumnIndex( "date" );
        // int indexType = curSent.getColumnIndex( "type" );
        //
        // do
        // {
        // smsSentCount++;
        // String strAddress = curSent.getString( indexAddress );
        // int intPerson = curSent.getInt( indexPerson );
        // String strbody = curSent.getString( indexBody );
        // long longDate = curSent.getLong( indexDate );
        // int intType = curSent.getInt( indexType );
        //
        // Message sms = new Message();
        // sms.setAddress( strAddress );
        // sms.setBody( strbody );
        // sms.setDate( longDate );
        // sms.setType( intType );
        // sms.setPerson( intPerson );
        //
        // conversationMap.put( longDate, sms );
        // //
        // // smsBuilder.append( "[ " );
        // // // smsBuilder.append( strAddress + ", " );
        // // // smsBuilder.append( intPerson + ", " );
        // // smsBuilder.append( strbody + " " );
        // // // smsBuilder.append( longDate + ", " );
        // // // smsBuilder.append( int_Type );
        // // smsBuilder.append( " ]\n\n" );
        // }
        // while ( curSent.moveToNext() );
        // }
        // else
        // {
        // smsBuilder.append( "No SMS received from " + number );
        // Toast.makeText( MainActivity.this, smsBuilder.toString(), Toast.LENGTH_LONG )
        // .show();
        // } // end if
        //
        // if ( !curReceived.isClosed() )
        // {
        // curReceived.close();
        // curReceived = null;
        // }
        // if ( !curSent.isClosed() )
        // {
        // curSent.close();
        // curSent = null;
        // }
        //
        // for ( Long date : conversationMap.keySet() )
        // {
        // Message sms = new Message();
        // sms = conversationMap.get( date );
        // smsBuilder.append( sms.getBody() + "\n\n" );
        //
        // }
        // tv.setText( smsSentCount + " SMS Sent: \n\n" + smsReceivedCount + " SMS received: \n\n"
        // + smsBuilder );
        //
        // }
        // catch ( Exception ex )
        // {
        // Log.e( "SQLiteException", ex.getMessage(), ex );
        // }

    }

    @Override
    public boolean onCreateOptionsMenu( Menu menu )
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate( R.menu.main, menu );
        return true;
    }

}
