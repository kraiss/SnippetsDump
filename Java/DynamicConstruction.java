package fr.kraiss.scratch.gist;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * @author Pierrick Rassat
 * @see https://github.com/kraiss
 */
public class DynamicConstruction {

    public static void dynamicUseOfConstructor() throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        // !! WARNING !! Use this with caution
        // This is a dirty but it can be very efficient and very useful if you use it correctly.

        // Get a class from a fully-qualified classname
        Class myClass = Class.forName("fr.kraiss.scratch.models.MyClass");
        // For inner classes use '$' : Class.forName("fr.kraiss.scratch.models.MyClass$MyInnerClass");

        // Get the public constructor of the class taking parameters in 'new Class[] {...}'
        Constructor<MyClass> constructor = myClass.getConstructor(new Class[]{String.class});

        // Get the public constructor of the class taking no parameter
        Constructor<MyClass> constructor2 = myClass.getConstructor(null);

        // Finally, instantiate the object
        MyClass obj = constructor.newInstance("arg");
        MyClass obj2 = constructor2.newInstance();
    }
}
