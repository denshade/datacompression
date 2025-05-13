package info.thelaboflieven.compression.statistical;

import info.thelaboflieven.compression.BitSetStream;

import java.util.*;

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

        public boolean isLeafNode() {
            return left == null && right == null;
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
        var map = toByteToBooleanMap(tree, new StringBuilder());
        for (var d: data) {
            var bools = map.get(d);
            for (boolean bool: bools) {
                setStream.addBit(bool);
            }
        }
    }

    public void writeTree(HuffmanNode tree, BitSetStream bitSetStream) {
        if (tree.isLeafNode())
        {
            bitSetStream.addBit(true);
            bitSetStream.writeByte(tree.data);
        }
        else
        {
            bitSetStream.addBit(false);
            writeTree(tree.left, bitSetStream);
            writeTree(tree.right, bitSetStream);
        }
    }

    private static Map<Boolean[], Byte> invert(Map<Byte, Boolean[]> myHashMap) {
        Map<Boolean[], Byte> myNewHashMap = new HashMap<>();
        for(Map.Entry<Byte, Boolean[]> entry : myHashMap.entrySet()){
            myNewHashMap.put(entry.getValue(), entry.getKey());
        }
        return myNewHashMap;
    }

    public Byte[] decode(BitSetStream bitSetStream, HuffmanNode t) {
        var map = toByteToBooleanMap(t, new StringBuilder());
        var boolArrayToByte = invert(map);
        List<Byte> result = new ArrayList<>();
        List<Boolean> buffer = new ArrayList<Boolean>();
        while (bitSetStream.hasBitsLeft()) {
            buffer.add(bitSetStream.readBit());
            var mappedByte = getByteForBools(boolArrayToByte, buffer);
            if (mappedByte == null) {
                continue;
            }
            result.add(mappedByte);
            buffer.clear();
        }
        return result.toArray(new Byte[0]);
    }

    private Byte getByteForBools(Map<Boolean[], Byte> boolArrayToByte, List<Boolean> buffer) {
        for (var key : boolArrayToByte.keySet()) {
            boolean keyMatched = true;
            if (key.length != buffer.size()) {
                continue;
            }
            for (int index = 0; index < key.length; index++) {
                if (key[index] != buffer.get(index)) {
                    keyMatched = false;
                }
            }
            if (keyMatched) {
                return boolArrayToByte.get(key);
            }
        }
        return null;
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

    private static Boolean[] bitStringToBoolean(String bitString){
        var b = new Boolean[bitString.length()];
        for (int i = 0; i < bitString.length(); i++) {
            b[i] = bitString.charAt(i) == '1';
        }
        return b;
    }

    public static Map<Byte, Boolean[]> toByteToBooleanMap(HuffmanNode root, StringBuilder code) {
        if (root == null) return Map.of();

        // If this is a leaf node, print the character and its code
        if (root.data != '$') {
            System.out.println(root.data + ": " + code);
            return Map.of(root.data, bitStringToBoolean(code.toString()));
        }
        var map = new HashMap<Byte, Boolean[]>();
        // Traverse the left subtree
        if (root.left != null) {
            map.putAll(toByteToBooleanMap(root.left, code.append('0')));
            code.deleteCharAt(code.length() - 1);
        }

        // Traverse the right subtree
        if (root.right != null) {
            map.putAll(toByteToBooleanMap(root.right, code.append('1')));
            code.deleteCharAt(code.length() - 1);
        }
        return map;
    }

}
