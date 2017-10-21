import java.math.BigInteger;
import java.util.Scanner;

public class FibonacciMethod {

    public static void main(String [] args){
        FibonacciMethod fibonacciMethod = new FibonacciMethod();
        BigInteger res;
        Scanner scanner = new Scanner(System.in);
        long startTime,endTime;
        while(true){
            System.out.println("请输入所求的斐波那契数序号n(n>=0),输入-1退出程序");
            int n = scanner.nextInt();
            if(n==-1){

                System.out.println("退出程序!");
                break;
            }
            //递归
            if(n<50){
                startTime = System.nanoTime();
                res = fibonacciMethod.fibRecursive(n);
                endTime = System.nanoTime();
                System.out.println("利用递归法求得斐波那契数列第"+n+"项为："+res.toString());
                System.out.println("利用递归法求得斐波那契数列第"+n+"项所花费时间为："+(endTime-startTime)/1000.0+"us.");
            }
            else
                System.out.println("递归法时间复杂度过大，仅用于计算斐波那契数列中序号为50以下的数！");

            //迭代
            startTime = System.nanoTime();
            res = fibonacciMethod.fibButtonUp(n);
            endTime = System.nanoTime();
            System.out.println("利用迭代法求得斐波那契数列第"+n+"项为："+res.toString());
            System.out.println("利用迭代法求得斐波那契数列第"+n+"项所花费时间为："+(endTime-startTime)/1000.0+"us.");

            //矩阵
            startTime = System.nanoTime();
            res = fibonacciMethod.fibMatrix(n);
            endTime = System.nanoTime();
            System.out.println("利用矩阵法求得斐波那契数列第"+n+"项为："+res.toString());
            System.out.println("利用矩阵法求得斐波那契数列第"+n+"项所花费时间为："+(endTime-startTime)/1000.0+"us.");
        }

    }
    //递归
    public  BigInteger fibRecursive(int n){
        if (n==0)
            return new BigInteger("0");
        else if(n==1)
            return new BigInteger("1");
        else
            return fibRecursive(n-1).add(fibRecursive(n-2));
    }

    //迭代法
    public BigInteger fibButtonUp(int n){
        BigInteger[] res = new BigInteger[3];
        res[0] = new BigInteger("0");
        res[1] = new BigInteger("1");
        if(n<=1)
            return res[n];
        else{
            for(int i=2;i<=n;i++){
                int j = i%3;
                res[j] = res[(j+1)%3].add(res[(j+2)%3]);
            }
            return res[n%3];
        }
    }
    //矩阵方法
    public BigInteger fibMatrix(int n){
        BigInteger[][] origMatrix = new BigInteger[2][2];
        BigInteger[][] resMatrix = new BigInteger[2][2];
        origMatrix[0][0] = new BigInteger("1");
        origMatrix[0][1] = new BigInteger("1");
        origMatrix[1][0] = new BigInteger("1");
        origMatrix[1][1] = new BigInteger("0");
        resMatrix[0][0] = new BigInteger("1");
        resMatrix[0][1] = new BigInteger("0");
        resMatrix[1][0] = new BigInteger("0");
        resMatrix[1][1] = new BigInteger("1");
        if(n==0)
            return origMatrix[1][1];
        else if(n==1)
            return origMatrix[0][1];
        else if(n==2)
            return origMatrix[0][0];
        else{

            while(n>0){
                if(n%2==1)
                    resMatrix = this.matrixMul(resMatrix,origMatrix);
                n/=2;
                origMatrix = this.matrixMul(origMatrix,origMatrix);
            }
            return resMatrix[0][1];
        }
    }
    private BigInteger[][] matrixMul(BigInteger[][] ans,BigInteger[][] ori){
        BigInteger[][] resMatrix = new BigInteger[2][2];
        BigInteger temp;
        for(int i=0;i<2;i++){
            for(int j=0;j<2;j++){
                temp = new BigInteger("0");
                for(int k=0;k<2;k++){
                    temp=temp.add(ans[i][k].multiply(ori[k][j]));
                }
                resMatrix[i][j] = temp;
            }
        }
        return resMatrix;
    }
}
