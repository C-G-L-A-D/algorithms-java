
## 技巧思路
RingBuffer 相当于环形数组，不同的是，提供 ` read ` 和 ` write ` 方法进行读取和写入。这两个方法都需要传入一个数组，一个用于存储写入字节，一个用于获取读取的字节。最后返回读取/写入的字节数。
同时因为 RingBuffer 结构通常用于底层，因此需要优化其时间复杂度。可优化点如下：

- 扩容/缩容方法：使用位运算代替 % 进行计算；
- 读取、写入方法中拷贝数组方法：可使用 System.arraycopy() 代替 for 循环进行数组拷贝。

## 详解版框架实现代码
```java
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

        // 读取有两种情况，一种是 r 在 w 的左边，一种是 r 在 w 的右边

        // 1. r --- w
        if(r < w) {
            // 此时顺序读取完毕即可
            /*
            等价与
            for(int i = 0; i < n; i++) {
                out[i] = buffer[r + i];
            }
            */
            System.arraycopy(buffer, r, out, 0, n);

            // 更新读取指针位置
            r += n;
            // 更新字节数
            size -= n;

            return n;
        }

        // 2. --- w --- r --- (r 在 w 的右边)
        // 此时读取中仍然具备两种情况，一种是顺序读取后，r 仍在 w 的右边，另一种是右边空间读取完毕，r 从开头读取，即 r 在 w 的左边
        final int cap = buffer.length;
        if(r + n <= cap) {
            // 2.1 ---- w ---**** r -- ，依旧是顺序读取完毕即可
            /*
            等价与
            for(int i = 0; i < n; i++) {
                out[i] = buffer[r + i];
            }
            */
            System.arraycopy(buffer, r, out, 0, n);
        } else {
            // 2.2 *** r --- w --- *****
            // 这种情况需要读取右边剩余字节后，重置去读指针后再继续顺序读取
            /*
            等价于
            for(int i = 0; i < n; i++) {
                if(r + i <= cap)
                    out[i] = buffer[r + i]; // 读取右边剩余字节
                else
                    out[i] = buffer[(i + r) & mask]; // 重头开始读取
            }
            */
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

        // 写入时有两种情况，一种是 w 在 r 左边，一种是 w 在 r 的右边

        // 1. w --- r , 此刻顺序写入即可（扩容后空间足够）
        if(w < r) {
            for(int i = 0; i < n; i++) {
                buffer[w + i] = in[i];
            }

            return n;
        }

        // 2. r ---- w ,此时写入依旧具备两种可能性。即情况一是顺序读写接口；情况二则是 w 到达数组末尾时还未写完，需要到数组开头继续写入。
        if(w + n <= buffer.length) {
            // 2.1 r ---*** w
            /*
            等价于
            for(int i = 0 ; i < n; i++) {
                buffer[w + i] = in[i];
            }
            */
            System.arraycopy(in, 0, buffer, w, n);
            w += n;
        } else {
            // 2.2 *** w --- r ---***
            /*
            等价于
            for(int i = 0 ; i < n; i++) {
                if(w + i <= buffer.length) {
                    buffer[w + i] = in[i];
                } else {
                    buffer[(w + i) % mask] = in[i];
                }
            }
            */
            int num1 = buffer.length - w;
            int num2 = n - num1;
            System.arraycopy(in, 0, buffer, w, num1);
            System.arraycopy(in, num1, buffer, 0, num2);
            w = (w + n) % mask;
        }

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

```

## 终结版代码基础框架
简洁 read 和 write 方法
```java
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

```
