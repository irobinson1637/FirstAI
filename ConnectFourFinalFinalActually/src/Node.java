import java.util.*;

class Node {
	int key;
	int wins;
	int loss;
	int plays;
	Node parent;
	ArrayList<Node> children = new ArrayList<Node>();

	Node() {

	}

	Node(int key, int wins, int plays, Node par) {
		this.key = key;
		this.wins = wins;
		this.plays = plays;
		this.parent = par;
	}

	Node findChild(int number, Node focusNode) { // returns child
		for(int y =0; y<children.size(); y++){
			Node store = children.get(y);
			if(store.key == number){
				return store;
			}
		}
		return null;
		
	}

	void setParent(Node par) {
		this.parent = par;
	}

	public ArrayList<Node> getChildren() {
		return children;
	}

	void addChildren(Node a) {
		this.children.add(a);
		a.setParent(this);
	
	}
	boolean childAlready(Node a){
for(int f =0; f< this.children.size(); f++){
	Node temp = this.children.get(f);
	//if(temp!=null){
	if(temp.key == a.key){
		return true;
	//}
	}
}
return false;
	
	}
	
	double winpct() {
		return (double) wins/((double) plays);
	}
	
	double losspct() {
		return (double) loss / (double) plays;
	}

	public String toString() {
		return key + "has" + wins + "wins in" + plays + "plays";
	}
}