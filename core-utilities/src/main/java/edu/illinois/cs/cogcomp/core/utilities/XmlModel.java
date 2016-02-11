package edu.illinois.cs.cogcomp.core.utilities;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Accepts JAXB compatible model classes and performs the IO/parsing. All operations are
 * thread-safe. You can choose either use the static method to load or extend this class to utilize
 * the protected parsing methods.
 *
 * @author cheng88
 */
public abstract class XmlModel {

    private static ConcurrentMap<Class<?>, JAXBContext> contextCache = new ConcurrentHashMap<>();

    private static JAXBContext getContext(Class<?> clazz) throws JAXBException {
        JAXBContext ret = contextCache.get(clazz);
        if (ret == null) {
            final JAXBContext newVal = JAXBContext.newInstance(clazz);
            ret = contextCache.putIfAbsent(clazz, newVal);
            if (ret == null) {
                ret = newVal;
            }
        }
        return ret;
    }

    /**
     * Unmarshallers are not thread safe, thus one per thread is required
     */
    private static ThreadLocal<Map<Class<?>, Unmarshaller>> unmarshallers =
            new ThreadLocal<Map<Class<?>, Unmarshaller>>() {
                @Override
                public Map<Class<?>, Unmarshaller> initialValue() {
                    return new HashMap<>();
                }
            };

    private static Unmarshaller getUnmarshaller(Class<?> clazz) throws JAXBException {
        Map<Class<?>, Unmarshaller> localPool = unmarshallers.get();
        if (!localPool.containsKey(clazz)) {
            localPool.put(clazz, getContext(clazz).createUnmarshaller());
        }
        return localPool.get(clazz);
    }

    /**
     * Marshals an object
     */
    public static void write(Object o, String filename) throws JAXBException {
        write(o, new File(filename));
    }

    public static void write(Object o, File file) throws JAXBException {
        getMarshaller(o).marshal(o, file);
    }

    public static String toXmlString(Object o) throws Exception {
        StringWriter w = new StringWriter();
        getMarshaller(o).marshal(o, w);
        return w.toString();
    }

    private static Marshaller getMarshaller(Object o) throws JAXBException {
        Marshaller m = getContext(o.getClass()).createMarshaller();
        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        m.setProperty("jaxb.encoding", "UTF-8");
        return m;
    }

    @SuppressWarnings("unchecked")
    public static <T> T load(Class<T> clazz, String filename) {
        try {
            return (T) getUnmarshaller(clazz).unmarshal(
                    new InputStreamReader(new FileInputStream(filename), "UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public static <T> T load(Class<T> clazz, File file) {
        try {
            return (T) getUnmarshaller(clazz).unmarshal(
                    new InputStreamReader(new FileInputStream(file), "UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void save(File file) {
        try {
            write(this, file.getAbsolutePath());
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }


}
