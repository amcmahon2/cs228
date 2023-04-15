package hw3;

import java.util.AbstractSequentialList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

//import hw3.StoutList.Node;
//import hw3.StoutList.NodeInfo;

/**
 * Implementation of the list interface based on linked nodes that store
 * multiple items per node. Rules for adding and removing elements ensure that
 * each node (except possibly the last one) is at least half full.
 */
public class StoutList<E extends Comparable<? super E>> extends AbstractSequentialList<E> {
	/**
	 * Default number of elements that may be stored in each node.
	 */
	private static final int DEFAULT_NODESIZE = 4;

	/**
	 * Number of elements that can be stored in each node.
	 */
	private final int nodeSize;

	/**
	 * Dummy node for head. It should be private but set to public here only for
	 * grading purpose. In practice, you should always make the head of a linked
	 * list a private instance variable.
	 */
	public Node head;

	/**
	 * Dummy node for tail.
	 */
	private Node tail;

	/**
	 * Number of elements in the list.
	 */
	private int size;

	/**
	 * Constructs an empty list with the default node size.
	 */
	public StoutList() {
		this(DEFAULT_NODESIZE);
	}

	/**
	 * Constructs an empty list with the given node size.
	 * 
	 * @param nodeSize number of elements that may be stored in each node, must be
	 *                 an even number
	 */
	public StoutList(int nodeSize) {
		if (nodeSize <= 0 || nodeSize % 2 != 0)
			throw new IllegalArgumentException();

		// dummy nodes
		head = new Node();
		tail = new Node();
		head.next = tail;
		tail.previous = head;
		this.nodeSize = nodeSize;
	}

	/**
	 * Constructor for grading only. Fully implemented.
	 * 
	 * @param head
	 * @param tail
	 * @param nodeSize
	 * @param size
	 */
	public StoutList(Node head, Node tail, int nodeSize, int size) {
		this.head = head;
		this.tail = tail;
		this.nodeSize = nodeSize;
		this.size = size;
	}

	/**
	 * @return size, which is equal to "Number of elements in the list."
	 */
	@Override
	public int size() {
		return this.size;
	}

	/**
	 * This method adds an item to the end of the data array, unless the item is
	 * null or the array already has it (specified in HW3 pdf)
	 * 
	 * @param item, the item to be added to the end of the list
	 */
	@Override
	public boolean add(E item) {
		if (item == null) {
			throw new NullPointerException();
		}
		// if (contains(item)) {
//			return false;
//		}

		// if there are no nodes currently, or the last node is full, make a new one
		if (size == 0) {
			Node t = new Node();
			t.addItem(item);
			head.next = t;
			t.previous = head;
			t.next = tail;
			tail.previous = t;
		} else {
			// if last node is full
			if (tail.previous.count >= this.nodeSize) {
				Node t = new Node();
				t.addItem(item);
				Node prev = tail.previous;
				prev.next = t;
				t.previous = prev;
				t.next = tail;
				tail.previous = t;
			} else {
				// last node is not full, add to end of last node
				tail.previous.addItem(item);
			}
		}
		this.size++;
		return true;
	}

	/**
	 * This method adds an item to array of data at a specified index pos
	 * 
	 * @param pos,  the index of where the item is to be added
	 * @param item, the item which is added to the data array
	 */
	@Override
	public void add(int pos, E item) {
		if (pos < 0 || pos > size) {
			throw new IndexOutOfBoundsException();
		}
//		if the list is empty, create a new node and put X at offset 0
		if (head.next == tail) {
			add(item);
		}
		NodeInfo nodeInfo = find(pos);
		Node temp = nodeInfo.node;
		int offset = nodeInfo.offset;

//		otherwise if off = 0 and one of the following two cases occurs,	  
		if (offset == 0) {
//		    if n has a predecessor which has fewer than M elements (and is not the head), put X in n’s
//	        predecessor
			if (temp.previous.count < nodeSize && temp.previous != head) {
				temp.previous.addItem(item);
				size++;
				return;
			}
//			if n is the tail node and n’s predecessor has M elements, create a new node and put X at offset
//			0
			else if (temp == tail) {
				add(item);
				size++;
				return;
			}
		}
//		otherwise if there is space in node n, put X in node n at offset off, shifting array elements as
//		necessary
		else if (temp.count < nodeSize) {
			temp.addItem(offset, item);
		}
//		otherwise, perform a split operation: move the last M/2 elements of node n into a new successor
//		node n', and then	
		else {
			Node nextOne = new Node();
			int index = 0;
			int i = nodeSize/2;
			// loop through and add data to array
			while (index < i) {
				nextOne.addItem(temp.data[i]);
				temp.removeItem(i);
				index++;
			}
			Node lastOne = temp.next;
			temp.next = nextOne;
			nextOne.previous = temp;
			nextOne.next = lastOne;
			lastOne.previous = nextOne;

			if (offset <= nodeSize / 2) {
				// "put X in node n at offset"
				temp.addItem(offset, item);
			} else {
				// "put X in node n at offset-M - nodeSize (M) / 2"
				nextOne.addItem(offset - i, item);
			}

		}
		size++;
	}

	/**
	 * This method removes an element at the specified position, with a few caveats
	 * as described in the project description PDF
	 * 
	 * @param pos, index of the element to be removed in the data array
	 */
	@Override
	public E remove(int pos) {
		if (pos < 0 || pos > size) {
			throw new IndexOutOfBoundsException();
		}
		NodeInfo nodeInfo = find(pos);
		Node temp = nodeInfo.node;
		int offset = nodeInfo.offset;
		E val = temp.data[offset];

		// "if the node n containing X is the last node and has only one element, delete
		// it;"
		if (temp.next == tail && temp.count == 1) {
			Node pre = temp.previous;
			pre.next = temp.next;
			temp.next.previous = pre;
			temp = null;
		}
		// "otherwise, if n is the last node (thus with two or more elements),
		// or if n has more than M/2 elements, remove X from n, shifting elements as
		// necessary;"
		else if (temp.next == tail || temp.count > nodeSize / 2) {
			temp.removeItem(offset);
		}
		// "otherwise (the node n must have at most M/2 elements), look at its successor
		// n'
		// (note that we dont look at the predecessor of n) and perform a merge
		// operation as follows:"
		else {
			temp.removeItem(offset);
			Node nextNode = temp.next;

			// "if the successor node n' has more than elements, move the first element from
			// n' to n. (mini-merge)"
			if (nextNode.count > nodeSize / 2) {
				temp.addItem(nextNode.data[0]);
				nextNode.removeItem(0);
			}
			// "if the successor node n' has or fewer elements, then move all elements from
			// n' to n and delete n' (full merge)"
			else if (nextNode.count <= nodeSize / 2) {
				for (int i = 0; i < nextNode.count; i++) {
					temp.addItem(nextNode.data[i]);
				}
				temp.next = nextNode.next;
				nextNode.next.previous = temp;
				nextNode = null;
			}
		}
		this.size--;
		return val;
	}

	/**
	 * Sort all elements in the stout list in the NON-DECREASING order. You may do
	 * the following. Traverse the list and copy its elements into an array,
	 * deleting every visited node along the way. Then, sort the array by calling
	 * the insertionSort() method. (Note that sorting efficiency is not a concern
	 * for this project.) Finally, copy all elements from the array back to the
	 * stout list, creating new nodes for storage. After sorting, all nodes but
	 * (possibly) the last one must be full of elements.
	 * 
	 * Comparator<E> must have been implemented for calling insertionSort().
	 */
	public void sort() {
		E[] list = (E[]) new Comparable[size];
		int j = 0;
		Node temp = head.next;
		// loop through all of the nodes
		while (temp != tail) {
			for (int i = 0; i < temp.count; i++) {
				list[j] = temp.data[i];
				j++;
			}
			// go to next node
			temp = temp.next;
		}

		head.next = tail;
		tail.previous = head;

		Comparator<E> comp = new ElementComparator();

		insertionSort(list, comp);
		size = 0;
		for (int i = 0; i < list.length; i++) {
			add(list[i]);
		}

	}

	/**
	 * Sort all elements in the stout list in the NON-INCREASING order. Call the
	 * bubbleSort() method. After sorting, all but (possibly) the last nodes must be
	 * filled with elements.
	 * 
	 * Comparable<? super E> must be implemented for calling bubbleSort().
	 */
	public void sortReverse() {
		E[] reverse = (E[]) new Comparable[this.size];
		int index = 0;
		Node temp = head.next;
		while (temp != tail) {
			for (int i = 0; i < temp.count; i++) {
				reverse[index] = temp.data[i];
				index++;
			}
			temp = temp.next;
		}

		// wipe (or "delete") all nodes
		// by setting the StoutList back to its original
		// state with dummy nodes pointing to one another
		head.next = tail;
		tail.previous = head;

		bubbleSort(reverse);
		// set size of StoutList back to 0 !!
		this.size = 0;
		for (int i = 0; i < reverse.length; i++) {
			add(reverse[i]);
		}
	}

	@Override
	public Iterator<E> iterator() {
		return new StoutListIterator();
	}

	@Override
	public ListIterator<E> listIterator() {
		return new StoutListIterator();
	}

	@Override
	public ListIterator<E> listIterator(int index) {
		return new StoutListIterator(index);
	}

	/**
	 * Returns a string representation of this list showing the internal structure
	 * of the nodes.
	 */
	public String toStringInternal() {
		return toStringInternal(null);
	}

	/**
	 * Returns a string representation of this list showing the internal structure
	 * of the nodes and the position of the iterator.
	 *
	 * @param iter an iterator for this list
	 */
	public String toStringInternal(ListIterator<E> iter) {
		int count = 0;
		int position = -1;
		if (iter != null) {
			position = iter.nextIndex();
		}

		StringBuilder sb = new StringBuilder();
		sb.append('[');
		Node current = head.next;
		while (current != tail) {
			sb.append('(');
			E data = current.data[0];
			if (data == null) {
				sb.append("-");
			} else {
				if (position == count) {
					sb.append("| ");
					position = -1;
				}
				sb.append(data.toString());
				++count;
			}

			for (int i = 1; i < nodeSize; ++i) {
				sb.append(", ");
				data = current.data[i];
				if (data == null) {
					sb.append("-");
				} else {
					if (position == count) {
						sb.append("| ");
						position = -1;
					}
					sb.append(data.toString());
					++count;

					// iterator at end
					if (position == size && count == size) {
						sb.append(" |");
						position = -1;
					}
				}
			}
			sb.append(')');
			current = current.next;
			if (current != tail)
				sb.append(", ");
		}
		sb.append("]");
		return sb.toString();
	}

	/**
	 * Node type for this list. Each node holds a maximum of nodeSize elements in an
	 * array. Empty slots are null.
	 */
	private class Node {
		/**
		 * Array of actual data elements.
		 */
		// Unchecked warning unavoidable.
		public E[] data = (E[]) new Comparable[nodeSize];

		/**
		 * Link to next node.
		 */
		public Node next;

		/**
		 * Link to previous node;
		 */
		public Node previous;

		/**
		 * Index of the next available offset in this node, also equal to the number of
		 * elements in this node.
		 */
		public int count;

		/**
		 * Adds an item to this node at the first available offset. Precondition: count
		 * < nodeSize
		 * 
		 * @param item element to be added
		 */
		void addItem(E item) {
			if (count >= nodeSize) {
				return;
			}
			data[count++] = item;
			// useful for debugging
			// System.out.println("Added " + item.toString() + " at index " + count + " to
			// node " + Arrays.toString(data));
		}

		/**
		 * Adds an item to this node at the indicated offset, shifting elements to the
		 * right as necessary.
		 * 
		 * Precondition: count < nodeSize
		 * 
		 * @param offset array index at which to put the new element
		 * @param item   element to be added
		 */
		void addItem(int offset, E item) {
			if (count >= nodeSize) {
				return;
			}
			for (int i = count - 1; i >= offset; --i) {
				data[i + 1] = data[i];
			}
			++count;
			data[offset] = item;
		}

		/**
		 * Deletes an element from this node at the indicated offset, shifting elements
		 * left as necessary. Precondition: 0 <= offset < count
		 * 
		 * @param offset
		 */
		void removeItem(int offset) {
			E item = data[offset];
			for (int i = offset + 1; i < nodeSize; ++i) {
				data[i - 1] = data[i];
			}
			data[count - 1] = null;
			--count;
		}
	}

	private class StoutListIterator implements ListIterator<E> {
		final int mNEXT = 1;
		final int mPREV = -1;
		/**
		 * Used to track position in stoutArray
		 */
		private int position;

		/**
		 * Array that stores all of the nodes' data
		 */
		private E[] stoutArray;

		/**
		 * Boolean variable which tracks whether remove() can be called per homework PDF
		 * rules
		 */
		private boolean canRemove;

		/**
		 * keeps track of direction last moved
		 */
		private int direction;

		/**
		 * Default constructor
		 */
		public StoutListIterator() {
			direction = 0;
			position = 0;
			getData();
		}

		/**
		 * Constructor finds node at a given position.
		 * 
		 * @param pos
		 */
		public StoutListIterator(int pos) {
			direction = 0;
			position = pos;
			getData();
		}

		/**
		 * Private class I made which makes stoutList data accessible in this class.
		 * Without it, it would be impossible (I think?) to change anything in any of
		 * the nodes (add/remove etc.)
		 */
		private void getData() {
			stoutArray = (E[]) new Comparable[size];
			int index = 0;
			Node t = head.next;
			// loop through nodes
			while (t != tail) {
				// loop through current elements in current node t
				// loops through the variable size of the node,
				// determined above.
				for (int i = 0; i < t.count; i++) {
					stoutArray[index] = t.data[i];
					index++;
				}
				// move to next node
				t = t.next;
			}
		}

		/**
		 * @return true if the position is <= the node's size, false if it is > the size
		 *         of the node
		 */
		@Override
		public boolean hasNext() {
			// check to see if we're at end of list
			if (position < size) {
				return true;
			} else {
				return false;
			}
		}

		/**
		 * "Removes from the list the last element that was returned by next or
		 * previous." pretty much .previous() but the other way
		 */
		@Override
		public E next() {
			// first check to make sure there is a next element
			if (!this.hasNext()) {
				throw new NoSuchElementException();
			} else {
				position = mNEXT;
				return stoutArray[position++];
			}
		}

		/**
		 * "Removes from the list the last element that was returned by next or
		 * previous."
		 */
		@Override
		public void remove() {
			if (direction == mNEXT) {
				StoutList.this.remove(position - 1);
				getData();
				direction = 0;
				position--;
				if (position < 0) {
					position = 0;
				}
			} else if (direction == mPREV) {
				StoutList.this.remove(position);
				getData();
				direction = 0;
			} else {
				throw new IllegalStateException();
			}

		}

		/**
		 * @return true if the position is not > 0, false if it is <= 0
		 */
		@Override
		public boolean hasPrevious() {
			if (position > 0) {
				return true;
			} else {
				return false;
			}
		}

		/**
		 * "Returns the previous element in the list and moves the cursor position
		 * backwards."
		 * 
		 * @return item, the previous element in the list
		 */
		@Override
		public E previous() {
			if (!hasPrevious()) {
				throw new NoSuchElementException();
			} else {
				direction = mPREV;
				return stoutArray[position--];
			}

		}

		/**
		 * Returns next index of element (next to be served)
		 * 
		 * @return this.position, which is the current position
		 */
		@Override
		public int nextIndex() {
			// since this.position is base 1 but index is base 0, return just this.position
			return position;
		}

		/**
		 * Returns previous index of element (last served)
		 * 
		 * @return this.position-1, the previous position
		 */
		@Override
		public int previousIndex() {
			return position - 1;
		}

		/**
		 * /Replaces the last element returned by next or previous with the specified
		 * element
		 * 
		 * @param e, element to set the current last served element equal to
		 */
		@Override
		public void set(E e) {
			if (direction == mNEXT) {
				NodeInfo nodeInfo = find(position - 1);
				nodeInfo.node.data[nodeInfo.offset] = e;
				stoutArray[position - 1] = e;
				direction = 0;
			} else if (direction == mPREV) {
				NodeInfo nodeInfo = find(position);
				nodeInfo.node.data[nodeInfo.offset] = e;
				stoutArray[position] = e;
				direction = 0;
			} else {
				throw new IllegalStateException();
			}
		}

		/***
		 * "Inserts the specified element into the list. The element is inserted
		 * immediately before the element that would be returned by next, if any, and
		 * after the element that would be returned by previous, if any."
		 * 
		 * @param e, element to add to the current StoutList
		 */
		@Override
		public void add(E e) {
			// "It (call to .remove()) can be made only if add has not been called after the
			// last call to next or previous."
			if (e == null) {
				throw new NullPointerException();
			}
			StoutList.this.add(position, e);
			position++;
			getData();
			direction = 0;
		}

	}

	/**
	 * This method is an Override of the compare method which is used to get around
	 * the instantiation error when making a new comparator for insertion sort.
	 */
	class ElementComparator<E extends Comparable<E>> implements Comparator<E> {
		@Override
		public int compare(E arg0, E arg1) {
			return arg0.compareTo(arg1);
		}
	}

	/**
	 * Sort an array arr[] using the insertion sort algorithm in the NON-DECREASING
	 * order.
	 * 
	 * @param arr  array storing elements from the list
	 * @param comp comparator used in sorting
	 */
	private void insertionSort(E[] arr, Comparator<? super E> comp) {
		// insertion sort algorithm
		for (int i = 1; i < arr.length; i++) {
			E temp = arr[i];
			int j = i - 1;
			// while j isn't out of bounds and j > key
			while (j >= 0 && (comp.compare(arr[j], temp)) > 0) {
				// move larger element
				arr[j + 1] = arr[j];
				j--;
			}
			arr[j + 1] = temp;
		}
	}

	/**
	 * Sort arr[] using the bubble sort algorithm in the NON-INCREASING order. For a
	 * description of bubble sort please refer to Section 6.1 in the project
	 * description. You must use the compareTo() method from an implementation of
	 * the Comparable interface by the class E or ? super E.
	 * 
	 * @param arr array holding elements from the list
	 */
	private void bubbleSort(E[] arr) {
		// bubble sort algorithm
		for (int i = 0; i < arr.length - 1; i++) {
			for (int j = 0; j < arr.length - i - 1; j++) {
				if (arr[j].compareTo(arr[j + 1]) < 0) {
					E temp = arr[j];
					arr[j] = arr[j + 1];
					arr[j + 1] = temp;
				}
			}
		}
	}

	/**
	 * Method used to "represent a node and offset" per the HW3 pdf
	 * 
	 * @author andrew mcmahon
	 *
	 */
	private class NodeInfo {
		public Node node;
		public int offset;

		/**
		 * Constructor for helper class
		 * 
		 * @param node,   the node of the element
		 * @param offset, the offset of the element
		 */
		public NodeInfo(Node node, int offset) {
			this.node = node;
			this.offset = offset;
		}

	}

	/**
	 * Helper method to find the "node and logical index" given the position of an
	 * element
	 * 
	 * @param pos, index of the element in the 1D array list of node data
	 * @return nodeInfo, the nodeInfo object which has the offset and node now
	 *         attached
	 */
	private NodeInfo find(int pos) {
		Node n = head.next;
		int arrayIndex = 0;
		while (n != tail) {
			// check if we are at the end of a node
			if (pos > n.count + arrayIndex) {
				arrayIndex += n.count;
				n = n.next;
				continue;
			}
			NodeInfo nodeInfo = new NodeInfo(n, pos - arrayIndex);
			return nodeInfo;
		}
		// if n is the tail, not found (return null)
		return null;
	}

}