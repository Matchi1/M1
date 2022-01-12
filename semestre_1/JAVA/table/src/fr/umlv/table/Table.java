package fr.umlv.table;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Table <T> {
    private final List<T> elements;

     public class Group <E> {
        private final TreeMap<E, ArrayList<Integer>> indexes;
        private final Function<? super T, E> function;

        private void refresh() {
            for(var element : elements) {
                var list = indexes.computeIfAbsent(function.apply(element),
                        value -> new ArrayList<>());
                var index = elements.indexOf(element);
                if(!list.contains(index)) {
                    list.add(index);
                }
            }
        }

        public Group(Function<? super T, E> function, Comparator<? super E> comparator) {
            var indexes = new TreeMap<E, ArrayList<Integer>>(comparator);
            for(var element : elements) {
                indexes.computeIfAbsent(function.apply(element),
                        value -> new ArrayList<>()).add(elements.indexOf(element));
            }
            this.indexes = indexes;
            this.function = function;
        }

        public int keySize() {
            refresh();
            return indexes.size();
        }

         public void forEach(Consumer<? super T> consumer) {
             refresh();
             indexes.values()
                     .stream()
                     .flatMap(array -> array.stream().map(elements::get))
                     .forEach(consumer);
         }

         public List<T> lookup(E key) {
            Objects.requireNonNull(key);
            if(indexes.containsKey(key)){
                return indexes.get(key).stream().map(elements::get).toList();
            }
            return Collections.unmodifiableList(new ArrayList<>());
         }

         @Override
         public String toString() {
            refresh();
            var joiner = new StringJoiner("\n");
            for(var items : indexes.entrySet()) {
                joiner.add(items.getKey().toString() + ": " + items.getValue().toString());
            }
            return joiner.toString();
         }
     }

    private Table(List<T> elements) {
        this.elements = elements;
    }

    public static <E> Table<E> of(E... elements) {
        Objects.requireNonNull(elements);
        return new Table<>(List.of(elements));
    }

    public int size() {
        return elements.size();
    }

    public <E> Group<E> groupBy(Function<? super T, E> function, Comparator<? super E> comparator) {
        Objects.requireNonNull(function);
        Objects.requireNonNull(comparator);
        return new Group<>(function, comparator);
    }

    public static <E> Table<E> dynamic() {
        return new Table<>(new ArrayList<>());
    }

    public void add(T element) {
         Objects.requireNonNull(element);
         elements.add(element);
    }
}
