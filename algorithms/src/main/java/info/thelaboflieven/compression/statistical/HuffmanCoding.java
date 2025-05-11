package info.thelaboflieven.compression.statistical;

import info.thelaboflieven.compression.BitSetStream;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

public class HuffmanCoding {

    class HuffmanNode {
        // Character data
        byte data;

        // Frequency of the character
        int frequency;

        // Left and right child nodes
        HuffmanNode left, right;

        // Constructor to initialize the node
        HuffmanNode(byte data, int frequency) {
            this.data = data;
            this.frequency = frequency;
            left = right = null;
        }
    }

    private Map<Byte, Integer> toFrequencyMap(byte[] data) {
        HashMap<Byte, Integer> frequencyMap = new HashMap<>();

        for (Byte c : data) {
            frequencyMap.put(c, frequencyMap.getOrDefault(c, 0) + 1);
        }
        return frequencyMap;
    }

    public void encode(byte[] data, BitSetStream setStream, HuffmanNode tree) {
        var frequencyMap = toFrequencyMap(data);
        PriorityQueue<HuffmanNode> priorityQueue =
                new PriorityQueue<>(Comparator.comparingInt(a -> a.frequency));
        // Create a Huffman node for each character and add it to the priority queue
        for (byte c : frequencyMap.keySet()) {
            priorityQueue.add(new HuffmanNode(c, frequencyMap.get(c)));
        }

        while (priorityQueue.size() > 1) {
            var left = priorityQueue.poll();
            var right = priorityQueue.poll();
            var newNode =
                    new HuffmanNode((byte)'$', left.frequency + right.frequency);
            newNode.left = left;
            newNode.right = right;
            priorityQueue.add(newNode);
        }

        var root = priorityQueue.poll();
        printCodes(root, new StringBuilder());
    }

    public HuffmanNode createTree(byte[] data) {
        var frequencyMap = toFrequencyMap(data);
        PriorityQueue<HuffmanNode> priorityQueue =
                new PriorityQueue<>(Comparator.comparingInt(a -> a.frequency));
        // Create a Huffman node for each character and add it to the priority queue
        for (byte c : frequencyMap.keySet()) {
            priorityQueue.add(new HuffmanNode(c, frequencyMap.get(c)));
        }

        while (priorityQueue.size() > 1) {
            var left = priorityQueue.poll();
            var right = priorityQueue.poll();
            var newNode =
                    new HuffmanNode((byte) '$', left.frequency + right.frequency);
            newNode.left = left;
            newNode.right = right;
            priorityQueue.add(newNode);
        }

        return priorityQueue.poll();
    }

    public static void printCodes(HuffmanNode root, StringBuilder code) {
        if (root == null) return;

        // If this is a leaf node, print the character and its code
        if (root.data != '$') {
            System.out.println(root.data + ": " + code);
        }

        // Traverse the left subtree
        if (root.left != null) {
            printCodes(root.left, code.append('0'));
            code.deleteCharAt(code.length() - 1);
        }

        // Traverse the right subtree
        if (root.right != null) {
            printCodes(root.right, code.append('1'));
            code.deleteCharAt(code.length() - 1);
        }
    }

}
