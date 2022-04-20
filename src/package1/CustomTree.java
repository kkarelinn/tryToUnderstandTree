package com.javarush.task.task20.task2028;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

/*
Построй дерево(1)
*/

public class CustomTree extends AbstractList<String> implements Cloneable, Serializable{
    Entry<String> root;

    private transient ArrayList<Entry<String>> totalList;

    public CustomTree() {
        this.root = new Entry<String>(null);
    }
    /*
    1) переопределить метод add(String s) - добавляет элементы дерева,
    в качестве параметра принимает имя элемента (elementName), искать место для вставки начинаем слева направо.
     */

    /**
     * Метод add добавляет элемент в дерево. Сначала устанавливается полная коллекция элементов, потом происходит
     * итерация по элементам. При итерации каждый элемент проверяется методом checkChildren на возможность иметь
     * ветви. isAvailableToAddChildren() возвращает true если такая возможность имеется; После чего создается и
     * инициализируется новый элемент Entry<String>, добавляется в коллекцию. После чего коллекция заного
     * проверяется и инициализируется с помощью setValidCollection();
     *
     * @param s строка (String) которую необходимо добавить в коллекцию;
     * @return true после добавления нового элемента;
     */
    @Override
    public boolean add(String s) {
        setUpCollection(root);
        for (Entry<String> entry : totalList) {

            if(!entry.wasDel){
                entry.checkChildren();
                if (entry.isAvailableToAddChildren()) {
                    createChild(entry, s);
                    setValidCollection();
                    return true;
                }

            }

        }

        return false;
    }
    /**
     * Метод setValidCollection записывает полную коллекцию элементов дерева, а затем удаляет 1 элемент коллекции,
     * который является изначальным корнем.
     * Используется в методах remove и т.д. для валидного прохода по коллекции.
     */
    private void setValidCollection() {
        setUpCollection(root);
        totalList.remove(0);
    }

    /**
     * Метод createChild создает новый элемент Entry<String> и устанавливает значение переменной parent
     *
     * @param parent родительский элемент Entry<String>;
     * @param s      значение elementName для нового элемента Entry<String>;
     */
    private void createChild(Entry<String> parent, String s) {
        Entry<String> newOne = new Entry<>(s);
        newOne.parent = parent;
        setChild(parent, newOne);
    }

    /**
     * Метод setChild присваивает переменным left/rightChild родителя ссылку на элемент Entry<String> child;
     * Если переменная newLineRootElement родителя имела значение true, то это значение передастся ребенку, у
     * родителя изменится на false;
     *
     * @param parent родительский элемент Entry<String>
     * @param child  элемент-потомок Entry<String>
     */
    private void setChild(Entry<String> parent, Entry<String> child) {
        if (parent.availableToAddLeftChildren) {
            parent.leftChild = child;
            parent.availableToAddLeftChildren = false;

        } else {
            parent.rightChild = child;
            parent.availableToAddRightChildren = false;
        }
    }

    @Override
    public boolean remove(Object o) {
        if ((o instanceof String)){
            String name = (String) o;
            setValidCollection();
            Entry<String> entry = totalList.stream().filter(e->e.elementName.equals(name)).findAny().orElse(null);
            if (entry!=null){
                Entry<String> parent = entry.parent;
                if (parent.leftChild==entry){
                    parent.leftChild=null;
                    parent.availableToAddLeftChildren = true;
                    parent.wasDel=true;
                }
                else {
                    parent.rightChild=null;
                    parent.availableToAddRightChildren = true;
                }
                entry = null;
                setUpCollection(root);
                setValidCollection();
                return  true;
            }
        }
        else throw new UnsupportedOperationException();

        return false;
    }

    /**
     * Метод setUpCollection проходит по дереву, начиная с элемента Entry<String> root и перезаписывает все элементы в
     * queue;
     *
     * @param root начальный элемент Entry<String> для вертикального прохода по дереву.
     */
    private void setUpCollection(Entry<String> root) {
        totalList = new ArrayList<>();
        Queue<Entry<String>> queue = new LinkedList<>();
        queue.add(root);

        while (!queue.isEmpty()) {
            Entry<String> entry = queue.remove();
            totalList.add(entry);

            if (entry.leftChild != null) {
                queue.add(entry.leftChild);
            }

            if (entry.rightChild != null) {
                queue.add(entry.rightChild);
            }
        }
    }

    @Override
    public String set(int index, String element) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void add(int index, String element) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String remove(int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(int index, Collection<? extends String> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<String> subList(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void removeRange(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String get(int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int size() {
//        setValidCollection();
        return totalList.size();
    }

    public String getParent(String s) {
        setValidCollection();
        Entry<String> entry = totalList.stream().filter(e->e.elementName.equals(s)).findFirst().orElse(null);
        return entry!=null ? entry.parent.elementName : "null";
    }

    static class Entry<T> implements Serializable{
        String elementName;
        boolean availableToAddLeftChildren;
        boolean availableToAddRightChildren;
        boolean wasDel;
        Entry<T> parent;
        Entry<T> leftChild;
        Entry<T> rightChild;

        public Entry(String elementName) {
            this.elementName = elementName;
            this.availableToAddRightChildren = true;
            this.availableToAddLeftChildren = true;
        }

        public boolean isAvailableToAddChildren(){
            return this.availableToAddLeftChildren || this.availableToAddRightChildren;
        }

        void checkChildren() {
            if (this.leftChild != null) {
                availableToAddLeftChildren = false;
            }
            if (this.rightChild != null) {
                availableToAddRightChildren = false;
            }
        }

    }
}
