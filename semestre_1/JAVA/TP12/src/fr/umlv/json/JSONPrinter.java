package fr.umlv.json;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.RecordComponent;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class JSONPrinter {

  public static String toJSON(Alien alien) {
    return """
      {
        "age": %s,
        "planet": "%s"
      }
      """.formatted(alien.age(), alien.planet());
  }

  private static Object callAccessor(Record record, Method accessor) {
    try {
      return accessor.invoke(record);
    } catch (IllegalAccessException e) {
      throw new IllegalStateException("method not accessible", e);
    } catch (InvocationTargetException e) {
      var cause = e.getCause();
      if (cause instanceof RuntimeException exception) {
        throw exception;
      }
      if (cause instanceof Error error) {
        throw error;
      }
      throw new UndeclaredThrowableException(e);
    }
  }

  private static String escape(Object o) {
      return o instanceof String ? "\"" + o + "\"": "" + o;
  }

  private static String name(RecordComponent component) {
    var annotation = component.getAnnotation(JSONProperty.class);
    return (annotation == null || annotation.value().isEmpty()) ?
            component.getName().replace('_', '-')
            : annotation.value();
  }

  /*
  private static final ClassValue<RecordComponent[]> CACHE =
          new ClassValue<>() {
            @Override
            protected RecordComponent[] computeValue(Class<?> type) {
              return type.getRecordComponents();
            }
          };
  */

  private static final ClassValue<List<Function<Record, String>>> CACHE =
          new ClassValue<>() {
            @Override
            protected List<Function<Record, String>> computeValue(Class<?> type) {
            	var components = type.getRecordComponents();
            	return Arrays.stream(components)
                        .<Function<Record, String>>map(component -> {
                        	var name = name(component);
                        	var accessor = component.getAccessor();
                        	return record -> "\"" + name + "\": " + escape(callAccessor(record, accessor));
                        })
                        .toList();
            }
          };
  
  public static String toJSON(Record record) {
      var components = CACHE.get(record.getClass());
      return components
    		  .stream()
    		  .map(component -> component.apply(record))
              .collect(Collectors.joining(",", "{", "}"));
  }

  
  public static void main(String[] args) {
    var person = new Person("John", "Doe");
    System.out.println(toJSON(person));
    var alien = new Alien(100, "Saturn");
    System.out.println(toJSON(alien));
  }
}
   
