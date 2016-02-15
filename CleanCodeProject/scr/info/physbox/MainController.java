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

    private Message ms;

    private String userName;

    protected void onCreate()
    {
        System.out.println("Welcome!!!\n\rversion 0.1\n\rAuthor @Jurasikt\n\rEnter you nikename...");
        Scanner in = new Scanner(System.in);

        this.userName = in.nextLine();
        this.ms = Message.factory();
    }

    protected void onClose()
    {
        this.ms.saveHistory();
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
                //break;
            case 1:
                this.actionNew(this.ms, this.userName);
                break;
            case 2: 
                this.actionShowAll(this.ms);
                break;
            case 3: 
                this.actionDelete(this.ms);
                break;
            case 4:
                this.actionSearch(this.ms);
                break;
        }
        System.out.println("");
        return false;
    }

    private void actionNew(Message ms, String userName)
    {
        System.out.println("Enter new message...");
        Scanner in = new Scanner(System.in);
        String text = in.nextLine();
        
        ms.addMessage(text, userName);
    }

    private void actionShowAll(Message ms)
    {
        ms.echoAll();
    }

    private void actionDelete(Message ms)
    {
        System.out.println("Please enter message id...");
        Scanner in = new Scanner(System.in);
        int id = in.nextInt();
        ms.deleteById(id);        
    }

    private void actionSearch(Message ms)
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
                if (ms.searchByPregExp(regexp)) {
                    System.out.println("404 Not Found");
                }
                break;
            case 1:
                System.out.println("Enter author...");
                String author = in.nextLine();
                author = in.nextLine();
                if (ms.searchByAuthor(author)) {
                    System.out.println("404 Not Found");
                }
                break;
            case 2:
                System.out.println("Enter key word...");
                String word = in.nextLine();
                word = in.nextLine();
                if (ms.searchByKeyWord(word)) {
                    System.out.println("404 Not Found");
                }            
                break;
            case 3:
                System.out.println("Enter date in format YYYY-mm-dd\n\rFrom...");
                String from = in.nextLine();
                from = in.nextLine();
                System.out.println("Enter date in format YYYY-mm-dd\n\rTo...");
                String to = in.nextLine();
                if (ms.searchByDate(from,to)) {
                    System.out.println("404 Not Found");
                }            
                break;
        }
    }

    public static MainController factory()
    {
        return new MainController();
    }
} 