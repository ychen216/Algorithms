import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;


public class Calculator{
    private ArrayList<Point> middlePointList;
    private Point closestPoint1;
    private Point closestPoint2;
    private double minDis;
    final double MAX_DIS = 1<<30;

    public static void main(String [] args){
        Calculator calculator=new Calculator();
        int []num={100,1000,10000,100000,500000,1000000};
        calculator.timeTest(num);
    }

    public Calculator(){
        init();
    }

    public double findNaive(ArrayList<Point> pointList){
        int size = pointList.size();
        for(int i = 0;i<size-1;i++){
            for(int j=i+1;j<size;j++){
                double temp = getDistance(pointList.get(i),pointList.get(j));
                if(temp<minDis)
                    update(temp,pointList.get(i),pointList.get(j));

            }
        }
        return minDis;
    }

    //pointList是已经按照X左边从小到大 排序好的点集
    public double findFast(ArrayList<Point> pointList,int start,int end){

        if(start==end)
            return MAX_DIS;//少于两个点
        //只有两个点
        if(end-1==start){
            double temp = getDistance(pointList.get(start),pointList.get(end));
            if(temp<minDis){
                update(temp,pointList.get(start),pointList.get(end));
                //System.out.println("........cp1:"+closestPoint1+"  cp2:"+closestPoint2+" dis"+temp);
            }

            return minDis;
        }

        int mid = (start+end)/2;
        //分治
        double min1=findFast(pointList,start,mid);
        double min2=findFast(pointList,mid+1,end);
        //minDis=Math.min(min1,min2);
        //minDis是全局的 左边两边分别处理完以后，minDis记录的就是二分后左边和右边最近点对的距离

        middlePointList = new ArrayList<Point>();
        //将中位数点左边 X轴上水平距离小于minDis的点加入
        double midX = pointList.get(mid).getX();
        Point p;

        //pointList X从小到大
        for(int i=mid;i>=start;i--){
            p = pointList.get(i);
            if((midX-p.getX())<=minDis)
                middlePointList.add(p);
            else //已经大于minDis i更小时两者水平距离一定大于minDis
                break;
        }
        for(int i =mid+1;i<=end;i++){
            p = pointList.get(i);
            if((p.getX()-midX)<=minDis)
                middlePointList.add(p);
            else
                break;
        }

        //按照Y坐标从小到大排序
        Collections.sort(middlePointList,new PointSortByY());
        int midListSize = middlePointList.size();
        for(int i=0;i<midListSize;i++){
            for(int j=1;j<=7&&(i+j)<midListSize;j++){
                double temp = getDistance(middlePointList.get(i),middlePointList.get(i+j));
                if(temp<minDis) {
                    update(temp,middlePointList.get(i),middlePointList.get(i+j));
                   // System.out.println("!!!!!!!!cp1:" + closestPoint1 + "  cp2:" + closestPoint2+" dis"+temp);
                }
            }
        }

        return minDis;
    }

    public void timeTest(int []num){
        Random random=new Random();
        ArrayList<Point> list = new ArrayList<Point>();
        ArrayList<Point> list1=new ArrayList<Point>();
        long start1,start2,end1,end2;
        for(int i=0;i<num.length;i++){
            list.clear();
            list1.clear();
            for(int j=0;j<num[i];j++){
                int x = random.nextInt(1000000);
                int y = random.nextInt(1000000);
                list.add(new Point(x,y));
                list1.add(new Point(x,y));
            }
            Collections.sort(list,new PointSortByX());
            start1 = System.nanoTime();
            findFast(list,0,num[i]-1);
            end1 = System.nanoTime();
            System.out.println("用分治法计算"+num[i]+"个点，耗时："+(end1-start1)/1000+"us.");
            start2 = System.nanoTime();
            findNaive(list1);
            end2 = System.nanoTime();
            System.out.println("用暴力法计算"+num[i]+"个点，耗时："+(end2-start2)/1000+"us.");
        }
    }

    public void init(){
        minDis = MAX_DIS;//随机设置一个初始大值
        closestPoint1 = null;
        closestPoint2 = null;
    }

    public double getDistance(Point p1,Point p2){
        return Math.sqrt((p1.getX()-p2.getX())*(p1.getX()-p2.getX())+(p1.getY()-p2.getY())*(p1.getY()-p2.getY()));
    }
    public void update(double dis,Point p1,Point p2){
        minDis=dis;
        closestPoint1=new Point(p1);
        closestPoint2=new Point(p2);
    }

    public Point getClosestPoint1() {
        return closestPoint1;
    }

    public Point getClosestPoint2() {
        return closestPoint2;
    }
}
