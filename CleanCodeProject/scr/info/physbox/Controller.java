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

public class Controller
{

    public void run()
    {
        int action = 0;
        System.out.println("Welcome!!!\n\rversion 0.1\n\rAuthor @Jurasikt\n\rEnter you nikename...");
        Scanner in = new Scanner(System.in);
        String userName = in.nextLine();

        Message ms = Message.factory();
      
        while (true) {
            System.out.println("Choose action...\n\r[0]: Exit\n\r[1]: New message\n\r"+
                "[2]: Show all messages\n\r[3]: Delete message\n\r[4]: Search:");

            in = new Scanner(System.in);
            action = in.nextInt();
            if (action == 0) {
                ms.saveHistory();
                break;
            }

            switch (action) {
                case 1:
                    this.actionNew(ms, userName);
                    break;
                case 2: 
                    this.actionShowAll(ms);
                    break;
                case 3: 
                    this.actionDelete(ms);
                    break;
                case 4:
                    this.actionSearch(ms);
                    break;
            }
            System.out.println("");
        }
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
            "[1]: Author\n\r[2]: Key word");
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
        }
    }

    public static Controller factory()
    {
        return new Controller();
    }
} 