import java.util.Collection;
import java.util.Iterator;
import java.util.Queue;

/**
 * @author luo ad
 * @description 底层使用链表手动实现队列（先进显出）
 * @param <E> 队列的元素类型
 */
public class MyQueue<E> {

    // 使用之前实现的链表来存储数据
    private MyLinkedList<E> list = new MyLinkedList<>();

    /**
     * 队尾插入元素
     * @param element 添加的元素
     */
    public void enqueue(E element) {
        list.addAtTail(element);
    }

    /**
     * 对头弹出元素
     * @return 对头元素
     */
    public E dequeue() {
        return list.deleteHead();
    }

    /**
     * 获取对头元素
     * @return 对头元素
     */
    public E peek() {
        return list.get(0);
    }

    public void print() {
        list.printLinkedList();
    }

    public static void main(String[] args) {
        MyQueue<Integer> queue = new MyQueue<>();

        queue.enqueue(10);
        queue.enqueue(9);
        queue.enqueue(8);
        queue.enqueue(7);
        queue.enqueue(6);
        queue.dequeue();
        queue.dequeue();
        queue.enqueue(5);
        queue.print();
        System.out.println("队列当前第一个元素是 " + queue.peek());
    }
}
