package managers;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class InMemoryHistoryManager implements HistoryManager {

    private final int HISTORY_MAX_SIZE = 10;
    private Node<Task> first;
    private Node<Task> last;
    private HashMap<Integer, Node> history;

    public InMemoryHistoryManager() {
        this.history = new HashMap<>();
    }

    @Override
    public void add(Task task) {
        if (history.containsKey(task.getTaskId())) {
            remove(task.getTaskId());
            linkLast(task);
        } else if (history.size() < HISTORY_MAX_SIZE) {
            linkLast(task);
        } else if (history.size() == HISTORY_MAX_SIZE) {
            removeNode(first);
            linkLast(task);
        }
    }

    @Override
    public void remove(int id) {
        if (history.containsKey(id) && (history.get(id).data instanceof Epic)) {
            ArrayList<Subtask> subtasks = ((Epic) history.get(id).data).getListOfSubtask();
            for (Subtask subtask : subtasks) {
                if (history.containsKey(subtask.getTaskId())) {
                    removeNode(history.get(subtask.getTaskId()));
                }
            }
            removeNode(history.get(id));
            history.remove(id);
        } else if (history.containsKey(id)) {
            removeNode(history.get(id));
            history.remove(id);
        }
    }

    @Override
    public ArrayList<Task> getHistory() {
        return getTasks();
    }


    private void linkLast(Task data) {
        final Node<Task> oldLast = last;
        final Node<Task> newNode = new Node<>(oldLast, data, null);
        last = newNode;
        if (oldLast == null) {
            first = newNode;
        } else {
            oldLast.next = newNode;
        }
        history.put(data.getTaskId(), newNode);
    }

    private ArrayList<Task> getTasks() {
        ArrayList<Task> historyList = new ArrayList<>();
        Node node = last;
        while (node != null) {
            historyList.add(node.data);
            node = node.prev;
        }
        return historyList;
    }

    private void removeNode(Node n) {
        if (n.next != null && n.prev != null) {
            n.prev.next = n.next;
            n.next.prev = n.prev;
            n.prev = null;
            n.next = null;
        } else if (n.next == null && n.prev != null) {
            n.prev.next = null;
            last = n.prev;
            n.prev = null;
        } else if (n.prev == null && n.next != null) {
            n.next.prev = null;
            first = n.next;
            n.next = null;
        } else {
            first = null;
            last = null;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InMemoryHistoryManager that = (InMemoryHistoryManager) o;
        return history.equals(that.history);
    }

    @Override
    public int hashCode() {
        return Objects.hash(history);
    }

    class Node<T extends Task> {
        public Node<Task> next;
        public Node<Task> prev;
        public Task data;

        public Node(Node<Task> prev, Task data, Node<Task> next) {
            this.prev = prev;
            this.data = data;
            this.next = next;
        }
    }
}

