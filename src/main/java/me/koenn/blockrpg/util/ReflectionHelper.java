package me.koenn.blockrpg.util;

import me.koenn.blockrpg.BlockRPG;

import javax.annotation.Nullable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public final class ReflectionHelper {

    private ReflectionHelper() {
    }

    public static Class getClass(String className) {
        Class clazz = null;
        try {
            clazz = Class.forName(className);
        } catch (ClassNotFoundException e) {
            BlockRPG.LOGGER.info("Failed to get class '" + className + "'");
            e.printStackTrace();
        }
        return clazz;
    }

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
            BlockRPG.LOGGER.info("Failed to get method '" + methodName + "' in class '" + obj.getClass().getName() + "'");
            return null;
        }
        Object value;
        try {
            value = method.invoke(obj, parameters);
        } catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
            BlockRPG.LOGGER.info("Failed to call method '" + methodName + "' in class '" + obj.getClass().getName() + "'");
            return null;
        }
        return value;
    }

    public static void setField(Object obj, String fieldName, Object value) {
        Field field;
        try {
            field = obj.getClass().getField(fieldName);
            field.setAccessible(true);
        } catch (NoSuchFieldException e) {
            BlockRPG.LOGGER.info("Failed to get field '" + fieldName + "' in class '" + obj.getClass().getName() + "'");
            return;
        }
        try {
            field.set(obj, value);
        } catch (IllegalAccessException e) {
            BlockRPG.LOGGER.info("Failed to set field '" + fieldName + "' in class '" + obj.getClass().getName() + "'");
        }
    }

    public static Object getField(Object obj, String fieldName) {
        Field field;
        try {
            field = obj.getClass().getField(fieldName);
            field.setAccessible(true);
        } catch (NoSuchFieldException e) {
            BlockRPG.LOGGER.info("Failed to get field '" + fieldName + "' in class '" + obj.getClass().getName() + "'");
            return null;
        }
        try {
            return field.get(obj);
        } catch (IllegalAccessException e) {
            BlockRPG.LOGGER.info("Failed to get value of field '" + fieldName + "' in class '" + obj.getClass().getName() + "'");
            return null;
        }
    }
}
