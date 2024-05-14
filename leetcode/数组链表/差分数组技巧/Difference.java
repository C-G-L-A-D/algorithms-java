// 封装为工具类
class Difference {
    private int[] diffs;

		// 初始化工具类
    public Difference(int length) {
      // 只传入数组大小，所有元素默认为0，与获取数组为 [0, 0, 0, ...] 的差分数组结果相同  
			diffs = new int[length];
		}
		// 根据指定数组初始化差分数组
    public Difference(int[] nums) {
        assert nums.length > 0;
        diffs = new int[nums.length];

        diffs[0] = nums[0];

        for(int i = 1; i < nums.length; i++) {
            diffs[i] = nums[i] - nums[i - 1];
        }
    }

		// [i, j]区间范围内增减 num
    public void increment(int i, int j, int num) {
        if(i >= 0 && i < diffs.length) diffs[i] += num;
        if(j + 1 >= i && j + 1 < diffs.length) diffs[j + 1] -= num;
    }

		// 获取对应数组结果
    public int[] result() {
        int[] res = new int[diffs.length];
        res[0] = diffs[0];
        for(int i = 1; i < diffs.length; i++) {
            res[i] = res[i - 1] + diffs[i];
        }
        return res;
    }
}
