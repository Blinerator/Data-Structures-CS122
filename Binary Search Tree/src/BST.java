
/**
 * A Binary Search Tree is a Binary Tree with the following
 * properties:
 * 
 * For any node x: (1) the left child of x compares "less than" x; 
 * and (2) the right node of x compares "greater than" x
 *
 *
 * @param <T> the type of data object stored by the BST's Nodes
 */
public class BST<T extends Comparable<T>> {

	/**
	 * The root Node is a reference to the 
	 * Node at the 'top' of a QuestionTree
	 */
	private Node<T> root;
	
	
	/**
	 * Construct a BST
	 */
	public BST() {
		root = null;
	}
	
	/**
	 * @return the root of the BST
	 */
	public Node<T> getRoot() { 
		return root;
	}
	
	/**
	 * _Part 1: Implement this method._
	 *
	 * Add a new piece of data into the tree. To do this, you must:
	 * 
	 * 1) If the tree has no root, create a root node 
	 *    with the supplied data
	 * 2) Otherwise, walk the tree from the root to the bottom
	 *    (i.e., a leaf) as though searching for the supplied data.
	 * Then:
	 * 3) Add a new Node to the leaf where the search ended.
	 *
	 * @param data - the data to be added to the tree
	 */
	public void add(T data) {
		if(root==null) {//create root if none exists
			Node<T> r=new Node<T>(data);
			root=r;
		}

		Node<T> cur = root;//create a cursor

		while(cur.data != data){//keep walking until we either find the data or add it ourselves (at which point the cursor would be on the data)
			if(cur.data.compareTo(data)>0){//go left or right depending on data
				if(cur.left==null){//if there is no path, this is where we put the data
					Node<T> n=new Node<T>(data);
					n.parent=cur;
					n.parent.left=n;
				}
				cur=cur.left;
			}
			if(cur.data.compareTo(data)<0){//go left or right depending on data
				if(cur.right==null){//if there is no path, this is where we put the data
					Node<T> n=new Node<T>(data);
					n.parent=cur;
					n.parent.right=n;
				}
				cur=cur.right;
			}
		}

		return;
	}

	/**
	 * _Part 2: Implement this method._
	 * 
	 * Find a Node containing the specified key and
	 * return it.  If such a Node cannot be found,
	 * return null.  This method works by walking
	 * through the tree starting at the root and
	 * comparing each Node's data to the specified 
	 * key and then branching appropriately.
	 * 
	 * @param key - the data to find in the tree
	 * @return a Node containing the key data, or null.
	 */
	public Node<T> find(T key) {
		Node<T> cur = root;//create a cursor
		Node<T> k=null;//this is what we return
		if(root.data==key){return root;}//check if the root is the data

		while(((cur.left != null) || (cur.right != null))){//keep walking until we reach a leaf
			if(cur.data.compareTo(key)>=0){//go left or right depending on data
				if(cur.left!=null) {
					cur = cur.left;
					if(cur.data.compareTo(key)==0){//check if data has been found
						k=cur;
					}
				}
				else if(cur.right!=null){//no matter what, we keep traversing so we can exit the loop
					cur=cur.right;
				}
			}
			if(cur.data.compareTo(key)<=0){//go left or right depending on data
				if(cur.right!=null) {
					cur = cur.right;
					if(cur.data.compareTo(key)==0){//check if data has been found
						k=cur;
					}
				}
				else if(cur.left!=null){//no matter what, we keep traversing so we can exit the loop
					cur=cur.left;
				}
			}
		}

		return k;
	}

	/**
	 * _Part 3: Implement this method._
	 *  
	 * @return the Node with the largest value in the BST
	 */
	public Node<T> maximum() {
		Node<T> cur = root;//create a cursor
		while(cur.right!=null){//max value will be found at far right of tree
			cur=cur.right;
		}
		return cur;
	}
	
	/**
	 * _Part 4: Implement this method._
	 *  
	 * @return the Node with the smallest value in the BST
	 */
	public Node<T> minimum() {
		Node<T> cur = root;//create a cursor
		while(cur.left!=null){//min value will be found at far left of tree
			cur=cur.left;
		}
		return cur;
	}

	/**
	 * _Part 5: Implement this method._
	 *
	 *  Assuming no two nodes contain equal data in the tree,
	 *  this method takes a node n with data d, and should
	 *  return the node that stores the value which appears
	 *  immediately after d in the sorted order, or null
	 *  if no such node exists.
	 *
	 *  Thus, for a tree containing data: 1, 3, 5, 10 (regardless
	 *  of the order they were added) the successor of the node
	 *  containing 1 should be the node containing 3.
	 *
	 * @param n a node in the BST
	 * @return the Node in the BST whose value is next in the sorted ordering
	 *   of all keys, or null if no such node exists
	 */
	public Node<T> successor(Node<T> n) {
		Node<T> cur = n;//create a cursor
		int LR = 3; // 0 L ; 1 R ; 3 root
		T temp = null;
		if (cur.parent != null) {//figure out if we're in the left or right side of the tree
			while (cur.parent != null) {
				if (cur.equals(cur.parent.left)) {
					LR = 0;
				} else {
					LR = 1;
				}
				cur = cur.parent;
			}
		}

		cur=n;
		if(cur.right!=null){//first go down left-most right branch
			if(LR==0){//find automatic next successor (the "reference" successor).  In any case it must be greater than "n.data"
				temp=root.data;
			}
			else if(LR==1 || LR==3){
				temp=n.right.data;
			}
			cur=n.right;
			//the rest of this 'if statement' goes down the right side of the tree, taking the left-most path.
			//if a value is found that is less than the reference data value, it replaces the temp value as the successor.
			if(cur.data.compareTo(temp)<0){
				temp=cur.data;
			}
			while(((cur.left != null) || (cur.right != null))){//keep going until a leaf is reached
				if(cur.left!=null){
					cur=cur.left;
					if(cur.data.compareTo(temp)<0){//check if this node has smaller data than the reference value
						temp=cur.data;
					}
				}
				else if(cur.right!=null){
					cur=cur.right;
					if(cur.data.compareTo(temp)<0){//check if this node has smaller data than the reference value
						temp=cur.data;
					}
				}
			}
		}
		//next up, if the node is on the left side of the tree, we also need to go up the tree until we hit the root,
		//checking each data value against the reference
		cur=n;
		if(LR==0) {
			if (cur.parent != null) {
				//if reference hasn't been assigned yet, make it the root, as the root is always greater
				//than any value on the left side
				if (temp == null) {
					temp = root.data;
				}
				while (cur.parent != null) {//keep going until root is reached, checking data values against reference
					cur = cur.parent;
					if (cur.data.compareTo(temp) < 0 && cur.data.compareTo(n.data) > 0) {
						temp = cur.data;
					}
				}

			}
		}
		if(temp!=null){return find(temp);}//if a successor data value has been found, find its node and return it.
		return null;
	}
	/**
	 * _Part 6: Extra Credit! Implement this method._
	 *  
	 * Searches for a Node with the specified key.
	 * If found, this method removes that node
	 * while maintaining the properties of the BST.
	 * 
	 * Note that the CLRS TreeDelete pseudo code
	 * nearly meets the requirements of this method, 
	 * however, it does not always return the correct
	 * value; your implementation should return a
	 * a reference to the removed Node.
	 *
	 * @return the Node that was removed or null.
	 */
	public Node<T> remove(T data) {
		if(root==null){return null;}
		Node<T> cur = find(data);
		if(cur==null){return null;}//if node doesn't exist, return null

		if(cur.parent!=null && ((cur.left==null) || (cur.right==null))){//case 1: one or no children
			if(cur.parent.left!=null && cur.parent.left.equals(cur)){//if node is left of parent
				if(cur.left!=null){//re-link to left branch of node if it exists
					cur.parent.left=cur.left;
				}
				else if(cur.right!=null){//re-link to right branch of node if it exists
					cur.parent.left=cur.right;
				}
				else{//if node has no children, parent will be linked to null
					cur.parent.left=null;
				}
			}
			else{//if node is right of parent
				if(cur.left!=null){//re-link to left branch of node if it exists
					cur.parent.right=cur.left;
				}
				else if(cur.right!=null){//re-link to right branch of node if it exists
					cur.parent.right=cur.right;
				}
				else{//if node has no children, parent will be linked to null
					cur.parent.right=null;
				}
			}
		}

		else if(cur.parent!=null && (cur.left!=null && cur.right!=null)){//case 2: two children
			Node<T> p = cur.parent;
			Node<T> l = cur.left;
			Node<T> r = cur.right;

			if(p.right.equals(cur)) {//if node is on right of parent, re-order in such a way that the two children are placed in order of magnitude
				p.right = l;
				l.parent = p;

				r.parent = l;
				l.right = r;
			}
			else{
				//if node is on left of parent, re-order in such a way that the two children are placed in order of magnitude
				//the order is reversed from the above method
				p.left=r;
				r.parent=p;

				l.parent=r;
				r.left=l;
			}
		}

		else if(cur.parent==null){//case 3: node is the root
			//for this re-ordering, we'll walk the left-most branch of the right side of the tree
			//the left side of the tree will be appended to the end of this branch
			Node<T> cur2 = cur;//new cursor to walk the tree
			if(cur2.right!=null && cur2.left!=null) {
				cur2 = cur2.right;

				while (((cur2.left != null) || (cur2.right != null))) {//walk the left-most branch of right side
					if (cur2.left != null) {
						cur2 = cur2.left;
					} else if (cur2.right != null) {
						cur2 = cur2.right;
					}
				}
				//at this point cur2 is the bottom of the tree
				cur2.left = cur.left;
				cur.right.parent = null;//new root
				root = cur.right;
				cur.left.parent = cur2;
			}
			else if(cur2.right==null && cur2.left!=null){
				cur2.left=root;
				root.parent=null;
			}
			else if(cur2.right!=null && cur2.left==null){
				cur2.right=root;
				root.parent=null;
			}
			else if(cur2.right==null && cur2.left==null){
				root=null;
			}
		}

		return cur;
	}
	
	/**
	 * 
	 * The Node class for our BST.  The BST
	 * as defined above is constructed from zero or more
	 * Node objects. Each object has between 0 and 2 children
	 * along with a data member that must implement the
	 * Comparable interface.
	 * 
	 * @param <T>
	 */
	public static class Node<T extends Comparable<T>> {
		private Node<T> parent;
		private Node<T> left;
		private Node<T> right;
		private T data;
		
		private Node(T d) {
			data = d;
			parent = null;
			left = null;
			right = null;
		}
		public Node<T> getParent() { return parent; }
		public Node<T> getLeft() { return left; }
		public Node<T> getRight() { return right; }
		public T getData() { return data; }
	}
}
