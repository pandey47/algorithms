import java.util.Scanner;

/**
 * This would be a basic segment tree with Range Query and single update
 * Also aims to solve RMQSQ problem at spoj
 * @author Vivek
 *
 */
public class SegmentTreeBasic {
	// keep variables global to avoid passing among multiple functions
	static int MAX_ARR_SIZE=100001;
	static int MAX_TREE_SIZE=280000;
	static int arr[]= new int [MAX_ARR_SIZE];
	static int tree[]= new int [MAX_TREE_SIZE];
	

	public static void main(String[] args) {
		Scanner sc= new Scanner(System.in);
		
		int n=sc.nextInt();
		for(int i=0;i<n;i++) {
			arr[i]=sc.nextInt();
		}
		buildTree(0,n-1,0); //build from top recursively
		
		int q=sc.nextInt();
		for(int i=0;i<q;i++) {
			int left=sc.nextInt();
			int right=sc.nextInt();
			System.out.println(queryTree(left,right,0,n-1,0));
		}
		

	}
	/**
	 * 
	 * @param left and @param right is the asked query range solution
	 * @param start and @param end will be the node's range values
	 * @param position is the index where the nodes range ans is stored in tree array
	 * @return
	 */
	private static int queryTree(int left, int right, int start, int end, int position) {
		// if entire node is within query range 
		if(start>=left && end <= right) {
			return tree[position];
		}
		if(end<left || start > right ) {
			//what to return if node doesnt has any overlap with range asked in query			
			return Integer.MAX_VALUE; 
			//or we can only look for appropriate child nodes avoiding those which might not fall in the range
		}
		int mid= (start+end)/2;
		
		int leftChildPosition = position*2  + 1;
		int rightChildPosition = position*2 + 2;
		
		int leftChildAns = queryTree(left,right,start,mid,leftChildPosition);//ans for left to mid
		int rightChildans= queryTree(left,right,mid+1,end,rightChildPosition); //ans for mid to right
		
		return Math.min(leftChildAns, rightChildans);
		
	}

	/**
	 * 
	 * @param position place in tree array to update the value 
	 * @return min, addition or whatever is for left to right
	 */
	private static int buildTree(int left, int right, int position) {
		//range is single ==> leaf node
		if(left==right) {
			tree[position] = arr[left];
			return arr[left];
		}
		int leftChildPosition = 2 * position +1;
		int rightChildPosition = 2 * position+2;
		int mid = (left+right)/2;
		
		//build left and right tree 
		int leftNodeAns = buildTree(left, mid, leftChildPosition);
		int rightNodeAns = buildTree(mid+1,right,rightChildPosition);
		
		//apply logic to append two nodes to parent
		int currentNodeValue= Math.min(leftNodeAns, rightNodeAns);
		
		tree[position]= currentNodeValue;
		return currentNodeValue;
		
	}

}
