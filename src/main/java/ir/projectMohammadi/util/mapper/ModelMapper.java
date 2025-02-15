package ir.projectMohammadi.util.mapper;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ir.projectMohammadi.model.baseModel.Base;
import ir.projectMohammadi.util.QueryResult.QueryResult;
import ir.projectMohammadi.util.enumEntryDescriptor.EnumEntryDescriptor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class ModelMapper {

    private static final Logger LOGGER = LogManager.getLogger(ModelMapper.class);
    private static Mapper mapper;

    public ModelMapper() {
        ModelMapper.mapper = new DozerBeanMapper();
    }

    public static Mapper getMapper() {
        if (mapper != null)
            return mapper;

        try {
            mapper = new DozerBeanMapper();
            LOGGER.info("Not using Spring Dozer Bean Mapper, creating a new instance.");
        } catch (Exception ex) {
            LOGGER.error("Error creating DozerBeanMapper instance: ", ex);
            mapper = null;
        }
        return mapper;
    }


    public static <T, U> U map(T source, final Class<U> destType) {
        return map(source, destType, true);
    }

    public static <T, U> U map(T source, final Class<U> destType, Boolean isSetNullModels) {
        if (source != null) {
            U dest = getMapper().map(source, destType);
            if (isSetNullModels) {
                setNullEntity(dest);
            }
            return dest;
        } else {
            return null;
        }
    }

    public static <T> void setNullEntity(T source) {
        try {
            List<Field> fields = getAllFields(source);

            for (Field field : fields) {
                field.setAccessible(true);
                Object innerObject = field.get(source);

                if (innerObject instanceof Base<?>) {
                    Method[] methods = field.getType().getMethods();
                    for (Method method : methods) {
                        if (method.getName().contentEquals("getId")) {
                            Object invokeMethod = method.invoke(innerObject);

                            if (invokeMethod == null) {
                                field.set(source, null);
                            } else if (invokeMethod instanceof Integer) {
                                Integer intValue = (Integer) invokeMethod;
                                if (intValue == -1) {
                                    field.set(source, null);
                                }
                            } else if (invokeMethod instanceof Long) {
                                Long longValue = (Long) invokeMethod;
                                if (longValue == -1L) {
                                    field.set(source, null);
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.info(e.getMessage());
        }
    }

    private static <T> List<Field> getAllFields(T source) {
        List<Field> fields = new ArrayList<>();
        for (Field field : source.getClass().getDeclaredFields()) {
            fields.add(field);
        }
        if (source.getClass().getSuperclass() != null) {
            for (Field field : source.getClass().getSuperclass().getDeclaredFields())
                fields.add(field);
        }
        return fields;
    }

    public static <T> List<EnumEntryDescriptor> getEnumKeyValue(Class<T> clazz) {

        List<EnumEntryDescriptor> result = new ArrayList<>();
        T[] enumConstants = clazz.getEnumConstants();
        for (T t : enumConstants) {
            EnumEntryDescriptor entry = new EnumEntryDescriptor();
            boolean enable = true;
            try {
                Method enableMethod = t.getClass().getMethod("isEnable", null);
                enable = (Boolean) enableMethod.invoke(t);
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                enable = true;
            }
            if (enable) {
                for (Method method : t.getClass().getMethods()) {
                    try {
                        if (method.getName().contentEquals("ordinal")) {
                            entry.setIndex((Integer) method.invoke(t));
                        } else if (method.getName().contentEquals("name")) {
                            entry.setTitle((String) method.invoke(t));
                        } else if (method.getName().toLowerCase().contains("persianTitle")) {
                            entry.setPersianTitle((String) method.invoke(t));
                        }
                    } catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }
            result.add(entry);
        }
        return result;
    }

    public static <T, U> List<U> mapList(final List<T> source, final Class<U> destType) {
        final List<U> dest = new ArrayList<>();
        for (T element : source) {
            dest.add(getMapper().map(element, destType));
        }
        return dest;
    }

    public static <T, U> List<U> mapList(final List<T> source, final Class<U> destType, final Boolean isSetNullModels) {
        final List<U> dest = new ArrayList<>();
        for (T element : source) {
            dest.add(map(element, destType, isSetNullModels));
        }
        return dest;
    }

    public static <T,U> Set<U> mapSet(final List<T> source, final Class<U> destType) {
     final Set<U> dest = new HashSet<>();
     for (T element : source){
         dest.add(getMapper().map(element, destType));
     }
     return dest;
    }

    public static <T,U> Set<U> mapSet(final List<T> source, final Class<U> destType, final Boolean isSetNullModels) {
        final Set<U> dest = new HashSet<>();
        for (T element : source) {
            dest.add(map(element, destType, isSetNullModels));
        }
        return dest;
    }

    public static <T,U> QueryResult<U> mapQueryResult(final QueryResult<T> source, final Class<U> destType) {
        final List<U> dest = new ArrayList<>();
        for (T element : source.getEntityList()){
            dest.add(getMapper().map(element, destType));
        }
        QueryResult<U> result = new QueryResult<U>(source.getPageNumber(),source.getTotalRecords(),source.getPageSize(),dest);
        return result;
    }
    public static <T> T convertJsonToObject(String jsonData, Class<T> convertedClass){
        try {
            return (T) new ObjectMapper().readValue(jsonData,convertedClass);
        }catch (JsonParseException | JsonMappingException e){
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

    public static String convertObjectToJson(Object object){
        try {
            return new ObjectMapper().writeValueAsString(object);
        }catch (JsonParseException | JsonMappingException e){
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

}

