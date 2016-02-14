package info.physbox;

//ORM

public class Item implements MessageInterface 
{
    private String message = "";
    private String date = "" ;
    private String author = "";

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

    public String getMessage()
    {
        return message;
    }

    public String getAuthor()
    {
        return author;
    }

    public String getDate()
    {
        return date;
    } 
}