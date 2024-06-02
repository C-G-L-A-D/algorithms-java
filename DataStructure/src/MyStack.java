/**
 * @author luo ad
 * @description 底层使用链表手动实现栈（先进后出）
 */
public class MyStack<E> {

    private MyLinkedList<E> list = new MyLinkedList();

    /**
     * 栈尾添加元素
     * @param e 元素
     */
    public void push(E e) {
        list.addAtTail(e);
    }

    /**
     * 栈尾弹出元素
     * @return
     */
    public E pop() {
        return list.deleteTail();
    }

    /**
     * 获取栈头第一个元素
     * @return 元素
     */
    public E peek() {
        return list.get(0);
    }

    /**
     * 输出当前栈列元素
     */
    public void print() {
        list.printLinkedList();
    }

    public static void main(String[] args) {
        MyStack<String> stack = new MyStack<>();
        stack.push("我家在天津");
        stack.push("骗你玩的");
        stack.push("我滴家在东北");
        stack.push("松花江上啊~");
        stack.pop();
        stack.push("吉林大学边");
        stack.pop();
        stack.print();
        System.out.printf("当前栈头是 " + stack.peek());
    }
}
