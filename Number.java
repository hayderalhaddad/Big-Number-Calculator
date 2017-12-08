package bigcalc;

/**
 *
 * @author djn
 */
public class Number {
	private Node low, high; // head and tail
	private int decimalPlaces = 0; // must always be >= 0
	private int digitCount = 0; //count all digits
	private boolean negative = false;

	public Number() {
	}

	public Number(String str) {
		if (!validate(str))
			throw new BadNumberException("Invalid number: " + str);
		else
			accept(str);
	}

	/**
	 * Tests a string to see if it represents a valid number
	 * 
	 * @param str
	 *            String to test for valid number
	 * @return whether valid or not
	 */
	private boolean validate(String str) {
		// TODO: implement this
		if (str.matches("[-]?[0-9]*\\.?[0-9]+")) //check number format
			return true;
		else {
			throw new BadNumberException("Invalid number: " + str); // throw if format is wrong
			// return false;
		}
	}

	/**
	 * Constructs a Number from input string. This method assumes that str
	 * represents a valid number
	 * 
	 * @param str
	 *            String representing a valid number
	 */
	public void accept(String str) {
		if (!validate(str))
			throw new BadNumberException("Invalid number: " + str);
		else {
			decimalPlaces = 0; //reset decimal count; 
			negative=false;
			int neg = 0; // find first digit after sign
			boolean dec = false; // detects if decemal point was found
			if (str.charAt(0) == '-') { //if number is negative, skip sign. 
				neg = 1;
				negative = true;

			}
			if (str.charAt(neg) == '.' && negative) {
				str = str.substring(1);
				str = '0' + str; // if number starts with . ,  -. 
				str = '-' + str;
			} else if (str.charAt(neg) == '.') {
				str = '0' + str; 
			}
			this.high = new Node(str.charAt(neg) - '0'); // set Head of node. 
			digitCount = 1;
			Node current = high;
			for (int i = neg + 1; i < str.length(); i++) {//iterate over string
				if (str.charAt(i) == '.') {
					dec = true; //past decimal point 
					continue;
				}
				if (dec == true)
					decimalPlaces++;
				digitCount++;
				Node point = new Node(str.charAt(i) - '0'); //add rest of numbers to list
				point.prev = current; 
				current.next = point;
				current = point;

			}
			this.low = current;
			trim();  //trim
			toString(); //print
		}
	}

	public Number add(Number n) {
		Number sum = new Number();

		// TODO: implement this
		int larger = compareToAbsolute(n);
		if (this.negative && n.negative) { // if bot numbers are negative add, and reverse sign 
			sum = addAbsolute(n);
			sum.reverseSign();
		} else if (this.negative || n.negative) {
			n.reverseSign(); // subtract if one of numbers is negative
			sum = subtract(n);
		} else {
			sum = addAbsolute(n); //if both positive call addAbsolute 
		}

		accept(sum.toString());  //convert to string and accept 

		return this;
	}

	public Number subtract(Number n) {
		Number difference = new Number();
		int larger = compareToAbsolute(n);
		if ((this.negative && n.negative)) {
			n.reverseSign(); //if both numbers are negatiive , change them to positve 
			this.reverseSign();
			difference = n.subtract(this);

		} else if (!this.negative && n.negative) {
			difference = addAbsolute(n); //add if n is negatve ( -- = +)
		} else if (this.negative && !n.negative) {
			n.reverseSign(); //if this is negative and n is positive add them 
			difference = addAbsolute(n);
			difference.reverseSign(); //change sign back to negative
		} else if (larger == -1) {
			difference = n.subtractAbsolute(this); //if this < n
			difference.reverseSign(); 
		} else {
			difference = subtractAbsolute(n); //if this > n 
		}
		accept(difference.toString());
		// TODO: implement this
		return this;
	}

	public Number multiply(Number n) {
		// emulate "paper and pencil" multiplication
		Number product = new Number(); //final product
		compareToAbsolute(n);
		int carry = 0; //carry
		int d = n.high.value; //current int
		Node nPtr = n.high;
		do {
			Node temp = this.low; //Node to multiply with d
			int partial=0;
			Number pro = new Number(); // partial product
			while (temp != null) {
				 partial = (d * temp.value) + carry;
				carry = partial / 10;
				partial = partial % 10;
				pro.insertHigh(partial);
				temp = temp.prev;
			}

			while(carry != 0) { //add  remaining carry
				pro.insertHigh(carry%10);
				carry/=10;
		}
			//if(nPtr.next!= null)
			product.insertLow(0);			
			product = product.addAbsolute(pro); //add partial products to product
			nPtr = nPtr.next; //next partial product Node
			if (nPtr != null)
				d = nPtr.value;
		} while (nPtr != null);
		// TODO: this is an optional part

		if (carry != 0)
			product.insertHigh(carry);
		if(n.negative&&this.negative) //fix signs
			product.negative= false;
		else if(n.negative||this.negative) 
			product.negative = true;
		product.decimalPlaces = n.decimalPlaces + decimalPlaces;
		//System.out.println(decimalPlaces + "   " + n.decimalPlaces);
		product.trim();
		accept(product.toString());
		return product;
	}

	public void reverseSign() {
		negative = !negative;
	}

	// compare |this| and |n|
	private int compareToAbsolute(Number n) {
		// perform comparison disregarding signs
		// return 0 if |this| == |n|
		// return 1 if |this| > |n|
		// return -1 if |this| < |n|

		int nDec = n.digitCount - n.decimalPlaces; // digits perceding the .
		int cDec = digitCount - decimalPlaces; // digits pereceding the .

		//this part is alligning the two numbers
		while (cDec > nDec) {
			n.insertHigh(0);  //insert on head for n
			nDec = n.digitCount - n.decimalPlaces;
		}

		while (nDec > cDec) {
			insertHigh(0); // insert for head on this
			cDec = n.digitCount - n.decimalPlaces;
		}

		while (n.decimalPlaces > decimalPlaces) {
			insertLow(0); // insert tail. 
			decimalPlaces++;

		}
		while (decimalPlaces > n.decimalPlaces) {
			n.insertLow(0);
			n.decimalPlaces++;
			
		}
		while (n.digitCount > this.digitCount) {
			insertHigh(0); //allign head
		}
		while (this.digitCount > n.digitCount) {
			n.insertHigh(0);
		}
		
		Node nHead = n.high;
		Node cHead = high;
		for (int i = 0; i < digitCount; i++) { //actually comparing values
			if (cHead.value > nHead.value)
				return 1; 
			else if (cHead.value < nHead.value) {
				return -1;
			} else {
				nHead = nHead.next;
				cHead = cHead.next;
			}
		}

		return 0;
	}
	
	// compute |this|+|n|
	private Number addAbsolute(Number n) {
		Number sum = new Number();
		// TODO: implement this
		this.compareToAbsolute(n); //allign using compare
		int carry = 0;
		Node thisPtr = this.low;
		Node nPtr = n.low;
		while (thisPtr != null) {
			int add = nPtr.value + thisPtr.value + carry; /// find partial product and add carry
			carry = add / 10;
			add = add % 10;
			sum.insertHigh(add);
			thisPtr = thisPtr.prev; // iterate over all nodes
			nPtr = nPtr.prev;
		}
		if (carry != 0) {
			sum.insertHigh(1); //insert remaining carry
		}
		sum.decimalPlaces = decimalPlaces;

		//sum.trim();
		return sum;
	}

	// compute |this|-|n|
	private Number subtractAbsolute(Number n) {
		// assumes that abs(this) >= abs(n)
		Number difference = new Number();

		// TODO: implement this
		int borrow = 0;
		Node thisPtr = low;
		Node nPtr = n.low; 

		while (thisPtr != null) {
			int sub = (thisPtr.value - borrow) - nPtr.value; // subtract
			if (sub < 0) {
				sub += 10;  //borrow from other number
				borrow = 1;
			} else {
				borrow = 0;
			}
			difference.insertHigh(sub);
			thisPtr = thisPtr.prev; // iterate
			nPtr = nPtr.prev;
		}
		difference.decimalPlaces = decimalPlaces;
		difference.trim();
		return difference;
	}

	/**
	 * Places new digit at high order position of Numbe
	 * 
	 * @param digit
	 *            digit to append
	 */
	private void insertHigh(int digit) {
		Node n = new Node(digit);
		// TODO: insert at head of list
		if (this.high == null) { //if list is empty then n is head and tail
			this.high = n;
			this.low = n;
			digitCount++;

		} else { // if list is not empty add new head
			n.next = high;
			high.prev = n;
			high = n; 
			digitCount++;
		}
	}

	/**
	 * Places new digit at low order position of Number
	 * 
	 * @param digit
	 *            digit to append
	 */
	private void insertLow(int digit) {
		Node n = new Node(digit);
		// TODO: insert at end of list
		if (low == null) { //if list is empty n is head and tail 
			low = n;
			high =n;
			digitCount++;
		} else {
			low.next = n;
			n.prev = low;
			low = n;
			digitCount++;
			//decimalPlaces++;
		}
	}

	/**
	 * removes leading 0s
	 */
	private void trim() {
		Node p = this.high;
		Node l = this.low;
		if (decimalPlaces > 0) { //trim lower 
			while (l.value == 0 && l.prev != null) {
				low = low.prev;
				low.next = null;
				l = low;
				decimalPlaces--;
				digitCount--;
			}
		}
		// remove leading 0s preceding decimal point
		if ((digitCount - decimalPlaces) > 0) { 
			while (p.value == 0 && p.next != null && digitCount > decimalPlaces) {
				high = high.next;
				high.prev = null;
				p = high;
				digitCount--;
			}
		}
	}

	/**
	 * For displaying Number in human readable form
	 * 
	 * @return Stringf representation of Number
	 */
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		int localDigitCount = 0;
		int countToPoint = 0;
		// how far to decimal point?
		countToPoint = digitCount - decimalPlaces;
		Node ptr = high;

		if (negative)
			sb.append('-');
		while (ptr != null) {

			if (localDigitCount == countToPoint)
				sb.append('.');
			sb.append(ptr.value);
			ptr = ptr.next;
			localDigitCount++;
		}

		return sb.toString();

	}

	private class Node {
		public int value; // 0~9
		public Node prev, next;

		public Node(int value) {
			this.value = value;
		}
	}

}
