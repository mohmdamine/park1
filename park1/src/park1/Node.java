package park1;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Node {
    public int x, y;
    public int disponibilite;

    public Node(int x, int y) {
        this.x = x;
        this.y = y;
        this.disponibilite = 0;
    }
    
    @Override
    public boolean equals(Object o) {
        if (o instanceof Node) {
            Node n = (Node) o;
            return this.x == n.x && this.y == n.y;
        }
        return false;
    }
     
    public int getX() {
    	return x;
    }
    
    public int getY() {
    	return y;
    }
    
    @Override
    public int hashCode() {
        return 31 * x + y;
    }
    
    
}
