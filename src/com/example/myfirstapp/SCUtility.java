package com.example.myfirstapp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.os.Handler;
import android.text.format.Time;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

public class SCUtility
{

    private static Time sTime = new Time();

    public static String convertStreamToString( InputStream is )
        throws IOException
    {
        if ( is != null )
        {
            StringBuilder sb = new StringBuilder();
            String line;

            try
            {
                BufferedReader reader = new BufferedReader(
                    new InputStreamReader( is, "UTF-8" ) );
                while ( ( line = reader.readLine() ) != null )
                {
                    sb.append( line ).append( "\n" );
                }
            }
            finally
            {
                is.close();
            }
            return sb.toString();
        }
        else
        {
            return "";
        }
    }

    public static String getMd5Hash( String input )
    {
        try
        {
            MessageDigest md = MessageDigest.getInstance( "MD5" );
            byte[] messageDigest = md.digest( input.getBytes() );
            BigInteger number = new BigInteger( 1, messageDigest );
            String md5 = number.toString( 16 );
            while ( md5.length() < 32 )
            {
                md5 = "0" + md5;
            }
            return md5;
        }
        catch ( NoSuchAlgorithmException e )
        {
            Log.e( "MD5", e.getMessage() );
            return null;
        }
    }

    public static String getMd5DecrytedString( String input )
    {
        try
        {
            byte[] password = input.getBytes( "UTF-8" );
            byte[] ciphertext =
            {
                -68, -112, 66, 78, 85, 50, 22, -63, 16, 24,
                -45, 4, -116, -14, 88, 34, -85, 116, 105, 59, 45, -126
            };
            byte[] plaintext = md5Decrypt( password, ciphertext );
            return new String( plaintext, "UTF-8" );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            return null;
        }
    }

    public static byte[] md5Decrypt( byte[] password, byte[] ciphertext )
    {
        try
        {
            MessageDigest digest = MessageDigest.getInstance( "MD5" );
            byte[] hash = digest.digest( password );
            Cipher rc4 = Cipher.getInstance( "RC4" );
            rc4.init( Cipher.DECRYPT_MODE, new SecretKeySpec( hash, "RC4" ) );
            return rc4.doFinal( ciphertext );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            return null;
        }
    }

    public static void showDialogOk( String title, String message,
        Context context )
    {
        Dialog dlg = new AlertDialog.Builder( context ).setTitle( title )
            .setMessage( message ).setCancelable( false )
            .setPositiveButton( "OK", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick( DialogInterface dialog, int whichButton )
                {

                }
            } ).create();
        dlg.setOnKeyListener( new DialogInterface.OnKeyListener()
        {

            @Override
            public boolean onKey( DialogInterface dialog, int keyCode,
                KeyEvent event )
            {
                if ( keyCode == KeyEvent.KEYCODE_SEARCH
                    && event.getRepeatCount() == 0 )
                {
                    return true;
                }
                if ( keyCode == KeyEvent.KEYCODE_MENU )
                {
                    return true;
                }
                return false;
            }

        } );
        dlg.show();
    }

    public static Dialog showDialogInitialFetchStatus( String title, String message,
        Context context )
    {
        Dialog dlg = new AlertDialog.Builder( context ).setTitle( title )
            .setMessage( message ).setCancelable( false )
            .setPositiveButton( "OK", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick( DialogInterface dialog, int whichButton )
                {

                }
            } ).create();
        dlg.setOnKeyListener( new DialogInterface.OnKeyListener()
        {

            @Override
            public boolean onKey( DialogInterface dialog, int keyCode,
                KeyEvent event )
            {
                if ( keyCode == KeyEvent.KEYCODE_SEARCH
                    && event.getRepeatCount() == 0 )
                {
                    return true;
                }
                if ( keyCode == KeyEvent.KEYCODE_MENU )
                {
                    return true;
                }
                return false;
            }

        } );
        dlg.show();
        return dlg;
    }

    public static void showDialogOkWithGoBack( String title, String message,
        final Activity activity )
    {
        AlertDialog.Builder adb = new AlertDialog.Builder( activity );
        adb.setTitle( title );
        adb.setMessage( message );
        adb.setNeutralButton( "OK", new DialogInterface.OnClickListener()
        {

            @Override
            public void onClick( DialogInterface dialog, int which )
            {
                dialog.cancel();
                activity.onBackPressed();
            }
        } );
        adb.setOnKeyListener( new DialogInterface.OnKeyListener()
        {

            @Override
            public boolean onKey( DialogInterface dialog, int keyCode,
                KeyEvent event )
            {
                if ( keyCode == KeyEvent.KEYCODE_SEARCH
                    && event.getRepeatCount() == 0 )
                {
                    return true;
                }
                if ( keyCode == KeyEvent.KEYCODE_MENU )
                {
                    return true;
                }
                return false;
            }

        } );
        AlertDialog ad = adb.create();
        ad.show();
    }

    public static void showToast( String message, Context context )
    {

        // Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

        final Toast toast = Toast.makeText( context, message, Toast.LENGTH_LONG );
        toast.show();

        Handler handler = new Handler();
        handler.postDelayed( new Runnable()
        {
            @Override
            public void run()
            {
                toast.cancel();
            }
        }, 3000 );

    }

    public static boolean isNetworkAvailable( Context context )
    {
        ConnectivityManager connec = ( ConnectivityManager ) context
            .getSystemService( Context.CONNECTIVITY_SERVICE );
        NetworkInfo netInfo = connec.getActiveNetworkInfo();
        if ( netInfo != null && netInfo.isConnected() == true )
        {
            return true;
        }
        return false;
    }

    /**
     * Downloads the file from a URL.
     * 
     * @param fileUrl
     */
    public static Bitmap downloadFileFromUrl( String fileUrl )
    {

        URL myFileUrl = null;
        Bitmap imageBitmap = null;

        try
        {
            myFileUrl = new URL( fileUrl );
        }
        catch ( MalformedURLException e )
        {
            e.printStackTrace();
        }

        try
        {
            HttpURLConnection connection = ( HttpURLConnection ) myFileUrl
                .openConnection();
            connection.setDoInput( true );
            connection.connect();
            InputStream is = connection.getInputStream();
            imageBitmap = BitmapFactory.decodeStream( is );
        }
        catch ( IOException e )
        {
            e.printStackTrace();
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }

        return imageBitmap;
    }

    public static ProgressDialog showProgressDialog( Context context )
    {
        ProgressDialog myProgressDialog = new ProgressDialog( context );
        myProgressDialog.setMessage( "Please wait..." );
        myProgressDialog.setCancelable( false );
        myProgressDialog.requestWindowFeature( Window.FEATURE_NO_TITLE );
        myProgressDialog.show();
        myProgressDialog.setOnKeyListener( new DialogInterface.OnKeyListener()
        {

            @Override
            public boolean onKey( DialogInterface dialog, int keyCode,
                KeyEvent event )
            {
                if ( keyCode == KeyEvent.KEYCODE_SEARCH
                    && event.getRepeatCount() == 0 )
                {
                    return true;
                }
                if ( keyCode == KeyEvent.KEYCODE_MENU )
                {
                    return true;
                }
                return false;
            }

        } );

        Log.d( "qblog", "dialog shown" );
        return myProgressDialog;
    }

    public static void dismissProgressDialog( ProgressDialog myProgressDialog )
    {
        if ( myProgressDialog != null )
        {
            try
            {
                myProgressDialog.dismiss();
                Log.d( "qblog", "dialog dismissed" );
            }
            catch ( Exception e )
            {
                e.printStackTrace();
            }
        }
    }

    public static Date getDateFromTimeStamp( String timestamp )
        throws ParseException
    {
        // SimpleDateFormat format = new SimpleDateFormat("MMddyyHHmmss");
        Date date = new Date( Long.parseLong( timestamp ) );
        // date = new Date(timestamp);
        return date;
    }

    public static Bitmap writeBitmapToPath( byte[] array, String filename )
    {
        try
        {
            Bitmap bmp = BitmapFactory.decodeByteArray( array, 0, array.length );

            File path = Environment.getExternalStorageDirectory();
            File file = new File( path, "CareTracker" );
            if ( !file.exists() )
            {
                file.mkdir();
            }

            FileOutputStream out = new FileOutputStream( path + "/CareTracker/"
                + filename + ".jpg" );
            bmp.compress( Bitmap.CompressFormat.JPEG, 90, out );

            return bmp;
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            return null;
        }

    }

    public static boolean validateMobileNo( String mobileNo )
    {
        if ( mobileNo.equals( "" ) || mobileNo.length() < 10 )
        {
            return false;
        }
        else if ( mobileNo.contains( "-" ) && mobileNo.length() > 10 )
        {
            Pattern p = Pattern.compile( "^[0-9]{3}\\-?[0-9]{3}\\-?[0-9]{4}$" );
            Matcher m = p.matcher( mobileNo );
            if ( m.matches() == false )
            {
                return false;
            }
            else
            {
                return true;
            }
        }
        else if ( mobileNo.contains( "+" ) && mobileNo.length() != 12 )
        {
            return false;
        }
        else if ( !mobileNo.contains( "+" ) && mobileNo.length() > 10 )
        {
            return false;
        }
        else
        {
            if ( mobileNo.length() == 10 || mobileNo.length() == 12 )
            {
                Pattern p = null;
                if ( mobileNo.length() == 10 )
                {
                    p = Pattern.compile( "[0-9]*" );
                }
                else
                {
                    p = Pattern.compile( "[+][0-9]*" );
                }
                Matcher m = p.matcher( mobileNo );
                if ( m.matches() == false )
                {
                    return false;
                }
                else
                {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean validateEmail( String email )
    {

        Pattern emailPattern = Pattern.compile( ".+@.+\\.[a-z]+" );
        Matcher emailMatcher = emailPattern.matcher( email );
        if ( !emailMatcher.matches() )
        {
            return false;
        }

        return true;
    }

    public static String formatDate( String datetoFormat )
    {
        String formatedDate = null;
        try
        {
            SimpleDateFormat df1 = new SimpleDateFormat( "dd/mm/yyyy" );
            SimpleDateFormat df2 = new SimpleDateFormat( "yyyy-mm-dd" );
            formatedDate = df2.format( df1.parse( datetoFormat ) );
        }
        catch ( ParseException e )
        {
            e.printStackTrace();
        }
        return formatedDate;
    }

    public static String reverseFormatDate( String datetoFormat )
    {
        String formatedDate = null;
        try
        {
            SimpleDateFormat df1 = new SimpleDateFormat( "yyyy-mm-dd" );
            SimpleDateFormat df2 = new SimpleDateFormat( "dd/mm/yyyy" );
            formatedDate = df2.format( df1.parse( datetoFormat ) );
        }
        catch ( ParseException e )
        {
            e.printStackTrace();
        }
        return formatedDate;
    }

    public static String reverseFormatDateMMDDYYYY( String datetoFormat )
    {
        String formatedDate = null;
        try
        {
            SimpleDateFormat df1 = new SimpleDateFormat( "yyyy-mm-dd" );
            SimpleDateFormat df2 = new SimpleDateFormat( "mm/dd/yyyy" );
            formatedDate = df2.format( df1.parse( datetoFormat ) );
        }
        catch ( ParseException e )
        {
            e.printStackTrace();
        }
        return formatedDate;
    }

    public static String convertDateToTimeStamp( String dateToCovert )
    {
        SimpleDateFormat formatter;
        Date date = null;
        formatter = new SimpleDateFormat( "dd/mm/yyyy" );
        try
        {
            date = formatter.parse( dateToCovert );
        }
        catch ( ParseException e )
        {
            e.printStackTrace();
        }
        java.sql.Timestamp timeStampDate = new Timestamp( date.getTime() );

        return String.valueOf( timeStampDate.getTime() );
    }

    public static void showCloseApplicationDialoge( final Activity activity )
    {

        AlertDialog.Builder builder = new AlertDialog.Builder( activity );

        builder.setMessage( "Do you want to exit CareTracker?" )
            .setCancelable( false )

            .setPositiveButton( "Yes",
                new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick( DialogInterface dialog, int id )
                    {
                        activity.finish();
                    }
                } )

            .setNegativeButton( "No", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick( DialogInterface dialog, int id )
                {
                    dialog.cancel();
                }
            } );

        AlertDialog alert = builder.create();
        alert.show();
    }

    public static long parseTime( String time )
    {
        sTime.parse( time );
        return sTime.toMillis( false );
    }

    public static final Calendar cal = Calendar.getInstance();
    public static final TimeZone DEFAULT_TIME_ZONE = cal.getTimeZone();

    public static long getCurrentTime( final Context context )
    {
        return System.currentTimeMillis();
    }

    public static String getCurrentDate()
    {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat( "yyyy-MM-dd" );
        String currentDate = df.format( c.getTime() );

        return currentDate;
    }

    public static String formatTimestampToTime( long timestamp )
    {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis( timestamp );
        int mHour = cal.get( Calendar.HOUR_OF_DAY );
        int mMinute = cal.get( Calendar.MINUTE );

        return pad( mHour ) + ":" + pad( mMinute );
    }

    public static String pad( int c )
    {
        if ( c >= 10 )
        {
            return String.valueOf( c );
        }
        else
        {
            return "0" + String.valueOf( c );
        }
    }

    public static void hideKeyboardOnButtonClick( Activity activity )
    {
        if ( activity != null )
        {
            InputMethodManager inputManager = ( InputMethodManager ) activity
                .getSystemService( Context.INPUT_METHOD_SERVICE );

            if ( inputManager != null )
            {
                try
                {
                    inputManager.hideSoftInputFromWindow( activity
                        .getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS );
                }
                catch ( Exception e )
                {

                }
            }
        }
    }

    public static void hideKeyboardWithEditTextFocus( Activity activity, EditText editText, int flag )
    {
        InputMethodManager inputManager = ( InputMethodManager ) activity
            .getSystemService( Context.INPUT_METHOD_SERVICE );

        if ( inputManager != null )
        {
            inputManager.hideSoftInputFromWindow( editText.getWindowToken(), flag );
        }

    }

    public static String getDateInFormatFromTimeStamp( long timestamp )
    {
        Log.i( "Date in formatted",
            "" + new SimpleDateFormat( "yyyy-MM-dd" ).format( new Date( timestamp ) ) );
        return new SimpleDateFormat( "yyyy-MM-dd" ).format( new Date( timestamp ) );
    }

    public static String mergeString( String firstString, String secondString )
    {
        if ( firstString == null && secondString == null )
        {
            return null;
        }
        else if ( firstString == null && secondString != null )
        {
            return secondString;
        }
        else if ( firstString != null && secondString == null )
        {
            return firstString;
        }
        else
        {
            return firstString + " " + secondString;
        }

    }

    public static String getDateFormattedInDesiredForm( long timestamp, String pattern )
    {
        Log.i( "Date in formatted in desired form",
            "" + new SimpleDateFormat( pattern ).format( new Date( timestamp ) ) );
        return new SimpleDateFormat( pattern ).format( new Date( timestamp ) );
    }

    public static ArrayList< String > getWeekTimestampList( Calendar currentDate )
    {
        Calendar rightNow = Calendar.getInstance();
        rightNow.set( currentDate.get( Calendar.YEAR ),
            currentDate.get( Calendar.MONTH ),
            currentDate.get( Calendar.DAY_OF_MONTH ) );
        rightNow.setTime( currentDate.getTime() );

        ArrayList< String > weekDays = new ArrayList< String >();
        // Set the calendar to Sunday of the current week
        rightNow.set( Calendar.DAY_OF_WEEK, Calendar.SUNDAY );

        for ( int i = 0; i < 7; i++ )
        {

            weekDays.add( getDateInFormatFromTimeStamp(
                rightNow.getTimeInMillis() ).replaceAll( "-", "/" ) );
            rightNow.add( Calendar.DATE, 1 );
        }
        return weekDays;

    }

    public static String modifyCalendarTaskDates( String datetoFormat )
    {
        String formatedDate = null;
        try
        {
            SimpleDateFormat df1 = new SimpleDateFormat( "yyyy/M/d" );
            SimpleDateFormat df2 = new SimpleDateFormat( "yyyy-MM-dd" );
            formatedDate = df2.format( df1.parse( datetoFormat ) );
        }
        catch ( ParseException e )
        {
            e.printStackTrace();
        }
        return formatedDate;
    }

    public static String formatDate( String fromFormat, String toFormat, String datetoFormat )
    {
        String formatedDate = null;
        try
        {
            SimpleDateFormat df1 = new SimpleDateFormat( fromFormat );
            SimpleDateFormat df2 = new SimpleDateFormat( toFormat );
            formatedDate = df2.format( df1.parse( datetoFormat ) );
        }
        catch ( ParseException e )
        {
            e.printStackTrace();
            formatedDate = datetoFormat;
        }
        return formatedDate;
    }

}
