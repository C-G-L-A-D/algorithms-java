import java.util.NoSuchElementException;

/**
 * 底层使用数组实现的双端队列
 * @param <E>
 */
public class MyArrayDeque<E> {

    private int size;
    private E[] data;
    // 默认初始化大小
    private final static int INIT_CAP = 2;

    // 队列有效元素位置为 [first, last)
    private int first, last;

    /**
     * 初始化指定大小的双端队列
     * @param initCap 初始化大小
     */
    public MyArrayDeque(int initCap) {
        data = (E[]) new Object[initCap];
        size = 0;
        //  默认此时队列为 [0, 0) 目前区间为空集，
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
        // 添加元素需要判断队列大小是否需要扩容
        if(size == data.length) {
            // 扩容两倍
            resize(data.length * 2);
        }
        // 如果 first 往前移到索引 0 的位置后还需要前进，则转从队尾开始（逻辑上模拟成环状）
        first = first == 0 ? data.length - 1 : first - 1;
        // 更改当前元素位置
        data[first] = e;

        // 元素个数增加
        size++;
    }

    /**
     * 在对尾插入元素
     * @param e 元素
     */
    public void addLast(E e) {
        // 添加元素需要判断队列大小是否需要扩容
        if(size == data.length) {
            // 扩容两倍
            resize(data.length * 2);
        }
        // 由于 last 初始值为 0，默认双端队列区间为 [0, 0)。因此队尾添加元素时需要先添加，在移动指针
        data[last] = e;
        last = last == data.length - 1 ? 0 : last + 1;

        // 元素个数增加
        size++;
    }

    /********** 删 **********/

    /**
     * 删除对头元素
     * @return 对头元素
     */
    public E removeFirst() {
        // 删除需要判断队列是否为空
        if(isEmpty()) {
            // 为空抛出异常
            throw new NoSuchElementException();
        }


        // 删除元素时，如果队列内元素个数不超过队列大小的四分之一，可以进行缩容
        if(size <= data.length / 4) {
            resize(data.length / 2);
        }

        // 提前记录对头元素
        E temp = data[first];

        // first 指针前移表示删除元素（原对头元素是否置空都可以，但队列有效元素位置为 [first, last) ）
        data[first] = null;
        first = first == data.length - 1 ? 0 : first + 1;

        // 元素个数减少
        size--;

        return temp;
    }

    /**
     * 删除对尾元素
     * @return 对尾元素
     */
    public E removeLast() {
        // 删除需要判断队列是否为空
        if(isEmpty()) {
            // 为空抛出异常
            throw new NoSuchElementException();
        }

        // 删除元素时，如果队列内元素个数不超过队列大小的四分之一，可以进行缩容
        if(size <= data.length / 4) {
            resize(data.length / 2);
        }

        // last 前移
        last = last == 0 ? data.length - 1 : last - 1;
        // 因为双端队列的区间范围为 [first, last) ，last当前指向的位置不在有效范围中，是原来的对尾元素
        E temp = data[last];
        data[last] = null;
        // 元素个数减少
        size--;

        return temp;
    }

    /********** 查 **********/
    /**
     * 获取对头元素
     * @return 对头元素
     */
    public E getFirst() {
        // 查找需要判断队列是否为空
        if(isEmpty()) {
            // 为空抛出异常
            throw new NoSuchElementException();
        }

        return data[first];
    }

    /**
     * 获取对尾元素
     * @return 对尾元素
     */
    public E getLast() {
        // 查找需要判断队列是否为空
        if(isEmpty()) {
            // 为空抛出异常
            throw new NoSuchElementException();
        }

        int index = last == 0 ? data.length - 1 : last - 1;
        return data[index];
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


    public void print() {
        for(int i = first; i != last; i = (i + 1) % data.length) {
            System.out.println("i = " + i + "\t data = " + data[i]);
        }
    }

    public static void main(String[] args) {
        MyArrayDeque<String> deque = new MyArrayDeque<>(10);

        deque.addLast("这是什么");
        deque.addLast("这是增加的第二条");
        deque.addLast("当前我是队尾");
        deque.addLast("需要再多增加几条数据");
        deque.addLast("好的");
        deque.removeFirst();
        deque.removeLast();
        deque.addFirst("有需要添加对头咯");
        deque.addFirst("想看看当前的索引位置是否正确");
        deque.addFirst("正确应该是 8");
        deque.addLast("那队尾是多少呢");
        deque.addLast("我也不知道");
        deque.removeLast();
        deque.addLast("会重复吗");
        deque.print();
        System.out.printf(
                "当前队列顺序应该是：\n" +
                "0 有需要添加对头咯\n" +
                "1 这是增加的第二条\n" +
                "2 当前我是队尾\n" +
                "3 需要再多增加几条数据\n" +
                "4 那队尾是多少呢\n" +
                "5 会重复吗\n" +
                "6 \n" +
                "7 \n" +
                "8 正确应该是 8\n" +
                "9 想看看当前的索引位置是否正确"
        );
    }

}
