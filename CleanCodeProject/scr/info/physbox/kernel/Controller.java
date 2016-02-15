package info.physbox.kernel;

import java.util.*;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

abstract public class Controller
{
	public void run()
	{
		this.onCreate();

		while (true) {
			if (this.listing()) {
				break;
			}
		}
		this.onClose();
	}

	protected abstract void onCreate();

	protected abstract void onClose();

	protected abstract boolean listing();

}