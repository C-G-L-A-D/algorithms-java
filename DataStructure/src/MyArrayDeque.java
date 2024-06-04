/**
 * 底层使用数组实现的双端队列
 * @param <E>
 */
public class MyArrayDeque<E> {

    private int size;
    private E[] data;
    // 默认初始化大小
    private final static int INIT_CAP = 2;

    private int first, last;

    /**
     * 初始化指定大小的双端队列
     * @param initCap 初始化大小
     */
    public MyArrayDeque(int initCap) {
        data = (E[]) new Object[initCap];
        size = 0;
        // [0, 0) 目前区间为空集
        first = last = 0;
    }

    /**
     * 默认初始化双端队列
     */
    public MyArrayDeque() {
        this(INIT_CAP);
    }

    /********** 增 **********/

    /**
     * 在对头插入元素
     * @param e 元素
     */
    public void addFirst(E e) {

    }

    /**
     * 在对尾插入元素
     * @param e 元素
     */
    public void addLast(E e) {

    }

    /********** 删 **********/

    /**
     * 删除对头元素
     * @return 对头元素
     */
    public E removeFirst() {

    }

    /**
     * 删除对尾元素
     * @return 对尾元素
     */
    public E removeLast() {

    }

    /********** 查 **********/
    /**
     * 获取对头元素
     * @return 对头元素
     */
    public E getFirst() {

    }

    /**
     * 获取对尾元素
     * @return 对尾元素
     */
    public E getLast() {

    }

    /********** 改 **********/

    /********** 工具函数 **********/

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * 扩容/锁容
     * @param newCap
     */
    private void resize(int newCap) {
        E[] temp = (E[]) new Object[newCap];

        for(int i = 0; i < newCap; i++) {
            // 迁移对头后 newCap 位的元素
            temp[i] = data[(first + i) % data.length];
        }

        first = 0;
        last = newCap;
        data = temp;
    }

}
