import java.util.NoSuchElementException;

public class MyArrayList<E> {
    // 真正存储的数据
    private E[] data;

    // 记录当前数组中的元素个数
    private int size;

    // 默认初始容量
    private static final int INIT_CAP = 1;

    public MyArrayList() {
        this(INIT_CAP);
    }

    public MyArrayList(int initCapacity) {
        data = (E[]) new Object[initCapacity];
        size = 0;
    }

    /***** 增 *****/

    // 在数组尾部添加元素
    public void addLast(E e) {
        // 先判断数组大小是否足够添加元素，决定是否扩容
        if(data.length == size) {
            // 扩容，一般是扩容两倍
            resize(data.length * 2);
        }

        // 在尾部插入数据
        data[size] = e;
        size++;
    }

    // 在 index 索引位置添加一个元素 element
    public void add(int index, E element) {
        // 1. 判断该索引 index 位置是否可以插入元素
        checkPositionIndex(index);

        // 2. 数据迁移「将所有数据向后移一位」， data[index...] 移动到 data[index + 1 ...]
        System.arraycopy(data, index, data, index+1, size - index);

        // 3. 添加元素
        data[index] = element;
        size++;
    }

    /***** 删 *****/

    // 删除数组的最后一个元素并返回
    public E removeLast() {
        // 1. 先判断数组中是否有元素可以删除
        if(isEmpty()) {
            throw new NoSuchElementException();
        }

        // 2. 判断数组是否需要缩容节约空间，一般缩小一半
        if(size < data.length / 4) {
            resize(data.length / 2);
        }

        E delElemet = data[size - 1];
        // 删除末尾元素，并改变 size
        data[--size] = null;
        return delElemet;
    }

    // 删除 index 索引位置的元素并返回
    public E remove(int index) {
        // 1. 判断该索引 index 位置是否存在元素
        checkElementIndex(index);

        // 2. 判断数组是否需要缩容节约空间，一般缩小一半
        if(size < data.length / 4) {
            resize(data.length / 2);
        }

        // 3. 记录删除元素
        E delElement = data[index];

        // 4. 数据迁移「将删除元素后的所有元素向前移动」， data[index...] -> data[index - 1...]
        System.arraycopy(data, index + 1, data, index, size - index + 1);
        data[size--] = null;

        return delElement;
    }

    /***** 查 *****/

    // 返回索引 index 对应的元素
    public E get(int index) {
        // 判断该索引 index 位置是否存在元素
        checkElementIndex(index);
        return data[index];
    }

    /***** 改 *****/

    // 将索引 index 的元素改为 element 并返回之前的元素值
    public E set(int index, E element) {
        // 判断该索引 index 位置是否存在元素
        checkElementIndex(index);
        E oldElement = data[index];
        data[index] = element;
        return  oldElement;
    }

    /***** 工具函数 *****/
    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    /***** 私有函数 *****/
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


    // 数组扩容或缩容
    private void resize(int newCap) {
        E[] temp = (E[]) new Object[newCap];

        // java 标准库中的方法，本质使用for循环赋值数据
        System.arraycopy(data, 0, temp, 0, size);
        data = temp;
    }
}
