package utils;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;

public class ListNode implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    public ArrayList<Integer> V;
    public ListNode next;

    public ListNode(ArrayList<Integer> V, ListNode next) {
        this.V = V;
        this.next = next;
    }
}
