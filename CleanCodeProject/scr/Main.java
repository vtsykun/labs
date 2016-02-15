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

import info.physbox.MainController;

public class Main {

    public static void main(String[] args) {
        
        MainController controller = MainController.factory();
        controller.run();
    }
}
