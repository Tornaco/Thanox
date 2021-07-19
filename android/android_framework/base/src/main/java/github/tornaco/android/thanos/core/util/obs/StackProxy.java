package github.tornaco.android.thanos.core.util.obs;

import android.annotation.SuppressLint;
import github.tornaco.android.thanos.core.util.Noop;
import lombok.AllArgsConstructor;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

@AllArgsConstructor
@SuppressLint("NewApi")

public class StackProxy<T> extends Stack<T> {
    private Stack<T> orig;

    @Override
    public T push(T t) {
        return orig.push(t);
    }

    @Override
    public T pop() {
        return orig.pop();
    }

    @Override
    public T peek() {
        return orig.peek();
    }

    @Override
    public boolean empty() {
        return orig.empty();
    }

    @Override
    public int search(Object o) {
        return orig.search(o);
    }

    @Override
    public void copyInto(Object[] objects) {
        orig.copyInto(objects);
    }

    @Override
    public void trimToSize() {
        orig.trimToSize();
    }

    @Override
    public void ensureCapacity(int i) {
        orig.ensureCapacity(i);
    }

    @Override
    public void setSize(int i) {
        orig.setSize(i);
    }

    @Override
    public int capacity() {
        return orig.capacity();
    }

    @Override
    public int size() {
        return orig.size();
    }

    @Override
    public boolean isEmpty() {
        return orig.isEmpty();
    }

    @Override
    public Enumeration<T> elements() {
        return orig.elements();
    }

    @Override
    public boolean contains(Object o) {
        return orig.contains(o);
    }

    @Override
    public int indexOf(Object o) {
        return orig.indexOf(o);
    }

    @Override
    public int indexOf(Object o, int i) {
        return orig.indexOf(o, i);
    }

    @Override
    public int lastIndexOf(Object o) {
        return orig.lastIndexOf(o);
    }

    @Override
    public int lastIndexOf(Object o, int i) {
        return orig.lastIndexOf(o, i);
    }

    @Override
    public T elementAt(int i) {
        return orig.elementAt(i);
    }

    @Override
    public T firstElement() {
        return orig.firstElement();
    }

    @Override
    public T lastElement() {
        return orig.lastElement();
    }

    @Override
    public void setElementAt(T t, int i) {
        orig.setElementAt(t, i);
    }

    @Override
    public void removeElementAt(int i) {
        orig.removeElementAt(i);
    }

    @Override
    public void insertElementAt(T t, int i) {
        orig.insertElementAt(t, i);
    }

    @Override
    public void addElement(T t) {
        orig.addElement(t);
    }

    @Override
    public boolean removeElement(Object o) {
        return orig.removeElement(o);
    }

    @Override
    public void removeAllElements() {
        orig.removeAllElements();
    }

    @Override
    public Object clone() {
        return orig.clone();
    }

    @Override
    public Object[] toArray() {
        return orig.toArray();
    }

    @Override
    public <T1> T1[] toArray(T1[] t1s) {
        return orig.toArray(t1s);
    }

    @Override
    public T get(int i) {
        return orig.get(i);
    }

    @Override
    public T set(int i, T t) {
        return orig.set(i, t);
    }

    @Override
    public boolean add(T t) {
        return orig.add(t);
    }

    @Override
    public boolean remove(Object o) {
        return orig.remove(o);
    }

    @Override
    public void add(int i, T t) {
        orig.add(i, t);
    }

    @Override
    public T remove(int i) {
        return orig.remove(i);
    }

    @Override
    public void clear() {
        orig.clear();
    }

    @Override
    public boolean containsAll(Collection<?> collection) {
        return orig.containsAll(collection);
    }

    @Override
    public boolean addAll(Collection<? extends T> collection) {
        return orig.addAll(collection);
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        return orig.removeAll(collection);
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
        return orig.retainAll(collection);
    }

    @Override
    public boolean addAll(int i, Collection<? extends T> collection) {
        return orig.addAll(i, collection);
    }

    @Override
    public boolean equals(Object o) {
        return orig.equals(o);
    }

    @Override
    public int hashCode() {
        return orig.hashCode();
    }

    @Override
    public String toString() {
        return orig.toString();
    }

    @Override
    public List<T> subList(int i, int i1) {
        return orig.subList(i, i1);
    }

    @Override
    public void removeRange(int i, int i1) {
        Noop.notSupported();
    }

    @Override
    public ListIterator<T> listIterator(int i) {
        return orig.listIterator(i);
    }

    @Override
    public ListIterator<T> listIterator() {
        return orig.listIterator();
    }

    @Override
    public Iterator<T> iterator() {
        return orig.iterator();
    }

    @Override
    public void forEach(Consumer<? super T> consumer) {
        orig.forEach(consumer);
    }

    @Override
    public boolean removeIf(Predicate<? super T> predicate) {
        return orig.removeIf(predicate);
    }

    @Override
    public void replaceAll(UnaryOperator<T> unaryOperator) {
        orig.replaceAll(unaryOperator);
    }

    @Override
    public void sort(Comparator<? super T> comparator) {
        orig.sort(comparator);
    }

    @Override
    public Spliterator<T> spliterator() {
        return orig.spliterator();
    }

    @Override
    public Stream<T> stream() {
        return orig.stream();
    }

    @Override
    public Stream<T> parallelStream() {
        return orig.parallelStream();
    }
}
