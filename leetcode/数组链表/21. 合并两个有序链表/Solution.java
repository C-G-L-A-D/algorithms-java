/**
 * Definition for singly-linked list.
 * public class ListNode {
 *     int val;
 *     ListNode next;
 *     ListNode() {}
 *     ListNode(int val) { this.val = val; }
 *     ListNode(int val, ListNode next) { this.val = val; this.next = next; }
 * }
 */
class Solution {
    public ListNode mergeTwoLists(ListNode list1, ListNode list2) {
        // 虚拟头节点 dummy 用于记录为合并后链表的头节点， p 用于充当尾节点，依次向后添加节点。
        ListNode dummy = new ListNode(), p = dummy;
        // p1, p2 分别记录两个链表当前的待比较节点（从头开始）
        ListNode p1 = list1, p2 = list2;


        // 1.同时循环遍历两个链表， 比较节点
        while(p1 != null && p2 != null) {

            // 1.1 比较两个链表的当前节点
            // 将较小的节点拼接到新链表中，然后更改待比较节点为下一个节点（只更改当前较小节点所在的链表）
            if(p1.val < p2.val) {
                p.next = p1;
                p1 = p1.next;
            } else {
                p.next = p2;
                p2 = p2.next;
            }
            // 1.2 更新新链表的尾节点
            p = p.next;
        }

        // 2.需要判断哪个链表还没遍历完
        // 只要有一个链表遍历完成就说明另外一个链表剩余的节点都比这个链表的节点大
        // 因此可以直接将剩下的节点进行添加。
        if(p1 != null) {
            p.next = p1;
        }

        if(p2 != null) {
            p.next = p2;
        }

        // 虚拟节点的下一个节点才是新链表的开始
        return dummy.next;

    }
}