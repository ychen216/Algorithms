import com.sun.deploy.pings.Pings;

import java.util.Comparator;

public class Point {
    private int x;
    private int y;
    public Point(){}
    public Point(int x,int y){
        this.x=x;
        this.y=y;
    }
    public Point(Point p){
        this.x=p.x;
        this.y=p.y;
    }
    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    @Override
    public String toString(){
        return "["+this.x+","+this.y+"]";
    }
    /*
    @Override
    public int compareTo(Point p) {
        if(this.getX()<p.getX())
            return 1;
        else
            return 0;
    }
    */
}
class PointSortByX implements Comparator<Point>{

    @Override
    public int compare(Point o1, Point o2) {
        if(o1.getX()<o2.getX())
            return -1;
        else if(o1.getX()>o2.getX())
            return 1;
        else
            return 0;
    }
}

class PointSortByY implements Comparator<Point>{
    @Override
    public int compare(Point o1, Point o2) {
        if(o1.getY()<o2.getY())
            return -1;
        else if(o1.getY()>o2.getY())
            return 1;
        else
            return 0;
    }
}