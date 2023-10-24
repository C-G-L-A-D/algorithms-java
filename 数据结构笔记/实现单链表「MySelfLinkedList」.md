## 实现原理
## 代码实现
### 实现单链表「MySelfLinkedList」的基础框架
```java
import java.util.Iterator;
import java.util.NoSuchElementException;

// 单链表
public class MyselfLinkedList<E> {
	// 双链表节点
	private static class Node<E> {
		// 当前节点元素
		E val;
		// 下一个节点指针
		Node<E> next;

		Node() {}

		Node(E val) {
			this.val = val;
		}
	}

	private final Node<E> head;
	private int size;

	/***** 构造函数 *****/
	public MyselfLinkedList() {
		// 创建哨兵节点进行占位， 简化边界条件，防止对特殊条件的判断
		this.head = new Node<>(null);

		head.next = null;

		this.size = 0;
	}

	/***** 增删改查开始 *****/
	
	/***** 增删改查结束 *****/

	/***** 工具函数 *****/

	public int size() {
		return size;
	}

	public boolean isEmpty() {
		return size == 0;
	}


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

	// 返回 index 对应的 Node 节点
	private Node<E> getNode(int index) {
		// 链表中第一个元素
		checkElementIndex(index);
		Node<E> firstNode = head.next;
		for (int i = 0; i < index; i++) {
			firstNode = firstNode.next;
		}
		return firstNode;
	}

	public Iterator<E> iterator() {
		return new Iterator<>() {
			Node<E> p = head.next;

			@Override
			public boolean hasNext() {
				return p != null;
			}

			@Override
			public E next() {
				E val = p.val;
				p = p.next;
				return val;
			}
		};
	}
}

```
### 实现单链表「MySelfLinkedList」的增加
```java
/***** 增 *****/

/**
 * 在头部添加元素
 * @param element 添加的元素
 *
 * */
public void addFirst(E element) {
	// 原： head -> temp
	// 改： head -> insertNode -> temp
	// 创建需要插入的节点
	Node<E> insertNode = new Node<>(element);
	// 记录头指针的下一个节点
	Node<E> temp = head.next;

	insertNode.next = temp;
	head.next = insertNode;
	size++;
}

/**
 * 在尾部添加元素
 * @param element 添加的元素
 *
 * */
public void addLast(E element) {
	checkPositionIndex(size);

	// 原： temp -> null
	// 改： temp -> insertNode -> null
	// 创建需要插入的节点
	Node<E> insertNode = new Node<>(element);
	Node<E> temp = getNode(size - 1);
	System.out.println(temp + " " + element);
	temp.next = insertNode;
	insertNode.next = null;
	size++;
}

/**
 * 在指定索引 index 位置添加元素
 * @param element 添加的元素
 *
 * */
public void add(int index, E element) {
	checkPositionIndex(index);
	// 原： prevNode -> temp
	// 改： prevNode -> insertNode -> temp
	Node<E> insertNode = new Node<>(element);
	Node<E> prevNode;
	if(index == 0 ) {
		addFirst(element);
		return;
	} else if(index == size) {
		addLast(element);
		return;
	} else {
		prevNode = getNode(index - 1);
	}
	Node<E> temp = prevNode.next;

	insertNode.next = temp;
	prevNode.next = insertNode;

	size++;
}
```

### 实现单链表「MySelfLinkedList」的删除
```java
/***** 删 *****/

/**
 * 删除头节点元素
 *
 * */
public E removeFirst() {
	if(isEmpty()) {
		throw new NoSuchElementException();
	}

	// 原： head -> removeNode -> temp
	// 改： head -> temp
	Node<E> removeNode = head.next;
	Node<E> temp = removeNode.next;

	head.next = temp;
	// 不用置空也可以，垃圾回收会把没有被指向的节点回收
	removeNode.next = null;
	size--;

	return removeNode.val;
}
/**
 * 删除尾节点元素
 *
 * */
public E removeLast() {
	if(isEmpty()) {
		throw new NoSuchElementException();
	}

	if(size - 2 < 0) {
		return removeFirst();
	}

	// 原： temp -> removeNode -> null
	// 改： temp -> null
	Node<E> temp = getNode(size - 2);

	Node<E> removeNode = temp.next;

	temp.next = null;
	size--;

	return removeNode.val;
}

/**
 * 删除指定节点元素
 * @param index 删除的元素位置
 *
 * */
public E remove(int index) {

	checkElementIndex(index);

	// 原： prevNode -> removeNode -> nextNode
	// 改： prevNode -> nextNode
	if(index - 1 < 0){
		return removeFirst();
	}

	Node<E> prevNode = getNode(index - 1);
	Node<E> removeNode = prevNode.next;
	Node<E> nextNode = removeNode.next;

	prevNode.next = nextNode;
	removeNode.next = null;
	size--;

	return removeNode.val;
}
```

### 实现单链表「MySelfLinkedList」的查找
```java
/***** 查 *****/

/**
 * 获取头节点元素
 *
 * */
public E getFirst() {
	if(isEmpty()) {
		throw new NoSuchElementException();
	}

	return head.next.val;
}

/**
 * 获取尾节点元素
 *
 * */
public E getLast() {
	if(isEmpty()) {
		throw new NoSuchElementException();
	}

	return getNode(size - 1).val;
}

/**
 * 获取指定节点元素
 * @param index 获取的节点位置
 *
 * */
public E get(int index) {
	checkElementIndex(index);

	return getNode(index).val;
}
```

### 实现单链表「MySelfLinkedList」的修改
```java
/***** 改 *****/
/**
 * 修改指定索引 index 位置的元素，并返回原值
 * @param index 指定修改的节点索引
 * @param element 新的元素
 *
 * */
public E set(int index, E element) {
	Node<E> node = getNode(index);

	E oldVal = node.val;
	node.val = element;

	return oldVal;
}
```

