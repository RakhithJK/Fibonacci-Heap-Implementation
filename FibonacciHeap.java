import java.util.*;
public class FibonacciHeap {
	
	// This class represents the Structure of a Fibonacci Heap and all the operations that can be performed on it
		int n; 			// number of nodes
		FibNode max;	//pointer to max element
		FibonacciHeap()
		{
			n=0;
			max=null;
		}
		
	public FibNode insertNode(int data)
	{
		// Method to insert a new Node into the heap
		FibNode new_node = new FibNode(data); 
		if (max==null)	// If the heap is empty before Insert, insert the Node as the max element
		{
			max=new_node;
		}
		else
		{
			new_node.left=max;
			max.right.left=new_node;
			new_node.right=max.right;
			max.right=new_node;
			if(new_node.key>max.key) 	// If newly inserted node has higher key value than the current max, then it becomes the new max Node
			{
				max=new_node;
			}
		}
		n++;
		return new_node;
	}
	public FibNode increaseKey(FibNode theNode,int newKey)
	{
		//Method to increase Key value of a given Node
		if(newKey<theNode.key)
		{
			//New key value can't be lesser than the older value
			System.out.println("The New Key cannot be lesser than original key value!");
		}
		theNode.key=newKey;
		FibNode p = theNode.parent;
		if (p!=null && theNode.key>p.key)
		{
			//Cut and Cascading cut methods are triggered on increment of Key
			cut(theNode,p);
			cascadingCut(p);
		}
		if (theNode.key>max.key) // Check for re-assignment of the max pointer
		{
			max=theNode;
		}
		return theNode;
	}
	public void cut(FibNode theNode, FibNode p)
	{
		// Supporting method for a "Child-Cut" operation
		p.degree--;
		
		// removing the Node from its sibling list
		theNode.left.right=theNode.right;
		theNode.right.left=theNode.left;
		if(theNode==p.child) // preserve the child-list of the Parent of "theNode"
		{
			p.child=theNode.right;
		}
		// inserting the node into the roots' sibling list
		if (max==null)
		{
			max=theNode;
			theNode.left=theNode;
			theNode.right=theNode;
		}
		else
		{
			theNode.left=max;
			max.right.left=theNode;
			theNode.right=max.right;
			max.right=theNode;
			if(theNode.key>max.key)
			{
				max=theNode;
			}
		}
		theNode.parent=null;
		theNode.mark=false;
		
		// check for re-assignment of Max pointer
		if (theNode.key>max.key)
		{
			max=theNode;
		}
	}
	public void cascadingCut(FibNode p)
	{
		// Supporting method for a Casading Cut resulting from Child-Cut for a Parent Node
		FibNode gp = p.parent;
		if (gp!=null)
		{
			//Set child-cut mark to True if its False initially. To denote child-loss
			if(p.mark==false)
			{
				p.mark=true;
			}
			// If the parent has already lost a child, trigger a cut for it from its parent and subsequent cascading cut checks
			else cut(p,gp);
			cascadingCut(gp);
		}
	}
	public FibNode extractMax()
	{
		// Method to remove the Max element from the Fibonacci Heap
		FibNode tempMax = max; // a pointer to the max element
		if (tempMax!=null)
		{
			// if the max-element's children exist, they are moved to the root list
			FibNode maxChild=tempMax.child;
			int numberOfChildren = tempMax.degree;
			FibNode sibling;
			while(numberOfChildren>0)
			{
				sibling = maxChild.right;
				maxChild.left.right=maxChild.right;
				maxChild.right.left=maxChild.left;
				
				maxChild.left=max;
				maxChild.right=max.right;
				max.right=maxChild;
				maxChild.right.left=maxChild;
			
				maxChild.parent = null;
				maxChild=sibling;
				numberOfChildren--;
			}
			
			// removing the max node from its sibling list
			tempMax.left.right=tempMax.right;
			tempMax.right.left=tempMax.left;
			
			if (tempMax.right==tempMax)
			{
				max=null;
				
			}
			else
			{
				max=tempMax.right;
				// Degree- based Pairwise combine operation triggered
				consolidate();
			}
			n--;
			return tempMax;
		}
		return null;
	}
	public void consolidate()
	{
		// Method to combine siblings of the removed max-element
		// Individual heaps(Nodes) are combined based on their degrees
		// ie. Nodes with equal degrees are melded together
		
		// A table containing pointers to heaps with degrees belonging to each of the possible values from 0 to O(log n)
		// Size calculated based on the formulae in Cormen Textbook
		int sizeOfDegreeRecordTable = (int) Math.round(Math.log(n)/Math.log(1.62));
		ArrayList<FibNode> degreeRecordTable = new ArrayList<FibNode> (sizeOfDegreeRecordTable);
		for (int i=0;i<sizeOfDegreeRecordTable;i++)
		{
			degreeRecordTable.add(null);
		}
		
		// Counting number of roots in the heap
		int rootCount = 0;
		FibNode x = max;
		if (x != null)
		{
			rootCount++;
			x = x.right;
			while(x!=max)
			{
				rootCount++;
				x=x.right;
			}
		}
		
		// pairwise combining of roots
		while(rootCount>0)
		{
			FibNode next = x.right;
			int d = x.degree;
			for(;;)
			{
				FibNode y = degreeRecordTable.get(d);
				if(y==null)
				{
					break;
				}
				
				// Meld operation invloving making one Node a child of another based on key values
				if(x.key<y.key)
				{
					FibNode temp = x;
					x = y;
					y = temp;
				}
				fibHeapLink(y,x); // Call to method that performs Make Child operation
				degreeRecordTable.set(d,null); // the degree pointer is set to null which can be used for a subsequent node having the same degree
				d++; 
			}
			degreeRecordTable.set(d,x);
			x=next;
			rootCount--;
		}
		
		max=null;
		// reconstructing the heap from the created degree record table
		for (int i=0;i<sizeOfDegreeRecordTable;i++)
		{
			FibNode y = degreeRecordTable.get(i);
			if (y == null)
			{
				continue;
			}
			y.left.right = y.right;
            y.right.left = y.left;
            
            // Insert into root-list
            if (max==null)
    		{
    			max=y;
    			y.left=y;
    			y.right=y;
    		}
    		else
    		{
    			y.left=max;
    			max.right.left=y;
    			y.right=max.right;
    			max.right=y;
    			if(y.key>max.key)
    			{
    				max=y;
    			}
    		}
		}
	}
	public void fibHeapLink(FibNode y, FibNode x)
	{
		// Supporting method to make one Node a child of Another
		y.right.left=y.left;
		y.left.right=y.right;
		y.parent=x;
		if(x.child==null)
		{
			x.child=y;
			y.left=y;
			y.right=y;
		}
		else
		{
			y.left = x.child;
            x.child.right.left=y;
            y.right = x.child.right;
            x.child.right = y;
		}
		x.degree++;
		y.mark=false;
	}
}
