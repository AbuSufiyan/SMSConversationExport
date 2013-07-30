package com.example.myfirstapp;

import java.io.Serializable;

public class SmsInfoUnit implements Serializable
{
    /**
     * 
     */
    private static final long serialVersionUID = 5664207430980184347L;

    private String address;
    private int threadId;
    private int count;

    public SmsInfoUnit()
    {
        super();
    }

    public SmsInfoUnit( String address, int threadId, int count )
    {
        super();
        this.address = address;
        this.threadId = threadId;
        this.count = count;
    }

    public String getAddress()
    {
        return address;
    }

    public void setAddress( String address )
    {
        this.address = address;
    }

    public int getThreadId()
    {
        return threadId;
    }

    public void setThreadId( int threadId )
    {
        this.threadId = threadId;
    }

    public long getCount()
    {
        return count;
    }

    public void setCount( int count )
    {
        this.count = count;
    }

}
