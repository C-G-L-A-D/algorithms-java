import java.util.Iterator;
import java.util.NoSuchElementException;

public class MyDoubleLinkedList<E> {
    // 双链表节点
    private static class Node<E> {
        // 当前节点元素
        E val;
        // 下一个节点指针
        Node<E> next;

        // 上一个节点指针
        Node<E> prev;

        Node(E val) {
            this.val = val;
        }
    }

    // 定义头尾指针
    private final Node<E> head, tail;
    private int size;

    /***** 构造函数 *****/
    public MyDoubleLinkedList() {
        // 创建哨兵节点进行占位， 简化边界条件，防止对特殊条件的判断
        this.head = new Node<>(null);
        this.tail = new Node<>(null);

        head.next = head;
        tail.prev = tail;

        this.size = 0;
    }

    /***** 增 *****/

    /**
     * 在头部添加元素
     * @param element 添加的元素
     *
     * */
    public void addFirst(E element) {
        // 创建需要插入的节点
        Node<E> node = new Node<>(element);
        // 记录头指针的下一个节点
        Node<E> temp = head.next;

        // 将待插入节点的前后指针分别指向头节点和temp节点
        node.next = temp;
        node.prev = head;

        temp.prev = node;
        head.next = node;

        size++;
    }

    /**
     * 在尾部添加元素
     * @param element 添加的元素
     *
     * */
    public void addLast(E element) {
        // 创建需要插入的节点
        Node<E> node = new Node<>(element);
        // 记录头指针的上一个节点
        Node<E> temp = tail.prev;

        tail.prev = node;
        temp.next = node;

        size++;
    }

    /**
     * 在指定索引 index 位置添加元素
     * @param element 添加的元素
     *
     * */
    public void add(int index, E element) {
        checkPositionIndex(index);

        // 原： prevNode <=> temp
        // 改： prevNode <=> insertNode <=> temp
        Node<E> insertNode = new Node<>(element);
        Node<E> temp = getNode(index);
        Node<E> prevNode = temp.prev;

        insertNode.prev = prevNode;
        insertNode.next = temp;


        prevNode.next = insertNode;
        temp.prev = insertNode;

        size++;
    }


    /***** 删 *****/

    /**
     * 删除头节点元素
     *
     * */
    public E removeFirst() {
        if(isEmpty()) {
            throw new NoSuchElementException();
        }

        // head <=> removeNode <=> temp
        Node<E> removeNode = head.next;
        Node<E> temp = removeNode.next;

        head.next = temp;
        temp.prev = head;

        // 不用置空也可以，垃圾回收会把没有被指向的节点回收
        removeNode.prev = removeNode.next = null;
        size--;

        return removeNode.val;
    }

    /**
     * 删除尾节点元素
     *
     * */
    public E removeLast() {
        if(isEmpty()) {
            throw new NoSuchElementException();
        }

        // temp <=> removeNode <=> tail
        Node<E> removeNode = tail.prev;
        Node<E> temp = removeNode.prev;

        temp.next = tail;
        tail.prev = temp;

        // 不用置空也可以，垃圾回收会把没有被指向的节点回收
        removeNode.prev = removeNode.next = null;
        size--;

        return removeNode.val;
    }

    /**
     * 删除指定节点元素
     * @param index 删除的元素位置
     *
     * */
    public E remove(int index) {

        checkElementIndex(index);

        // 原： prevNode <=> removeNode <=> nextNode
        // 改： prevNode <=> nextNode
        Node<E> removeNode = getNode(index);
        Node<E> nextNode = removeNode.next;
        Node<E> prevNode = removeNode.prev;

        nextNode.prev = prevNode;
        prevNode.next = nextNode;

        removeNode.prev = removeNode.next = null;
        size--;

        return removeNode.val;
    }


    /***** 查 *****/

    /**
     * 获取头节点元素
     *
     * */
    public E getFirst() {
        if(isEmpty()) {
            throw new NoSuchElementException();
        }

        return head.next.val;
    }

    /**
     * 获取尾节点元素
     *
     * */
    public E getLast() {
        if(isEmpty()) {
            throw new NoSuchElementException();
        }

        return tail.prev.val;
    }

    /**
     * 获取指定节点元素
     * @param index 获取的节点位置
     *
     * */
    public E get(int index) {
        checkElementIndex(index);

        return getNode(index).val;
    }

    /***** 改 *****/
    /**
     * 修改指定索引 index 位置的元素，并返回原值
     * @param index 指定修改的节点索引
     * @param element 新的元素
     *
     * */
    public E set(int index, E element) {
        Node<E> node = getNode(index);

        E oldVal = node.val;
        node.val = element;

        return oldVal;
    }

    /***** 工具函数 *****/

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }


    private  boolean isElementIndex(int index) {
        return index >= 0 && index < size;
    }

    private  boolean isPositionIndex(int index) {
        // 元素间，包含数组的头尾
        return index >=0 && index <= size;
    }

    // 检查 index 索引位置是否可以存在元素
    private void checkElementIndex(int index) {
        if(!isElementIndex(index)) {
            throw new IndexOutOfBoundsException(
                    "Index" + index + ", Size: " + size);
        }
    }

    // 检查 index 索引位置是否可以添加元素
    private void checkPositionIndex(int index) {
        if(!isPositionIndex(index)) {
            throw new IndexOutOfBoundsException(
                    "Index" + index + ", Size: " + size);
        }
    }

    // 返回 index 对应的 Node 节点
    private Node<E> getNode(int index) {
        // 链表中第一个元素
        Node<E> firstNode = head.next;

        // TODO: 待完善
        for (int i = 0; i < index; i++) {
            firstNode = firstNode.next;
        }

        return firstNode;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<>() {
            Node<E> p = head.next;

            @Override
            public boolean hasNext() {
                return p != tail;
            }

            @Override
            public E next() {
                E val = p.val;
                p = p.next;
                return val;
            }
        };
    }
}
