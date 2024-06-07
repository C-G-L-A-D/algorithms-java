public class RingBuffer {
    // 字节缓冲区
    private byte[] buffer;
    // mask 用户防止索引越界
    private int mask;

    // 读指针和写指针
    private int r, w;

    private int size;

    private static final int INIT_CAP = 1024;

    public RingBuffer() {
        this(INIT_CAP);
    }

    public RingBuffer(int cap) {
        buffer = new byte[cap];
    }

    /**
     * 读取缓冲区的数据到 out 中
     * @param out
     * @return 返回读取的字节数
     */
    public int read(byte[] out) {}

    /**
     * 将 in 中的数据写入缓冲区中
     * @param in
     * @return 返回写入的字节个数
     */
    public int write(byte[] in) {

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
    private void resize(int newCap) {}


    /**
     * 将输入的 n 转化为 2 的指数
     * @param n
     * @return
     */
    private static int ceilToPowerOfTwo(int n) {}


    public static void main(String[] args) {
        RingBuffer rb = new RingBuffer();
        String s = "123456789";

        int nwrite = rb.write(s.getBytes());
        System.out.printf("write " + nwrite + " bytes " + s);
        // write 9 bytes 123456789

        byte[] out1 = new byte[6];
        int nread = rb.read(out1);
        System.out.printf("read " + nread + " bytes " + new String(out1));
        // read 6 bytes 123456

        byte[] out2 = new byte[6];
        nread = rb.read(out2);
        System.out.printf("read " + nread + " bytes " + new String(out2));
        // read 3 bytes 789NULLNULLNULL
    }
}
