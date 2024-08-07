public class RingBuffer<E> {
    // 字节缓冲区
    private byte[] buffer;

    /*
      mask 用户防止索引越界
      (i + n) % cap 可用与获取需要处理元素的位置，并且防止该索引越界。
      但是 % 的运算复杂度较高，可以使用位运算降低复杂度
      (i + n) & (cap - 1) 等价于 (i + n) % cap
      所以需要存储 cap - 1 的值（cap 指缓存区大小）
     */
    private int mask;

    // 读指针和写指针
    private int r, w;

    private int size;

    private static final int INIT_CAP = 1024;

    public RingBuffer() {
        this(INIT_CAP);
    }

    public RingBuffer(int cap) {
        // 将长度设置为 2 的指数
        cap = ceilToPowerOfTwo(cap);

        // 缓冲区大小变化时，mask 也需要同步更新
        mask = cap - 1;
        // 初始化创建缓存区
        buffer = new byte[cap];

        // 初始化时，写入指针和读取指针默认为开头位置
        r = w = 0;

        // 当前写入字节数为 0
        size = 0;
    }

    /**
     * 读取缓冲区的数据到 out 中
     * @param out
     * @return 返回读取的字节数
     */
    public int read(byte[] out) {

        if(out == null || out.length == 0 || isEmpty()) {
            return 0;
        }

        // 获取最终能够读取的最大字节数
        final int n = Math.min(size, out.length);
        final int cap = buffer.length;
        // 1. 顺序读取完毕即可
        if(r < w || r + n <= cap) {
            System.arraycopy(buffer, r, out, 0, n);
        } else {
            int num1 = buffer.length - r;
            int num2 = num1 - r;
            // buffer[r ... ] - out[0 ... num1]
            System.arraycopy(buffer, r, out, 0, num1);
            // buffer[0 ... num2] - out[num1 ... n]
            System.arraycopy(buffer, r, out, num1, num2);
        }
        // 更新读取指针位置
        r = (r + n) & mask;
        // 更新字节数
        size -= n;
        return n;
    }

    /**
     * 将 in 中的数据写入缓冲区中
     * @param in
     * @return 返回写入的字节个数
     */
    public int write(byte[] in) {
        if(in == null || in.length == 0) {
            return 0;
        }

        // 写入数组的大小
        final int n = in.length;
        // 剩余字节空间
        int free = buffer.length - size;

        if(n > free) {
            // 空间不足，需要扩容
            ensureCapacity(size + n);
        }

        // 1. 顺序写入即可（扩容后空间足够）
        if(w < r || w + n <= buffer.length) {
            System.arraycopy(in, 0, buffer, w, n);
        } else {
            // 2. w 到达数组末尾时还未写完，需要到数组开头继续写入。
            int num1 = buffer.length - w;
            int num2 = n - num1;
            System.arraycopy(in, 0, buffer, w, num1);
            System.arraycopy(in, num1, buffer, 0, num2);

        }
        w = (w + n) % mask;
        // 更新字节数
        size += n;
        return n;
    }

    /***** 工具函数 *****/

    /**
     * 获取缓冲区的元素个数
     * @return
     */
    public int length() {
        return size;
    }

    /**
     * 缓冲区是否为空
     * @return
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * 扩容/缩容
     * @param newCap 指定大小
     */
    private void ensureCapacity(int newCap) {
        // 取整为 2 的指数
        newCap = ceilToPowerOfTwo(newCap);
        byte[] temp = new byte[newCap];
        // 将原始数据读取到临时数组中，并获取写入字节数
        int n = read(temp);
        // 赋值给本地缓存数组，实现扩容/缩容
        this.buffer = temp;
        // 修改写入和读取指针
        this.r = 0;
        this.w = n;
        // 充值边界值
        this.mask = newCap - 1;
    }


    /**
     * 将输入的 n 转化为 2 的指数
     * @param n
     * @return
     */
    private static int ceilToPowerOfTwo(int n) {
        if(n < 0) {
            n = 2;
        }

        if(n > (1 << 30)){
            // int 型最大值为 2^31 - 1， 所以无法向上取整到 2^31
            n = 1 << 30;
        }


        // 此处是为了处理原本就是2的指数的数，例如 8，减一后会向上取整为8
        n--;
        n |= n >> 1;
        n |= n >> 2;
        n |= n >> 4;
        n |= n >> 8;
        n |= n >> 16;
        n++;
        return n;
    }

    public static void main(String[] args) {
        RingBuffer rb = new RingBuffer();
        String s = "123456789";

        int nwrite = rb.write(s.getBytes());
        System.out.println("write " + nwrite + " bytes " + s);
        // write 9 bytes 123456789

        byte[] out1 = new byte[6];
        int nread = rb.read(out1);
        System.out.println("read " + nread + " bytes " + new String(out1));
        // read 6 bytes 123456

        byte[] out2 = new byte[6];
        nread = rb.read(out2);
        System.out.println("read " + nread + " bytes " + new String(out2));
        // read 3 bytes 789   
    }
}
