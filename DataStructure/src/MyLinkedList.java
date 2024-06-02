import java.util.Iterator;

public class MyLinkedList<E> {

    private MyDoubleLinkedList<E> linkedList;
//    private MyArrayList<Integer> linkedList;
//    private MyselfLinkedList<Integer> linkedList;

    public MyLinkedList() {
        linkedList = new MyDoubleLinkedList<>();
//        linkedList = new MyArrayList<>(10);
//        linkedList = new MyselfLinkedList<>();
    }

    public E get(int index) {
        return linkedList.get(index);
    }

    public void addAtHead(E val) {
        linkedList.addFirst(val);
    }

    public void addAtTail(E val) {
        linkedList.addLast(val);
    }

    public void addAtIndex(int index, E val) {
        linkedList.add(index, val);
    }

    public void deleteAtIndex(int index) {
        linkedList.remove(index);
    }

    public E deleteTail() {
        return linkedList.removeLast();
    }

    public E deleteHead() {
        return linkedList.removeFirst();
    }

    public void printLinkedList() {
        Iterator<E> iterator = linkedList.iterator();
        while (iterator.hasNext()) {
            E element = iterator.next();
            System.out.println(element);
        }
    }

    public int size() {
        return linkedList.size();
    }

    public static void main(String[] args) {
        MyLinkedList<Integer> linkedList1 = new MyLinkedList<>();

        linkedList1.addAtHead(10);
        linkedList1.addAtTail(20);
        linkedList1.addAtTail(30);
        linkedList1.addAtTail(40);
        linkedList1.addAtTail(50);
        linkedList1.addAtIndex(3, 15);
        linkedList1.printLinkedList();
    }
}
