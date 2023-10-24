## 基本原理
在内存空间中开辟一段连续的空间作为数组空间，因此数组具有一下几种特点：

   - 数组空间大小是固定的；
   - 数组名代表的是数组在内存中的首地址；
   - 数组是紧凑存储，可以根据索引推导出每个数组元素的位置；
   - 由于数组是紧凑存储，因此对数组元素进行增删操作时，需将该元素后面的数组元素向后/前移动。
## 代码实现
### 实现 ArrayList 的基础框架
```java
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

	/****** 增删查改开始位置 ******/
	
	/****** 增删查改结束位置 ******/

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
	
	@Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private int p = 0;

            @Override
            public boolean hasNext() {
                return p != size;
            }

            @Override
            public E next() {
                return data[p++];
            }
        };
    }
}

```

![image.png](https://cdn.nlark.com/yuque/0/2023/png/27354749/1695266262567-5fee2bd4-df62-42f0-a13a-cbc5f48ccf00.png#averageHue=%23fbfbfb&clientId=u87321e82-950a-4&from=paste&height=381&id=u81fY&originHeight=762&originWidth=1944&originalType=binary&ratio=2&rotation=0&showTitle=false&size=53454&status=done&style=shadow&taskId=u103d603a-a6ca-47fb-a5c4-ea1d05c4a0c&title=&width=972)

> System.arraycopy(); 传入参数：
> 1.  源数组
> 2. 源数组中需要复制的元素索引开始位置
> 3. 目标数组
> 4. 目标数组中需要接收元素的索引开始位置
> 5. 需要复制的元素个数


### 实现 ArrayList 的增加
```java
....




....
```

### 实现 ArrayList 的删除

### 实现 ArrayList 的查找
```java
/***** 查 *****/
// 返回索引 index 对应的元素
public E get(int index) {
	// 判断该索引 index 位置是否存在元素
	checkElementIndex(index);
	return data[index];
}
```

### 实现 ArrayList 的更改
```java
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
	
	// 4. 数据迁移「将删除元素后的所有元素向前移动」，data[index...] -> data[index - 1...]
	System.arraycopy(data, index + 1, data, index, size - index + 1);
	data[size--] = null;
	
	return delElement;
}
```
