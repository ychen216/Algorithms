import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class MainFrame extends JFrame {

    private JButton mouseButton;
    private JButton randomButton;
    public static void main(String[] args) {
       new MainFrame();
        /*ArrayList<Point> list = new ArrayList<Point>();
        list.add(new Point(3,6));
        list.add(new Point(1,6));
        Collections.sort(list,new PointSortByX());
        System.out.println(list);*/
    }

    public MainFrame(){
        this.setSize(300,200);
        this.setTitle("最近点对");
        this.setLocationRelativeTo(null);
        this.setLayout(new GridLayout(2,1));
        JPanel topPanel = new JPanel();
        JPanel buttomPanel = new JPanel();
        this.add(topPanel);
        this.add(buttomPanel);
        mouseButton = new JButton("鼠标生成点");
        randomButton = new JButton("随机生成点");
        topPanel.add(mouseButton);
        buttomPanel.add(randomButton);
        mouseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MouseFrame mouseFrame = new MouseFrame();
                mouseFrame.setSize(800,700);
                mouseFrame.setLocationRelativeTo(null);
                mouseFrame.setVisible(true);
            }
        });

        randomButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RandomFrame randomFrame = new RandomFrame();
                randomFrame.setSize(1500,1000);
                randomFrame.setLocationRelativeTo(null);
                randomFrame.setVisible(true);
            }
        });
        this.setVisible(true);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    }

}
class MouseFrame extends JFrame{
    private JPanel panel;
    private JButton btnCal;
    private JButton btnReset;
    private JLabel label;
    private ArrayList<Point> pointList;
    private Point cp1;
    private Point cp2;
    private boolean showLine =false;
    public MouseFrame(){
        this.setTitle("最小点对-鼠标生成点");
        this.setLayout(new BorderLayout());
        panel = new JPanel();
        panel.setBorder(new TitledBorder("请在此区域点击生成点对"));
        this.add(panel,BorderLayout.CENTER);
        JPanel panel2 = new JPanel();
        panel2.setLayout(new FlowLayout());
        this.add(panel2,BorderLayout.SOUTH);
        btnCal = new JButton("计算最近点对");
        btnReset = new JButton("清空所有点");
        btnReset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pointList.clear();
                cp1=cp2=null;
                repaint();
            }
        });
        btnCal.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int size = pointList.size();
                if(size<2){
                    label.setText("点的个数少于两个无法计算点对最小距离");
                    return;
                }

                //为了找到中轴 先按X从小到大进行排序 则中轴为下标中点
                Collections.sort(pointList,new PointSortByX());
                Calculator calculator=new Calculator();
                double mixDis = calculator.findFast(pointList,0,size-1);
                cp1 = calculator.getClosestPoint1();
                cp2 = calculator.getClosestPoint2();
                label.setText("最近点对距离为:"+mixDis);
                showLine = true;
                repaint();
            }
        });
        panel2.add(btnReset);
        panel2.add(btnCal);
        label = new JLabel("最近点对距离:");
        label.setSize(300,20);
        label.setVisible(true);
        panel2.add(label);
        this.add(panel2,BorderLayout.SOUTH);
        pointList = new ArrayList<Point>();
        //画点
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                int x = e.getX()+10;
                int y = e.getY()+30;
                Point p = new Point(x,y);
                pointList.add(p);
                repaint();
            }
        });
        this.setVisible(true);
    }


    @Override
    public void paint(Graphics graphics){
        super.paint(graphics);
        for(Point p:pointList){
            graphics.setColor(Color.RED);
            graphics.drawOval(p.getX(),p.getY(),4,4);
        }
        if(showLine){
            graphics.setColor(Color.BLACK);
            graphics.drawLine(cp1.getX(),cp1.getY(),cp2.getX(),cp2.getY());
            showLine=false;
        }
    }
}

class RandomFrame extends JFrame{
    private JPanel panel;
    private JButton btnCal;
    private JButton btnRandom100;
    private JButton btnRandom1K;
    private JButton btnRandom1W;
    private JButton btnRandom10W;
    private JButton btnRandom100W;
    private JLabel label;
    private ArrayList<Point> pointList=new ArrayList<Point>();
    private Point cp1;
    private Point cp2;
    private boolean showLine =false;
    public RandomFrame() {
        this.setTitle("最小点对-随机生成点");
        this.setLayout(new BorderLayout());
        panel = new JPanel();
        panel.setBorder(new TitledBorder("随机生成点对"));
        this.add(panel, BorderLayout.CENTER);
        JPanel panel2 = new JPanel();
        panel2.setLayout(new FlowLayout());
        this.add(panel2, BorderLayout.SOUTH);
        btnCal = new JButton("计算最近点对");
        btnRandom100 = new JButton("随机生成点一百个点");
        btnRandom100.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                label.setText("最近点对距离:");
                pointList.clear();
                Random random=new Random();
                for(int i=0;i<100;i++){
                    int x =random.nextInt(panel.getWidth());
                    int y =random.nextInt(panel.getHeight());
                    pointList.add(new Point(x,y));
                }
                cp1 = cp2 = null;
                repaint();
            }
        });
        btnRandom1K = new JButton("随机生成点一千个点");
        btnRandom1K.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                label.setText("最近点对距离:");
                pointList.clear();
                Random random=new Random();
                for(int i=0;i<1000;i++){
                    int x =random.nextInt(panel.getWidth());
                    int y =random.nextInt(panel.getHeight());
                    pointList.add(new Point(x,y));
                }
                cp1 = cp2 = null;
                repaint();
            }
        });
        btnRandom1W = new JButton("随机生成点一万个点");
        btnRandom1W.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                label.setText("最近点对距离:");
                pointList.clear();
                Random random=new Random();
                for(int i=0;i<10000;i++){
                    int x =random.nextInt(panel.getWidth());
                    int y =random.nextInt(panel.getHeight());
                    pointList.add(new Point(x,y));
                }
                cp1 = cp2 = null;
                repaint();
            }
        });
        btnRandom10W = new JButton("随机生成点十万个点");
        btnRandom10W.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                label.setText("最近点对距离:");
                pointList.clear();
                Random random=new Random();
                for(int i=0;i<100000;i++){
                    int x =random.nextInt(panel.getWidth());
                    int y =random.nextInt(panel.getHeight());
                    pointList.add(new Point(x,y));
                }
                cp1 = cp2 = null;
                repaint();
            }
        });
        btnRandom100W = new JButton("随机生成点一百万个点");
        btnRandom100W.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                label.setText("最近点对距离:");
                pointList.clear();
                Random random=new Random();
                for(int i=0;i<1000000;i++){
                    int x =random.nextInt(panel.getWidth());
                    int y =random.nextInt(panel.getHeight());
                    pointList.add(new Point(x,y));
                }
                cp1 = cp2 = null;
                repaint();
            }
        });
        btnCal.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int size = pointList.size();
                if (size < 2) {
                    label.setText("点的个数少于两个无法计算点对最小距离");
                    return;
                }

                //为了找到中轴 先按X从小到大进行排序 则中轴为下标中点
                Collections.sort(pointList,new PointSortByX());
                Calculator calculator=new Calculator();
                double mixDis = calculator.findFast(pointList,0,size-1);
                cp1 = calculator.getClosestPoint1();
                cp2 = calculator.getClosestPoint2();
                label.setText("最近点对距离为:"+mixDis);
                showLine = true;
                repaint();
            }
        });
        panel2.add(btnRandom100);
        panel2.add(btnRandom1K);
        panel2.add(btnRandom1W);
        panel2.add(btnRandom10W);
        panel2.add(btnRandom100W);
        panel2.add(btnCal);
        label = new JLabel("最近点对距离:");
        label.setSize(300, 20);
        label.setVisible(true);
        panel2.add(label);
        this.add(panel2, BorderLayout.SOUTH);
        this.setVisible(true);
    }
    @Override
    public void paint(Graphics graphics){
        super.paint(graphics);
        for(Point p:pointList){
            graphics.setColor(Color.RED);
            graphics.drawOval(p.getX(),p.getY(),4,4);
        }
        if(showLine){
            graphics.setColor(Color.BLACK);
            graphics.drawLine(cp1.getX(),cp1.getY(),cp2.getX(),cp2.getY());
            showLine=false;
        }
    }
}