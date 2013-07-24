package com.example.myfirstapp;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.provider.ContactsContract.PhoneLookup;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity
{
    private int fileSize = 0;
    private int MAX_SIZE = 0;
    private ProgressDialog progressBar;
    private int progressBarStatus;
    private Button btnStartProgress;
    private final Handler progressBarHandler = new Handler();
    ArrayList< String > conversationAdresses = new ArrayList< String >();
    ArrayList< String > conversationCount = new ArrayList< String >();

    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );
        btnStartProgress = ( Button ) findViewById( R.id.button1 );
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        btnStartProgress.setClickable( true );
        addListenerOnButton();
    }

    private void addListenerOnButton()
    {

        btnStartProgress.setOnClickListener( new OnClickListener()
        {
            @Override
            public void onClick( View v )
            {
                btnStartProgress.setClickable( false );

                if ( conversationAdresses == null || conversationAdresses.isEmpty()
                    || conversationCount == null || conversationCount.isEmpty() )
                {
                    progressBar = new ProgressDialog( v.getContext() );
                    progressBar.setCancelable( true );
                    progressBar.setMessage( "Fetching Data" );
                    progressBar.setProgressStyle( ProgressDialog.STYLE_HORIZONTAL );
                    progressBar.setProgress( 0 );
                    progressBar.show();

                    new Thread( new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            Uri SMS_INBOX = Uri.parse( "content://mms-sms/conversations/" );
                            Cursor c =
                                getContentResolver().query( SMS_INBOX, new String[]
                                {
                                    "address", "thread_id"
                                }, null, null, "date desc limit 100" );
                            MAX_SIZE = c.getCount();
                            progressBar.setMax( MAX_SIZE );
                            if ( c.moveToFirst() )
                            {
                                do
                                {
                                    String count = "" + 0;
                                    count =
                                        getSMSCount( c.getInt( c.getColumnIndex( "thread_id" ) ) );
                                    progressBarStatus =
                                        doSomeTask( c.getString( c.getColumnIndex( "address" ) ),
                                            count );
                                    // progressBar.setProgress( progressBarStatus );

                                    try
                                    {
                                        Thread.sleep( 10 );
                                    }
                                    catch ( InterruptedException e )
                                    {
                                        e.printStackTrace();
                                    }

                                    progressBarHandler.post( new Runnable()
                                    {
                                        @Override
                                        public void run()
                                        {
                                            progressBar.setProgress( progressBarStatus );

                                        }
                                    } );
                                }
                                while ( c.moveToNext() );
                            }
                            if ( progressBarStatus >= MAX_SIZE )
                            {
                                progressBar.dismiss();

                                Intent intentSelectionActivity =
                                    new Intent( MainActivity.this, SelectionActivity.class );

                                intentSelectionActivity.putStringArrayListExtra(
                                    "conversationAddresses",
                                    conversationAdresses );
                                intentSelectionActivity.putStringArrayListExtra(
                                    "conversationCount",
                                    conversationCount );

                                startActivityForResult( intentSelectionActivity, 1 );

                            }
                        }
                    } ).start();
                }
                else
                {
                    progressBar.dismiss();

                    Intent intentSelectionActivity =
                        new Intent( MainActivity.this, SelectionActivity.class );

                    intentSelectionActivity.putStringArrayListExtra(
                        "conversationAddresses",
                        conversationAdresses );
                    intentSelectionActivity.putStringArrayListExtra(
                        "conversationCount",
                        conversationCount );

                    startActivityForResult( intentSelectionActivity, 1 );
                }
            }
        } );
    }

    public String getSMSCount( int number )
    {
        Uri uri = Uri.parse( "content://sms" );
        Cursor smsCountCursor =
            getContentResolver().query( uri, new String[]
            {
                "date", "body"
            }, "thread_id=" + number,
                null, "date asc" );

        int smsCount = smsCountCursor.getCount();

        // Log.d( "SMS count", smsCount + "" );

        /*
         * if ( smsCountCursor.moveToFirst() ) { do { String str = smsCountCursor.getString(
         * smsCountCursor.getColumnIndex( "date" ) ); Long ll = Long.parseLong( str ); String fg =
         * new SimpleDateFormat( "dd/MM/yyyy HH:mm:ss" ).format( new Date( ll ) ); Log.d( "" +
         * number, fg ); Log.d( "" + number + "->BODY", smsCountCursor.getString(
         * smsCountCursor.getColumnIndex( "body" ) ) ); } while ( smsCountCursor.moveToNext() ); }
         */

        return smsCount + "";
    }

    public int doSomeTask( String adress, String number )
    {
        // Log.d( "doSomteTask()", "" + fileSize + "max size" + MAX_SIZE );
        fileSize++;
        String address = adress;
        // c.getString( c.getColumnIndex( "address" ) );

        // Log.d( "firstC ", address );
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
        conversationCount.add( number );

        return fileSize;

    }

    public void getSMS( String number, String name )
    {
        fileSize = 0;

        // Intent intentSelectionActivity = new Intent( this, SelectionActivity.class );
        // ArrayList< String > conversationAdresses = new ArrayList< String >();
        //
        // conversationAdresses = getallConversations();
        //
        // intentSelectionActivity.putStringArrayListExtra( "conversationAddresses",
        // conversationAdresses );
        // startActivityForResult( intentSelectionActivity, 1 );

        int smsReceivedCount = 0;
        int smsSentCount = 0;
        TextView tv = ( TextView ) findViewById( R.id.smslist );

        StringBuilder smsBuilder = new StringBuilder();
        final String SMS_URI_SENT = "content://sms/sent";
        final String SMS_URI_INBOX = "content://sms/inbox";
        try
        {
            Map< Long, Message > conversationMap = new TreeMap< Long, Message >();

            String[] projection = new String[]
            {
                "_id", "address", "person", "body", "date", "type"
            };

            Uri uriInbox = Uri.parse( SMS_URI_INBOX );
            Cursor curReceived =
                getContentResolver()
                    // .query( uriInbox, projection, "PHONE_NUMBERS_EQUAL(address, '" + number +
                    // "')",

                    // To get the sms of Names without numbers eg: SBT, IRCTC
                    // .query( uriInbox, projection, "address ='" + number + "'",

                    .query( uriInbox, projection, "address ='" + number + "'",
                        null, "date asc" );

            Uri uri = Uri.parse( SMS_URI_SENT );
            Cursor curSent =
                getContentResolver()
                    .query( uri, projection, "PHONE_NUMBERS_EQUAL(address, '" + number + "')",
                        null,
                        "date asc" );

            boolean sent = false;
            boolean received = false;
            sent = curSent.moveToFirst();
            received = curReceived.moveToFirst();

            if ( received && sent )
            {

                // Conversation
                int indexAddress = curReceived.getColumnIndex( "address" );
                int indexPerson = curReceived.getColumnIndex( "person" );
                int indexBody = curReceived.getColumnIndex( "body" );
                int indexDate = curReceived.getColumnIndex( "date" );
                int indexType = curReceived.getColumnIndex( "type" );

                do
                {
                    smsReceivedCount++;
                    String strAddress = curReceived.getString( indexAddress );
                    int intPerson = curReceived.getInt( indexPerson );
                    String strbody = curReceived.getString( indexBody );
                    long longDate = curReceived.getLong( indexDate );
                    int intType = curReceived.getInt( indexType );

                    Message sms = new Message();
                    sms.setAddress( strAddress );
                    sms.setBody( strbody );
                    sms.setDate( longDate );
                    sms.setType( intType );
                    sms.setPerson( intPerson );
                    sms.setReceived( true );

                    conversationMap.put( longDate, sms );
                    //
                    // smsBuilder.append( "[ " );
                    // // smsBuilder.append( strAddress + ", " );
                    // // smsBuilder.append( intPerson + ", " );
                    // smsBuilder.append( strbody + " " );
                    // // smsBuilder.append( longDate + ", " );
                    // // smsBuilder.append( int_Type );
                    // smsBuilder.append( " ]\n\n" );
                }
                while ( curReceived.moveToNext() );

                indexAddress = curSent.getColumnIndex( "address" );
                indexPerson = curSent.getColumnIndex( "person" );
                indexBody = curSent.getColumnIndex( "body" );
                indexDate = curSent.getColumnIndex( "date" );
                indexType = curSent.getColumnIndex( "type" );

                do
                {
                    smsSentCount++;
                    String strAddress = curSent.getString( indexAddress );
                    int intPerson = curSent.getInt( indexPerson );
                    String strbody = curSent.getString( indexBody );
                    long longDate = curSent.getLong( indexDate );
                    int intType = curSent.getInt( indexType );

                    Message sms = new Message();
                    sms.setAddress( strAddress );
                    sms.setBody( strbody );
                    sms.setDate( longDate );
                    sms.setType( intType );
                    sms.setPerson( intPerson );
                    sms.setReceived( false );

                    conversationMap.put( longDate, sms );
                    //
                    // smsBuilder.append( "[ " );
                    // // smsBuilder.append( strAddress + ", " );
                    // // smsBuilder.append( intPerson + ", " );
                    // smsBuilder.append( strbody + " " );
                    // // smsBuilder.append( longDate + ", " );
                    // // smsBuilder.append( int_Type );
                    // smsBuilder.append( " ]\n\n" );
                }
                while ( curSent.moveToNext() );

            }

            else if ( received )
            {
                // Only Received
                int indexAddress = curReceived.getColumnIndex( "address" );
                int indexPerson = curReceived.getColumnIndex( "person" );
                int indexBody = curReceived.getColumnIndex( "body" );
                int indexDate = curReceived.getColumnIndex( "date" );
                int indexType = curReceived.getColumnIndex( "type" );

                do
                {
                    smsReceivedCount++;
                    String strAddress = curReceived.getString( indexAddress );
                    int intPerson = curReceived.getInt( indexPerson );
                    String strbody = curReceived.getString( indexBody );
                    long longDate = curReceived.getLong( indexDate );
                    int intType = curReceived.getInt( indexType );

                    Message sms = new Message();
                    sms.setAddress( strAddress );
                    sms.setBody( strbody );
                    sms.setDate( longDate );
                    sms.setType( intType );
                    sms.setPerson( intPerson );
                    sms.setReceived( true );

                    conversationMap.put( longDate, sms );
                    //
                    // smsBuilder.append( "[ " );
                    // // smsBuilder.append( strAddress + ", " );
                    // // smsBuilder.append( intPerson + ", " );
                    // smsBuilder.append( strbody + " " );
                    // // smsBuilder.append( longDate + ", " );
                    // // smsBuilder.append( int_Type );
                    // smsBuilder.append( " ]\n\n" );
                }
                while ( curReceived.moveToNext() );
            }

            else if ( sent )
            {
                // Only Sent
                int indexAddress = curSent.getColumnIndex( "address" );
                int indexPerson = curSent.getColumnIndex( "person" );
                int indexBody = curSent.getColumnIndex( "body" );
                int indexDate = curSent.getColumnIndex( "date" );
                int indexType = curSent.getColumnIndex( "type" );

                do
                {
                    smsSentCount++;
                    String strAddress = curSent.getString( indexAddress );
                    int intPerson = curSent.getInt( indexPerson );
                    String strbody = curSent.getString( indexBody );
                    long longDate = curSent.getLong( indexDate );
                    int intType = curSent.getInt( indexType );

                    Message sms = new Message();
                    sms.setAddress( strAddress );
                    sms.setBody( strbody );
                    sms.setDate( longDate );
                    sms.setType( intType );
                    sms.setPerson( intPerson );
                    sms.setReceived( false );

                    conversationMap.put( longDate, sms );
                    //
                    // smsBuilder.append( "[ " );
                    // // smsBuilder.append( strAddress + ", " );
                    // // smsBuilder.append( intPerson + ", " );
                    // smsBuilder.append( strbody + " " );
                    // // smsBuilder.append( longDate + ", " );
                    // // smsBuilder.append( int_Type );
                    // smsBuilder.append( " ]\n\n" );
                }
                while ( curSent.moveToNext() );
            }
            else
            {
                SCUtility.showDialogOk( number, "No messages sent or received from this number",
                    MainActivity.this );
            } // end if

            if ( !curReceived.isClosed() )
            {
                curReceived.close();
                curReceived = null;
            }
            if ( !curSent.isClosed() )
            {
                curSent.close();
                curSent = null;
            }

            for ( Long date : conversationMap.keySet() )
            {
                Message sms = new Message();
                sms = conversationMap.get( date );

                if ( sms.isReceived() )
                {
                    smsBuilder.append( name + " :: " + sms.getBody() + "\n\n" );
                }
                else
                {
                    smsBuilder.append( "Me :: " + sms.getBody() + "\n\n" );
                }

            }
            tv.setText( smsSentCount + " SMS Sent: \n\n" + smsReceivedCount + " SMS received: \n\n"
                + smsBuilder );

        }
        catch ( Exception ex )
        {
            Log.e( "SQLiteException", ex.getMessage(), ex );
        }

    }

    private ArrayList< String > getallConversations()
    {
        // TODO
        Uri SMS_INBOX = Uri.parse( "content://mms-sms/conversations/" );
        Cursor c = getContentResolver().query( SMS_INBOX, null, null, null, "date desc" );
        MAX_SIZE = c.getCount();

        ArrayList< String > conversationAdresses = new ArrayList< String >();

        if ( c.moveToFirst() )
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
        return conversationAdresses;
    }

    @Override
    public boolean onCreateOptionsMenu( Menu menu )
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate( R.menu.main, menu );
        return true;
    }

    @Override
    protected void onActivityResult( int requestCode, int resultCode, Intent data )
    {
        super.onActivityResult( requestCode, resultCode, data );

        if ( resultCode == RESULT_OK )
        {
            Bundle extras = data.getExtras();
            if ( extras != null )
            {
                String name = extras.getString( "result" );
                String number = getPhoneNumber( name );
                getSMS( number, name );
            }
            else
            {
                SCUtility.showDialogOk( "No", "No data found", MainActivity.this );
            }
        }
    }

    public String getPhoneNumber( String name )
    {
        String ret = null;
        String num = null;
        String selection = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
            + " like'%" + name + "%'";
        String[] projection = new String[]
        {
            ContactsContract.CommonDataKinds.Phone.NUMBER
        };
        Cursor c = getContentResolver().query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            projection, selection, null, null );
        if ( c.moveToFirst() )
        {
            ret = c.getString( 0 );
            num = ret.replaceAll( "-", "" );
        }
        c.close();
        if ( num == null )
        {
            num = name;
        }
        return num;
    }
}
