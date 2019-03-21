

class FibNode
		{
			// This class represents the logical structure of a Node of the Fibonacci Heap
			int key; 		//Key Value (Number of Hits for a keyword)
			FibNode child; 	// Pointer to a child of the Node
			FibNode parent;	// Pointer to a parent of the Node
			boolean mark;	// Child-cut mark value		
			int degree;		// Number of children of the Node
			FibNode left;	// Pointer to the left sibling of the Node
			FibNode right;	// Pointer to the right sibling of the Node
			FibNode(int d)
			{
				this.key=d;
				this.child=null;
				this.parent=null;
				this.degree=0;
				this.left=this;
				this.right=this;
				this.mark=false;
			}
		}