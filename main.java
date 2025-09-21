class Main {
    public static void main(String[] args) {
        MSQueue<String> queue = new MSQueue<String>();
        queue.enqueue("hello");
        try {
            System.out.println(queue.dequeue());
        } catch(Exception e) {
            System.out.println("Error: " + e);
        }
    }
}