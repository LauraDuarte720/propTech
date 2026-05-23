package co.edu.uniquindio.com.proptech.mappers.structuresMappers;

import co.edu.uniquindio.com.proptech.mappers.MapperOnlyDto;
import co.edu.uniquindio.com.proptech.structures.AVLTree.AVLTree;
import co.edu.uniquindio.com.proptech.structures.arrayList.ArrayList;
import co.edu.uniquindio.com.proptech.structures.hashTable.HashTable;
import co.edu.uniquindio.com.proptech.structures.linkedList.LinkedList;
import co.edu.uniquindio.com.proptech.structures.priorityQueue.PriorityQueue;
import co.edu.uniquindio.com.proptech.structures.queue.Queue;
import co.edu.uniquindio.com.proptech.structures.stack.Stack;

import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class StructuresMappers {

    public <E, D> List<D> fromArrayList(ArrayList<E> source, MapperOnlyDto<E, D> mapper) {
        List<D> result = new java.util.ArrayList<>();
        for (E element : source) {
            result.add(mapper.toDto(element));
        }
        return result;
    }

    public <E, D> List<D> fromLinkedList(LinkedList<E> source, MapperOnlyDto<E, D> mapper) {
        List<D> result = new java.util.ArrayList<>();
        for (E element : source) {
            result.add(mapper.toDto(element));
        }
        return result;
    }

    public <E extends Comparable<E>, D> List<D> fromAVLTree(AVLTree<E> source, MapperOnlyDto<E, D> mapper) {
        List<D> result = new java.util.ArrayList<>();
        for (E element : source.inOrder()) {
            result.add(mapper.toDto(element));
        }
        return result;
    }

    public <E, D> List<D> fromStack(Stack<E> source, MapperOnlyDto<E, D> mapper) {
        List<D> result = new java.util.ArrayList<>();
        for (E element : source.toJavaList()) {
            result.add(mapper.toDto(element));
        }
        return result;
    }

    public <E, D> List<D> fromQueue(Queue<E> source, MapperOnlyDto<E, D> mapper) {
        List<D> result = new java.util.ArrayList<>();
        for (E element : source.toJavaList()) {
            result.add(mapper.toDto(element));
        }
        return result;
    }

    public <E, D> List<D> fromPriorityQueue(PriorityQueue<E> source, MapperOnlyDto<E, D> mapper) {
        List<D> result = new java.util.ArrayList<>();
        for (E element : source.toJavaList()) {
            result.add(mapper.toDto(element));
        }
        return result;
    }

    public <K, V, D> List<D> fromHashTableValues(HashTable<K, V> source, MapperOnlyDto<V, D> mapper) {
        List<D> result = new java.util.ArrayList<>();
        for (V element : source.valuesToJavaList()) {
            result.add(mapper.toDto(element));
        }
        return result;
    }

    public <K, V> java.util.Map<String, V> fromHashTableToMap(HashTable<K, V> source) {
        java.util.Map<String, V> result = new java.util.HashMap<>();
        List<K> keys = source.keysToJavaList();
        for (K key : keys) {
            result.put(key.toString(), source.get(key));
        }
        return result;
    }

    public <K, V, D> List<D> fromHashTableToList(HashTable<K, V> source,
                                                 java.util.function.BiFunction<K, V, D> mapper) {
        List<D> result = new java.util.ArrayList<>();
        for (K key : source.keysToJavaList()) {
            result.add(mapper.apply(key, source.get(key)));
        }
        return result;
    }
}