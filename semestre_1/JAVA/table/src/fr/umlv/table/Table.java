package fr.umlv.table;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

public class Table <T> {
    private final List<T> elements;

     public static class Group <T, E> {
        private final TreeMap<E, ArrayList<Integer>> indexes;

        public Group(TreeMap<E, ArrayList<Integer>> indexes) {
            this.indexes = indexes;
        }

        public static <T,E> Group<T,E> createGroup(List<T> elements, Function<? super T, E> function, Comparator<E> comparator) {
            var indexes = new TreeMap<E, ArrayList<Integer>>(comparator);
            for(var element : elements) {
                indexes.computeIfAbsent(function.apply(element),
                        value -> new ArrayList<>()).add(elements.indexOf(element));
            }
            return new Group<>(indexes);
        }

        public int keySize() {
            return indexes.size();
        }

         @Override
         public String toString() {
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

    public <E> Group<T,E> groupBy(Function<? super T, E> function, Comparator<E> comparator) {
        Objects.requireNonNull(function);
        Objects.requireNonNull(comparator);
        return Group.createGroup(elements, function, comparator);
    }

    public static <E> Table<E> dynamic() {
        return new Table<>(Arrays.asList());
    }

    public void add(T element) {
         Objects.requireNonNull(element);
         elements.add(element);
    }
}
