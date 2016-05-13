package fr.kraiss.scratch.gist;

import fr.kraiss.scratch.models.MyClass;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author Pierrick Rassat
 * @see https://github.com/kraiss
 */
public class PrivateFieldsAndMethodsAccess {

    public static void accessPrivateField() throws NoSuchFieldException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        // !! WARNING !! Use this with caution
        // This is a dirty but it can be very efficient and very useful if you use it correctly.
        // As instance, if you don't want to set an attribute or method visible but need it for tests. You can use this.

        // Create a instance of MyClass
        MyClass myClassObj = new MyClass("FirstValue");

        // Get the private field value and print it
        Field field = MyClass.class.getDeclaredField("privateString");
        field.setAccessible(true);
        System.out.println("FirstValue == " + (String) field.get(myClassObj));

        // Set a new value
        Method setMethod = MyClass.class.getDeclaredMethod("setPrivateString", new Class[] { String.class });
        setMethod.setAccessible(true);
        setMethod.invoke(myClassObj, "SecondValue");

        // Get the new value
        Method getMethod = MyClass.class.getDeclaredMethod("getPrivateString", null);
        getMethod.setAccessible(true);
        System.out.println("SecondValue == " + getMethod.invoke(myClassObj, null));
    }
}
