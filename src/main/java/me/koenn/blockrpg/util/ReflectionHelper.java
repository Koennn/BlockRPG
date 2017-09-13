package me.koenn.blockrpg.util;

import javax.annotation.Nullable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static me.koenn.blockrpg.BlockRPG.getLogger;

/**
 * Utility class containing all kinds of useful reflection related methods. Do not create an instance of this class,
 * only static methods need to be called.
 * <p>
 * Copyright (C) Koenn - All Rights Reserved Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential Written by Koen Willemse, May 2017
 */
@SuppressWarnings("unused")
public final class ReflectionHelper {

    private ReflectionHelper() {
    }

    /**
     * Get a <code>Class</code> object of the specified class name.
     *
     * @param className Name of the class you want to get
     * @return Class object
     */
    public static Class getClass(String className) {
        Class clazz = null;
        try {
            clazz = Class.forName(className);
        } catch (ClassNotFoundException e) {
            getLogger().info("Failed to get class '" + className + "'");
            e.printStackTrace();
        }
        return clazz;
    }

    /**
     * Create a new instance of a <code>Class</code> with specified arguments.
     *
     * @param clazz Class to instantiate
     * @param args  Constructor arguments
     * @return Instance of specified class
     */
    public static Object newInstance(Class clazz, @Nullable Object[] args) {
        try {
            if (args == null || args.length == 0) {
                return clazz.newInstance();
            }

            List<Class<?>> argTypes = new ArrayList<>();
            for (Object object : args) {
                argTypes.add(object.getClass());
            }
            Constructor<?> explicitConstructor = clazz.getConstructor(argTypes.toArray(new Class[argTypes.size()]));
            return explicitConstructor.newInstance(args);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * Call a method by name in a certain object instance with specified parameters.
     *
     * @param obj        Object instance to call the method in
     * @param methodName Name of the method to call
     * @param parameters Parameters for the method
     * @return Method return value
     */
    public static Object callMethod(Object obj, String methodName, Object... parameters) {
        Class<?>[] paramTypes = new Class[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            paramTypes[i] = parameters[i].getClass();
        }
        Method method;
        try {
            method = obj.getClass().getMethod(methodName, paramTypes);
            method.setAccessible(true);
        } catch (SecurityException | NoSuchMethodException e) {
            getLogger().info("Failed to get method '" + methodName + "' in class '" + obj.getClass().getName() + "'");
            return null;
        }
        Object value;
        try {
            value = method.invoke(obj, parameters);
        } catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
            getLogger().info("Failed to call method '" + methodName + "' in class '" + obj.getClass().getName() + "'");
            return null;
        }
        return value;
    }

    /**
     * Set a field by name in a certain object instance.
     *
     * @param obj       Object instance to set the field in
     * @param fieldName Name of the field to set
     * @param value     Value to set the field to
     */
    public static void setField(Object obj, String fieldName, Object value) {
        Field field;
        try {
            field = obj.getClass().getField(fieldName);
            field.setAccessible(true);
        } catch (NoSuchFieldException e) {
            getLogger().info("Failed to get field '" + fieldName + "' in class '" + obj.getClass().getName() + "'");
            return;
        }
        try {
            field.set(obj, value);
        } catch (IllegalAccessException e) {
            getLogger().info("Failed to set field '" + fieldName + "' in class '" + obj.getClass().getName() + "'");
        }
    }

    /**
     * Get a field's value by name in a certain object instance.
     *
     * @param obj       Object instance to get the field from
     * @param fieldName Name of the field
     * @return Value of the field
     */
    public static Object getField(Object obj, String fieldName) {
        Field field;
        try {
            field = obj.getClass().getField(fieldName);
            field.setAccessible(true);
        } catch (NoSuchFieldException e) {
            getLogger().info("Failed to get field '" + fieldName + "' in class '" + obj.getClass().getName() + "'");
            return null;
        }
        try {
            return field.get(obj);
        } catch (IllegalAccessException e) {
            getLogger().info("Failed to get value of field '" + fieldName + "' in class '" + obj.getClass().getName() + "'");
            return null;
        }
    }
}
