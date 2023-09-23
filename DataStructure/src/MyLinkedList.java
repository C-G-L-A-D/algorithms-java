import java.util.Iterator;

public class MyLinkedList {

//    private MyDoubleLinkedList<Integer> linkedList;
//    private MyArrayList<Integer> linkedList;
    private MyselfLinkedList<Integer> linkedList;

    public MyLinkedList() {
//        linkedList = new MyDoubleLinkedList<>();
//        linkedList = new MyArrayList<>(10);
        linkedList = new MyselfLinkedList<>();
    }

    public int get(int index) {
        return linkedList.get(index);
    }

    public void addAtHead(int val) {
        linkedList.addFirst(val);
    }

    public void addAtTail(int val) {
        linkedList.addLast(val);
    }

    public void addAtIndex(int index, int val) {
        linkedList.add(index, val);
    }

    public void deleteAtIndex(int index) {
        linkedList.remove(index);
    }

    public void printLinkedList() {
        Iterator<Integer> iterator = linkedList.iterator();
        while (iterator.hasNext()) {
            Integer element = iterator.next();
            System.out.println(element);
        }
    }

    public static void main(String[] args) {
        MyLinkedList linkedList1 = new MyLinkedList();

        linkedList1.addAtHead(10);
        linkedList1.addAtTail(20);
        linkedList1.addAtIndex(0, 15);
        linkedList1.printLinkedList();
    }
}
