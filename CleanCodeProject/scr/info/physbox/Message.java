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
//import .Item;

public class Message 
{

    private String homePath = "../var/";

    private Map history = new HashMap<Integer,Item>();

    private int maxId = 0;

    private Message() 
    {
        this.jsonDecode();            
    }

    private void jsonDecode()
    {
        Gson json = new GsonBuilder().create();
        Type t = new TypeToken<Map<Integer, Item>>(){}.getType();

        this.history = json.fromJson(
            fileGetContent(this.homePath+"message.json"),t);

        Set<Map.Entry> ents = this.history.entrySet();

        for (Map.Entry<Integer, Item> item: ents) {
            if (this.maxId < (int)item.getKey()) {
                this.maxId = (int)item.getKey();
            }
        }  
    }

    /**
     * Выводит сообщения по индификатору 
     * @param int id  идишник сообщения
     */
    public void echoById(int id)
    {
        Item ms = (Item)this.history.get((Integer)id);
        if (ms == null) {
            System.out.println("404 Not Found");
        } else {
    
            System.out.println("ID: "+id+"\n\rAuthor: "+ms.getAuthor()+"\n\rDate: "+
                ms.getDate()+"\n\r-------\n\r"+ms.getMessage()+"\n\r\n\r");

        }
    }

    /**
     * Выводит всю историю сообщений
     *  
     */
    public void echoAll()
    {
        Set<Map.Entry> ents = this.history.entrySet();

        for (Map.Entry<Integer, Item> item: ents) {
            this.echoById((int)item.getKey());
        }
    }

    public boolean searchByPregExp(String pattern)
    {
        boolean notFount = true;
        Set<Map.Entry> ents = this.history.entrySet();

        for (Map.Entry<Integer, Item> item: ents) {

            Item ms = item.getValue();
            if (ms.getMessage().matches(pattern)) {
                notFount = false;
                this.echoById((int)item.getKey());
            }
            
        } 
        return notFount;
    }

    public boolean searchByKeyWord(String key)
    {
        boolean notFount = true;
        Set<Map.Entry> ents = this.history.entrySet();

        for (Map.Entry<Integer, Item> item: ents) {
            
            Item ms = item.getValue();
            if (ms.getMessage().indexOf(key) != -1) {
                notFount = false;
                this.echoById((int)item.getKey());
            }
            
        }
        return notFount;
    }

    public boolean searchByAuthor(String author)
    {
        boolean notFount = true;
        Set<Map.Entry> ents = this.history.entrySet();

        for (Map.Entry<Integer, Item> item: ents) {
            
            Item ms = item.getValue();
            if (ms.getAuthor().equals(author)) {
                notFount = false;
                this.echoById((int)item.getKey());
            }
            
        }
        return notFount;
    }

    public boolean searchByDate(String from, String to) 
    {
        boolean notFount = true;

        if (!from.matches("\\d{4}-\\d{2}-\\d{2}") || 
                !to.matches("\\d{4}-\\d{2}-\\d{2}")) {
            return false;
        }
        Set<Map.Entry> ents = this.history.entrySet();

        for (Map.Entry<Integer, Item> item: ents) {

            Item ms = item.getValue();
            if (ms.getDate().compareTo(from) >= 0 && 
                    ms.getDate().compareTo(to) <= 0) {
                
                notFount = false;
                this.echoById((int)item.getKey());
            }
            
        }
        return notFount;        
    }

    /**
     * Создает новое сообщение 
     * @param String message тект нового сообщения 
     * @paarm String author 
     */
    public void addMessage(String message, String author)
    {
        Item newMessage = new Item();

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        newMessage.setMessage(message)
            .setAuthor(author)
            .setDate(dateFormat.format(date));

        this.history.put((Integer)(++this.maxId), newMessage);

    }

    /**
     * Сохраняет все сообщения в файл .json
     *
     */
    public void saveHistory()
    {
        Gson gson = new Gson();
        String json = gson.toJson(this.history);

        try {
            FileWriter fwrite = new FileWriter(this.homePath+"message.json");
            fwrite.write(json);
            fwrite.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteById(int id) 
    {
        this.history.remove((Integer)id);
    }

    public String fileGetContent(String filename) 
    {
        File file = new File(filename); 
        String content = "";
        try {
            FileReader th = new FileReader(file);
            char[] chars = new char[(int)file.length()];
            th.read(chars);
            th.close();
            content = new String(chars);
            
        } catch (IOException e) {
            return "";
        } 
        return content;
    }


    public static Message factory()
    {
        return new Message();
    }

}