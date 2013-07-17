package com.example.myfirstapp;

public class Message
{
    private String body;
    private Long date;
    private String address;
    private int type;
    private int person;

    public String getBody()
    {
        return body;
    }

    public void setBody( String body )
    {
        this.body = body;
    }

    public Long getDate()
    {
        return date;
    }

    public void setDate( Long date )
    {
        this.date = date;
    }

    public String getAddress()
    {
        return address;
    }

    public void setAddress( String address )
    {
        this.address = address;
    }

    public int getType()
    {
        return type;
    }

    public void setType( int type )
    {
        this.type = type;
    }

    public int getPerson()
    {
        return person;
    }

    public void setPerson( int person )
    {
        this.person = person;
    }

}
