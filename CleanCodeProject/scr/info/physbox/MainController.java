package info.physbox;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import info.physbox.kernel.Controller;


public class MainController extends  Controller
{

    private Message message;

    private String userName;

    protected void onCreate()
    {
        System.out.println("Welcome!!!\n\rversion 0.1\n\rAuthor @Jurasikt\n\rEnter you nikename...");
        Scanner in = new Scanner(System.in);

        this.userName = in.nextLine();
        this.message = Message.factory();
    }

    protected void onClose()
    {
        this.message.saveHistory();
    }

    protected boolean listing()
    {
        System.out.println("Choose action...\n\r[0]: Exit\n\r[1]: New message\n\r"+
            "[2]: Show all messages\n\r[3]: Delete message\n\r[4]: Search:");

        Scanner in = new Scanner(System.in);
        int action = in.nextInt();

        switch (action) {
            case 0:
                return true;
            case 1:
                this.actionNew(this.userName);
                break;
            case 2: 
                this.actionShowAll();
                break;
            case 3: 
                this.actionDelete();
                break;
            case 4:
                this.actionSearch();
                break;
        }
        System.out.println("");
        return false;
    }

    private void actionNew(String userName)
    {
        System.out.println("Enter new message...");
        Scanner in = new Scanner(System.in);
        String text = in.nextLine();
        
        this.message.addMessage(text, userName);
    }

    private void actionShowAll()
    {
        this.message.echoAll();
    }

    private void actionDelete()
    {
        System.out.println("Please enter message id...");
        Scanner in = new Scanner(System.in);
        int id = in.nextInt();
        this.message.deleteById(id);        
    }

    private void actionSearch()
    {
        System.out.println("Search by...\n\r[0]: Regular expression\n\r"+
            "[1]: Author\n\r[2]: Key word\n\r[3]: Search by date");
        Scanner in = new Scanner(System.in);

        int route = in.nextInt();

        switch (route) {
            case 0:
                System.out.println("Enter regexp...");
                String regexp = in.nextLine();
                regexp = in.nextLine();
                this.message.searchByPregExp(regexp);
                break;
            case 1:
                System.out.println("Enter author...");
                String author = in.nextLine();
                author = in.nextLine();
                this.message.searchByAuthor(author);
                break;
            case 2:
                System.out.println("Enter key word...");
                String word = in.nextLine();
                word = in.nextLine();
                this.message.searchByKeyWord(word);        
                break;
            case 3:
                System.out.println("Enter date in format YYYY-mm-dd\n\rFrom...");
                String from = in.nextLine();
                from = in.nextLine();
                System.out.println("Enter date in format YYYY-mm-dd\n\rTo...");
                String to = in.nextLine();
                this.message.searchByDate(from, to);
                break;
        }
    }

    public static MainController factory()
    {
        return new MainController();
    }
} 