import java.util.Scanner;
/**
 * This algorithm is for Range update and range query on arrays using Lazy propogation in segment trees
 * Also aims to solve HORRIBLE question at SPOJ
 * @author Vivek
 *
 */
public class SegmentTreeLazyPropogation {
	static int MAX_ARR_SIZE=100001;
	static int MAX_TREE_SIZE=280000;
	static long arr[]= new long [MAX_ARR_SIZE];
	static long tree[]= new long [MAX_TREE_SIZE];
	static long lazyTree[]= new long [MAX_TREE_SIZE]; //stores pending updates in trees
	
	public static void main(String args[]) {
		Scanner sc= new Scanner(System.in);
		long t=sc.nextInt();
		while(t-->0) {
			long n=sc.nextInt();
			long c=sc.nextInt();
			arr= new long [MAX_ARR_SIZE];      
			tree= new long [MAX_TREE_SIZE];    
			lazyTree= new long [MAX_TREE_SIZE];
			//buildTree(0,n-1,0); //not required as all elements of  array are 0
			//for all commands
			while(c-->0) {
				long type=sc.nextInt();
				if(type==0) {
					//add value to the range
					long p=sc.nextInt();
					long q=sc.nextInt();
					long value=sc.nextInt();
					
					rangeUpdateTree(p-1,q-1,0,n-1,value,0);
				} else {
					//for  query of sum with range
					long p=sc.nextInt();
					long q=sc.nextInt();
					
					System.out.println(queryTree(p-1, q-1, 0, n-1, 0));
				}
			}
		}
		
	}
	/**
	 * Update in range with lazy propogation
	 * @param p and q is the range where updates should occur with @param value.
	 * @param nodeStart and nodeEnd is the current nodes start and end 
	 * @param position is the index of current node in the tree
	 */
	private static void rangeUpdateTree(long left, long right, long nodeStart, long nodeEnd, long value, int position) {
		//lazy update if it is pending
		//return if node is invalid for update.
		//if fully in range update and pass values to child.
		//else if partial update left child and right child
		
		if(lazyTree[position]!=0) {
			//logic to update value on the root tree on range update
			tree[position] = tree[position] + (nodeEnd-nodeStart +1) * lazyTree[position]; //all the range of leaf would be updated 
			//push the pending updates of parents to  its left and right child if it is not a leaf node
			if(nodeStart!=nodeEnd) {
				lazyTree[2*position+1] +=lazyTree[position];
				lazyTree[2*position+2] +=lazyTree[position];
			}
			//IMPORTANT set back lazyof current node to 0
			lazyTree[position]=0;
		}
		//invalid node for update range
		if(nodeStart > right || nodeEnd < left) {
			return;
		}
		//if node is completely within range to update
		if(nodeStart >= left && nodeEnd <= right) {
			// update the current node with value across all nodes withing the roo
			tree[position] = tree[position] + (nodeEnd-nodeStart+1)*value;  
			//if not leaf node pass on the pending update  value to lazy tree of childs
			if(nodeEnd!=nodeStart) {
				lazyTree[2*position+1] +=value;
				lazyTree[2*position+2] +=value;
			}
			
			return;
		}
		//if partial overlaps update parent after updating both childs
		long mid=(nodeEnd+nodeStart)/2;
		int leftChildPosition = position*2+1;
		int rightChildPosition = position*2+2;
		rangeUpdateTree(left, right, nodeStart, mid, value, leftChildPosition);
		rangeUpdateTree(left, right, mid+1, nodeEnd, value, rightChildPosition);
		
		//update function sum
		tree[position]= tree[leftChildPosition] + tree[rightChildPosition]; 
	}
	
	/**
	 * Query would be same as we used to but just with lazy update at start
	 * @param left and @param right is the asked query range solution
	 * @param start and @param end will be the node's range values
	 * @param position is the index where the nodes range ans is stored in tree array
	 * @return
	 */
	private static long queryTree(long left, long right, long nodeStart, long nodeEnd, int position) {
		//perform lazyupdate if present, then query for the range with sum
		if(lazyTree[position]!=0) {
			//logic to update value on the root tree on range update
			tree[position] = tree[position] + (nodeEnd-nodeStart +1) * lazyTree[position]; //all the range of leaf would be updated 
			//push the pending updates of parents to  its left and right child if it is not a leaf node
			if(nodeStart!=nodeEnd) {
				lazyTree[2*position+1] +=lazyTree[position];
				lazyTree[2*position+2] +=lazyTree[position];
			}
			//IMPORTANT set back lazyof current node to 0
			lazyTree[position]=0;
		}
		// if entire node is within query range 
		if(nodeStart>=left && nodeEnd <= right) {
			return tree[position];
		}
		if(nodeEnd<left || nodeStart > right ) {
			//what to return if node doesnt has any overlap with range asked in query			
			return 0; //since this is a sum hence for invalid node it should be 0 
			//or we can only look for appropriate child nodes avoiding those which might not fall in the range
		}
		long mid= (nodeStart+nodeEnd)/2;
		
		int leftChildPosition = position*2  + 1;
		int rightChildPosition = position*2 + 2;
		
		long leftChildAns = queryTree(left,right,nodeStart,mid,leftChildPosition);//ans for left to mid
		long rightChildans= queryTree(left,right,mid+1,nodeEnd,rightChildPosition); //ans for mid to right
		
		return leftChildAns+rightChildans;
		
	}

}
