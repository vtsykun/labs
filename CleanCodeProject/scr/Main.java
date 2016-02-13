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
//import pac.Item;
import info.physbox.Message;

public class Main {

    public static void main(String[] args) {
        Scanner in;
        String  userName,text;
        int action;
        System.out.println("Welcome!!!\n\rversion 0.1\n\rAuthor @Jurasikt\n\rEnter you nikename...\t");
        in = new Scanner(System.in);
        userName = in.nextLine();

        Message ms = Message.factory();
      
        while (true) {
            System.out.println("Choose action...\n\r[1]: New message\n\r"+
                "[2]: Show all messages\n\r[3]: Delete message\n\r[4] Exit");
            in = new Scanner(System.in);
            action = in.nextInt();

            if (action == 4) {
                ms.saveHistory();
                break;
            }
            switch (action) {
                case 1:
                    System.out.println("Enter new message...");
                    text = in.nextLine();
                    text = in.nextLine();
                    ms.addMessage(text, userName);
                    break;
                case 2: 
                    ms.echoAll();
                    break;
                case 3: 
                    System.out.println("Please enter message id...");
                    int id = in.nextInt();
                    ms.deleteById(id);
                    break;
            }
        }
    }
}
