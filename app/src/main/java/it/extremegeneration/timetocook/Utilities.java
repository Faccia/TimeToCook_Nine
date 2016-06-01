package it.extremegeneration.timetocook;

import java.lang.reflect.Field;

/**
 * Created by Alex on 20/05/2016.
 */
public class Utilities {

    public static int getResId(String resName, Class<?> c) {

        try {
            Field idField = c.getDeclaredField(resName);
            return idField.getInt(idField);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
}
