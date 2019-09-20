

//https://codility.com/cert/view/certJB6PQA-QCAN37JEA9JSVF7N/details/

import java.util.TreeSet;

class Solution {
    public int solution(int[] A) {
        if (A == null) return -1;
		
		int N = A.length;
		long sol = 0;
		
		if (N == 0) return (int)sol;
		
		//Element[] E = new Element[N];
		TreeSet<Element> rightTree = new TreeSet<Element>();
		TreeSet<Element> leftTree = new TreeSet<Element>();
		
		
		for (int i = 0; i < N; i++){ // time-complexity O(NlogN)
			//E[i] = new Element(A[i]);
			rightTree.add(new Element(A[i]));
		}
		
		
		for (int j = 0; j < N; j++){
			// initial set up
			if (j == 0){
				Element next = new Element(rightTree.last());
				next.setC(1);
				next.setA(1);
				rightTree.remove(next);
				while (next.getH() > A[0]){
					rightTree.add(next);
					Element succ = next;
					next = new Element(rightTree.lower(next));
					next.setC(1);
					next.setA(succ.getA() + 1);
					rightTree.remove(next);
				}
				next.setC(next.getA());
				leftTree.add(next);
				sol += next.getA();
			}
			// final step
			else if (j == (N - 1)){
				sol++;
				sol += leftTree.tailSet(new Element(A[N - 1])).size();
			}
			// somewhere in the middle
			else {
				// bad case
				if (A[j] < A[j - 1]){
					Element nE = new Element(A[j]);
					Element pE = new Element(A[j - 1]);
					rightTree.remove(nE);
					leftTree.add(nE);
					
					Element lt = new Element(leftTree.lower(pE));
					long ltAcc = leftTree.floor(pE).getA();
					Element rt = new Element(rightTree.lower(pE));
					long rtAcc = 0;
					Element rtSucc = rightTree.higher(pE);
					if (rtSucc != null) rtAcc = rtSucc.getA();
					
					while (rt != null && rt.getH() > A[j]){
						if (lt.getH() > rt.getH()){
							lt.setC(rtAcc + 1);
							lt.setA(rtAcc + 1 + ltAcc);
							leftTree.remove(lt);
							leftTree.add(new Element(lt));
							ltAcc = lt.getA();
							lt = leftTree.lower(lt);
						}
						else {
							rt.setC(ltAcc + 1);
							rt.setA(ltAcc + 1 + rtAcc);
							rightTree.remove(rt);
							rightTree.add(new Element(rt));
							rtAcc = rt.getA();
							rt = rightTree.lower(rt);
						}
					}
					while (lt.getH() > A[j]){
						lt.setC(rtAcc + 1);
						lt.setA(rtAcc + 1 + ltAcc);
						leftTree.remove(lt);
						leftTree.add(new Element(lt));
						ltAcc = lt.getA();
						lt = leftTree.lower(lt);
					}
					lt.setC(rtAcc + 1);
					lt.setA(rtAcc + 1 + ltAcc);
					leftTree.remove(lt);
					leftTree.add(new Element(lt));
					sol += lt.getA();	
				}
				// good case
				else {
					Element ne = new Element(A[j]);
					rightTree.remove(ne);
					Element ltSucc = leftTree.higher(ne);
					Element rtSucc = rightTree.higher(ne);
					if (ltSucc == null && rtSucc == null){
						ne.setC(1);
						ne.setA(1);
					}
					else if (ltSucc != null && rtSucc == null){
						ne.setC(1);
						ne.setA(ltSucc.getA() + 1);
					}
					else if (ltSucc == null && rtSucc != null){
						ne.setC(rtSucc.getA() + 1);
						ne.setA(ne.getC());
					}
					else{
						ne.setC(rtSucc.getA() + 1);
						ne.setA(ltSucc.getA() + ne.getC());
					}
					leftTree.add(ne);
					sol = sol + ne.getA();
				}
			}
		}
		return (int)(sol % 1000000007);
    }
}

class Element implements Comparable<Element> {

	private long height;
	private long acc;
	private long con;
	
	public Element(long h){
		height = h;
		acc = 0;
		con = 0;
	}
	
	public Element(Element e){
		if (e == null){
			height = -1;
			acc = -1;
			con = -1;
		}
		else {
			height = e.getH();
			acc = e.getA();
			con = e.getC();
		}
	}
	
	public long getH(){
		return height;
	}
	
	public long getA(){
		return acc;
	}
	
	public long getC(){
		return con;
	}
	
	public void setA(long a){
		acc = a;
	}
	
	public void setC(long c){
		con = c;
	}
	
	public int compareTo(Element e) {
		if (height < e.getH()) return -1;
		if (height > e.getH()) return 1;
		return 0;
	}
	
	public boolean equals(Element e){
		return height == e.getH();
	}

}