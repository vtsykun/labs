package info.physbox;

//import MessageInterface

public class Item implements MessageInterface 
{
    public String message = "";
    public String date = "" ;
    public String author = "";

    public Item setDate(String date)
    {
        this.date = date;
        return this;
    }

    public Item setMessage(String message)
    {
        this.message = message;
        return this;
    }

    public Item setAuthor(String author)
    {
        this.author = author;
        return this;
    }
}