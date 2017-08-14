package lib;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class CopyUtil {
    public static Object deepCopy(Object obj) {
        try {
            Class<? extends Object> cls = obj.getClass();
            Object clone = cls.newInstance();

            Field[] fields = cls.getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                Field field = fields[i];
                field.setAccessible(true);
                if (!Modifier.isFinal(field.getModifiers())) {
                    if (field.getType().isPrimitive()) {
                        field.set(clone, field.get(obj));
                    } else {
                        field.set(clone, deepCopyObject(field.get(obj)));
                    }
                }
            }

            while (true) {
                cls = cls.getSuperclass();
                if (Object.class.equals(cls)) {
                    break;
                }
                Field[] sFields = cls.getDeclaredFields();
                for (int i = 0; i < sFields.length; i++) {
                    Field field = sFields[i];
                    if (!Modifier.isFinal(field.getModifiers())) {
                        field.setAccessible(true);
                        if (field.getType().isPrimitive()) {
                            field.set(clone, field.get(obj));
                        } else {
                            field.set(clone, deepCopyObject(field.get(obj)));
                        }
                    }
                }
            }
            return clone;
        } catch (Exception e) {
            return null;
        }
    }

    public static Object deepCopyObject(Object obj) {
        ByteArrayOutputStream baos = null;
        ObjectOutputStream oos = null;
        ObjectInputStream ois = null;
        Object ret = null;

        try {
            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            oos.writeObject(obj);
            ois = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()));
            ret = ois.readObject();
        } catch (Exception e) {
        } finally {
            try {
                if (oos != null) {
                    oos.close();
                }
                if (ois != null) {
                    ois.close();
                }
            } catch (IOException e) {
            }
        }

        return ret;
    }

    public static void main(String[] args){
		double[][] a1=new double[2][2],a2;
		a2=(double [][])CopyUtil.deepCopyObject(a1);
		a1[0][0]=1.0;
		a1[0][1]=1.0;
		System.out.println(a1[0][0]+" "+a1[0][1]+" "+a1[1][0]+" "+a1[1][1]);
		System.out.println(a2[0][0]+" "+a2[0][1]+" "+a2[1][0]+" "+a2[1][1]);
	}
}