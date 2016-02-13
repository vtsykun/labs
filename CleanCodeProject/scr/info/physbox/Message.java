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

    public String home = "../var/";

    public Map history = new HashMap<Integer,Item>();

    public int maxId = 0;

    private Message() 
    {
        this.render();            
    }

    private void render()
    {
        Gson json = new GsonBuilder().create();
        Type t = new TypeToken<Map<Integer, Item>>(){}.getType();

        this.history = json.fromJson(
            fileGetContent(this.home+"message.json"),t);

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
    
            System.out.println("ID: "+id+"\n\rAuthor: "+ms.author+"\n\rDate: "+
                ms.date+"\n\r-------\n\r"+ms.message+"\n\r\n\r");

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
            FileWriter fwrite = new FileWriter(this.home+"message.json");
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