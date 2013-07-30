package com.example.myfirstapp;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ConversationListAdapater extends ArrayAdapter< SmsInfoUnit >
{
    TextView nameTv;
    TextView countTv;
    int layoutResourceId;

    Context context;
    ArrayList< SmsInfoUnit > smsInfoUnitList = null;

    public ConversationListAdapater( Context context, int layoutResourceId,
        ArrayList< SmsInfoUnit > smsInfoUnitList )
    {
        super( context, layoutResourceId, smsInfoUnitList );
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.smsInfoUnitList = smsInfoUnitList;
    }

    @Override
    public View getView( int position, View convertView, ViewGroup parent )
    {
        View row = convertView;

        if ( smsInfoUnitList != null )
        {
            SmsInfoUnit unit = smsInfoUnitList.get( position );

            if ( row == null )
            {
                LayoutInflater inflater = ( ( Activity ) context ).getLayoutInflater();
                row = inflater.inflate( layoutResourceId, parent, false );

                countTv = ( TextView ) row.findViewById( R.id.convCount );
                nameTv = ( TextView ) row.findViewById( R.id.conversation );
            }

            countTv.setText( unit.getCount() + "" );
            nameTv.setText( unit.getAddress() );
        }
        return row;
    }

}
