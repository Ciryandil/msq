import java.util.concurrent.atomic.AtomicStampedReference;

class Node<T> {
    T value;
    AtomicStampedReference<Node<T>> next;
    public Node(T value) {
        this.value = value;
        this.next = new AtomicStampedReference<Node<T>>(null, 0);
    }

}

class MSQueue<T> {
    AtomicStampedReference<Node<T>> head;
    AtomicStampedReference<Node<T>> tail;
    
    public MSQueue() {
        Node<T> headNode = new Node<T>(null);
        head = new AtomicStampedReference<Node<T>>(headNode, 0);
        tail = new AtomicStampedReference<Node<T>>(headNode, 0);
    }

    public void enqueue(T value) {
        Node<T> newNode = new Node<T>(value);
        int lastStamp[] = new int[1];
        int nextStamp[] = new int[1];
        Node<T> last;
        Node<T> next;
        while(true) {
            last = tail.get(lastStamp);
            next = last.next.get(nextStamp);
            if(last == tail.getReference()) {
                if(next == null) {
                    if(last.next.compareAndSet(next, newNode, nextStamp[0], nextStamp[0]+1)) {
                        tail.compareAndSet(last, newNode, lastStamp[0], lastStamp[0]+1);
                        return;
                    }
                } else {
                    tail.compareAndSet(last, next, lastStamp[0], nextStamp[0]);
                }
            }
        }
    }

    public T dequeue() throws Exception {
        Node<T> first;
        Node<T> next;
        Node<T> last;
        int firstStamp[] = new int[1];
        int nextStamp[] = new int[1];
        int lastStamp[] = new int[1];
        while(true) {
            first = head.get(firstStamp);
            next = first.next.get(nextStamp);
            last = tail.get(lastStamp);
            if(first == head.getReference()) {
                if(first == last) {
                    if(next == null) {
                        throw new Exception("Queue is empty");
                    }
                    tail.compareAndSet(last, next, lastStamp[0], lastStamp[0]+1);
                } else {
                    T value = next.value;
                    if(head.compareAndSet(first, next, firstStamp[0], firstStamp[0]+1)) {
                        //free(first);
                        return value;
                    }
                }
            }
        }
    }
}