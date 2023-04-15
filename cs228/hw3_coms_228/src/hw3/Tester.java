package hw3;


import static org.junit.jupiter.api.Assertions.*;

import java.util.Iterator;
import java.util.ListIterator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class Tester {
	StoutList<String> n;

	@BeforeEach
	void setUp() {
		n = new StoutList<>(); 
		n.add("A");  n.add("B");  n.add("1");	 n.add("2"); 
		n.add("C");	 n.add("D");  n.add("E");	 n.add("F");
	
		n.remove(2); 
		n.remove(2); 
		n.remove(5);
	}
	
	@Test
	/**
	 * tests sort() and reverseSort()
	 */
	public void sortTest() {
//		System.out.println(n.toStringInternal());
	
		n.sortReverse();
		assertEquals("[(E, D, C, B), (A, -, -, -)]",n.toStringInternal());
//		System.out.println(n.toStringInternal());
		
		n.sort();
		assertEquals("[(A, B, C, D), (E, -, -, -)]",n.toStringInternal());
//		System.out.println(n.toStringInternal());
		
	}
	
	@Test
	/**
	 * Tests fully iterating with StoutListIterator through the list with next() and previous()
	 * Also tests hasNext() and hasPrevious()
	 */
	public void listIterTests() {
		String s = "";
		ListIterator<String> iter = n.listIterator();
		
		while (iter.hasNext()) {
			s += iter.next();
		}
		assertEquals("ABCDE", s);
		
		s = "";
		
		while (iter.hasPrevious()) {
			s += iter.previous();
		}
		assertEquals("EDCBA", s);
	}
	
	@Test
	/**
	 * Tests the set() method of StoutListIterator after both next() and previous()
	 * Also tests that set() is able to be called multiple times in a row without problem
	 * (unlike add and remove)
	 */
	public void setTest() {
		ListIterator<String> iter = n.listIterator();
		
		iter.next();
		iter.set("Q");
		assertEquals("[(Q, | B, -, -), (C, D, E, -)]",n.toStringInternal(iter));
		iter.set("N");
		assertEquals("[(N, | B, -, -), (C, D, E, -)]",n.toStringInternal(iter));
		
		iter.next();
		iter.next();
		iter.next();
		iter.previous();
		iter.set("P");
		assertEquals("[(N, B, -, -), (C, | P, E, -)]",n.toStringInternal(iter));
		iter.set("L");
		assertEquals("[(N, B, -, -), (C, | L, E, -)]",n.toStringInternal(iter));	
	}
	
	/**
	 * Tests functionality of StoutIterator
	 */
	@Test
	public void iterTests() {
		Iterator<String> iter = n.iterator();
		String s = "";
		
		while (iter.hasNext()) {
			s += iter.next();
		}
		assertEquals("ABCDE", s);
	}
	
	@Test
	/**
	 * Example add and remove from the pdf but using StoutListIterator. Tests add() and remove()
	 * after both next() and previous()
	 */
	public void pdfExampleIter() {
		ListIterator<String> iter = n.listIterator(n.size());
//		System.out.println(n.toStringInternal(iter));
		
		iter.add("V");
		assertEquals("[(A, B, -, -), (C, D, E, | V)]",n.toStringInternal(iter));
//		System.out.println(n.toStringInternal(iter));
		
		iter.next();
//		System.out.println(n.toStringInternal(iter));
		
		iter.add("W");
		assertEquals("[(A, B, -, -), (C, D, E, V), (| W, -, -, -)]",n.toStringInternal(iter));
//		System.out.println(n.toStringInternal(iter));
		
		iter.previous();
		iter.previous();
		iter.previous();
		iter.previous();
		iter.add("X");
		assertEquals("[(A, B, | X, -), (C, D, E, V), (W, -, -, -)]",n.toStringInternal(iter));
//		System.out.println(n.toStringInternal(iter));
		
		iter.add("Y");
		assertEquals("[(A, B, | Y, X), (C, D, E, V), (W, -, -, -)]",n.toStringInternal(iter));
//		System.out.println(n.toStringInternal(iter));
		
		iter.add("Z");
		assertEquals("[(A, B, | Z, -), (Y, X, -, -), (C, D, E, V), (W, -, -, -)]",n.toStringInternal(iter));
//		System.out.println(n.toStringInternal(iter));
			
//		System.out.println();
		
		iter.next();
		iter.next();
		iter.next();
		iter.next();
		iter.next();
		iter.next();
		iter.next();
		iter.next();
		iter.remove();
		assertEquals("[(A, B, Z, -), (Y, X, -, -), (C, D, E, V |)]",n.toStringInternal(iter));
//		System.out.println(n.toStringInternal(iter));
		
		iter.previous();
		iter.previous();
		iter.previous();
		iter.previous();
		iter.previous();
		iter.previous();
		iter.remove();
		assertEquals("[(A, B, Z, -), (| X, C, -, -), (D, E, V, -)]",n.toStringInternal(iter));
//		System.out.println(n.toStringInternal(iter));
		
		iter.next();
		iter.remove();
		assertEquals("[(A, B, Z, -), (| C, D, -, -), (E, V, -, -)]",n.toStringInternal(iter));
//		System.out.println(n.toStringInternal(iter));
		
		iter.next();
		iter.next();
		iter.next();
		iter.remove();
		assertEquals("[(A, B, Z, -), (C, D, -, -), (| V, -, -, -)]",n.toStringInternal(iter));
//		System.out.println(n.toStringInternal(iter));
		
		iter.previous();
		iter.previous();
		iter.remove();
		assertEquals("[(A, B, Z, -), (| D, V, -, -)]",n.toStringInternal(iter));
//		System.out.println(n.toStringInternal(iter));
		
	}

	
	@Test
	/**
	 * Example from the pdf, taken from another student on Piazza (thanks!)
	 */
	public void pdfExample() {		
		//After calling remove(5) you are at the start for the pdf example
		n.add("V");
		assertEquals("[(A, B, -, -), (C, D, E, V)]",n.toStringInternal());
		n.add("W");
		assertEquals("[(A, B, -, -), (C, D, E, V), (W, -, -, -)]",n.toStringInternal());
		n.add(2,"X");
		assertEquals("[(A, B, X, -), (C, D, E, V), (W, -, -, -)]",n.toStringInternal());
		n.add(2,"Y");
		assertEquals("[(A, B, Y, X), (C, D, E, V), (W, -, -, -)]",n.toStringInternal());
		n.add(2,"Z");
		assertEquals("[(A, B, Z, -), (Y, X, -, -), (C, D, E, V), (W, -, -, -)]",n.toStringInternal());
	//	System.out.println(n.toStringInternal());


		//Examples removing elements from a list

		//Removes W
		n.remove(9);
	//	System.out.println(n.toStringInternal());
		assertEquals("[(A, B, Z, -), (Y, X, -, -), (C, D, E, V)]",n.toStringInternal());

		//Removes Y
		n.remove(3);
	//	System.out.println(n.toStringInternal());
		assertEquals("[(A, B, Z, -), (X, C, -, -), (D, E, V, -)]",n.toStringInternal());

		//Removes X (mini merge)
		n.remove(3);
	//	System.out.println(n.toStringInternal());
		assertEquals("[(A, B, Z, -), (C, D, -, -), (E, V, -, -)]",n.toStringInternal());
	//	System.out.println(n.toStringInternal());

		//Removes E (No merge with predecessor Node)
		n.remove(5);
	//	System.out.println(n.toStringInternal());
		assertEquals("[(A, B, Z, -), (C, D, -, -), (V, -, -, -)]",n.toStringInternal());
	//	System.out.println(n.toStringInternal());

		//Removes C (Full merge with successor Node)
		n.remove(3);
	//	System.out.println(n.toStringInternal());
		assertEquals("[(A, B, Z, -), (D, V, -, -)]",n.toStringInternal());
	//	System.out.println(n.toStringInternal());

	}
}

