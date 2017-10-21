import java.util.Arrays;
import java.util.Scanner;

public class SortMethod {
    private int size;
    final private int MAX_ARRAY_SIZE = 200000000;
    final private int unsignedSize = 32;
    private long[] numArray;// = new long[MAX_ARRAY_SIZE];
    private long[] mergeArray;// = new long[MAX_ARRAY_SIZE];
    private int[] hibbardGap= {536870911, 268435455, 134217727, 67108863, 33554431, 16777215, 8388607,
            4194303, 2097151, 1048575, 524287, 262143, 131071, 65535, 32767, 16383, 8191, 4095, 2047,
            1023, 511, 255, 127, 63, 31, 15, 7, 3, 1};
    private int[] sedgwickGap ={ 603906049, 268386305, 150958081, 67084289, 37730305, 16764929, 9427969, 4188161,
            2354689, 1045505, 587521, 260609, 146305, 64769, 36289, 16001, 8929, 3905, 2161, 929,
            505, 209, 109, 41, 19, 5, 1 };

    private long[] bArray;//用于计数排序
    private int[] cArray;//用于计数排序
    public static void main(String[] args){

        SortMethod sortMethod=new SortMethod();
        //sortMethod.shellSortExperiment();
        sortMethod.radixSortExperiment();
        sortMethod.sortExperiment();

    }

    public void sortExperiment(){
        //int[] sizeArray = {10,100,1000,10000,100000,1000000,10000000,100000000,200000000};
        int inputSize;
        Scanner scanner=new Scanner(System.in);
        while(true){
            System.out.println("请输入排序数据规模n,n是不超过200000000的正整数,输入-1结束:");
            inputSize=scanner.nextInt();
            if(inputSize==-1)
                break;
            long [] copyArray;//保证在原始生成的数组上进行比较
            long startTime,endTime;
            System.out.println("Data Size Is "+inputSize);
            this.setSize(inputSize);
            this.getRandomNumber();
            copyArray = Arrays.copyOf(this.getNumArray(),inputSize);
            if(inputSize<1000000){
                startTime = System.nanoTime();
                this.insertSort(copyArray);
                endTime = System.nanoTime();
                System.out.println("InsertSort Costs:"+(endTime-startTime)/1000+"us.");
            }
            else
                System.out.println("Data Is Too Large.InsertSort Uses Too Much Time.");


            copyArray = Arrays.copyOf(this.getNumArray(),inputSize);
            startTime = System.nanoTime();
            this.shellSortSedgewick(copyArray);
            endTime = System.nanoTime();
            System.out.println("ShellSort Costs:"+(endTime-startTime)/1000+"us.");


            copyArray = Arrays.copyOf(this.getNumArray(),inputSize);
            startTime = System.nanoTime();
            this.quickSort(copyArray,0,inputSize-1);
            endTime = System.nanoTime();
            System.out.println("QuickSort Costs:"+(endTime-startTime)/1000+"us.");


            copyArray = Arrays.copyOf(this.getNumArray(),inputSize);
            startTime = System.nanoTime();
            mergeArray=new long[size];
            this.mergeSort(copyArray,0,inputSize-1);
            endTime = System.nanoTime();
            System.out.println("MergeSort Costs:"+(endTime-startTime)/1000+"us.");

            copyArray = Arrays.copyOf(getNumArray(),inputSize);
            startTime = System.nanoTime();
            this.radixSort(copyArray,8);
            endTime = System.nanoTime();
            System.out.println("RadixSort Costs:"+(endTime-startTime)/1000+"us.");


        }

    }

    public void radixSortExperiment() {
        int[] sizeArray ={10000, 100000, 1000000, 10000000,100000000};
        int []r={2,4,5,6,8,10,12,14,16};
        long[] copyArray;//保证在原始生成的数组上进行比较
        for (int i = 0; i < sizeArray.length; i++){
            System.out.println("Data Size:" + sizeArray[i]);
            this.setSize(sizeArray[i]);
            this.getRandomNumber();
            long startTime, endTime;
            for(int j=0;j<r.length;j++){
                copyArray = Arrays.copyOf(this.getNumArray(), sizeArray[i]);
                startTime = System.nanoTime();
                this.radixSort(copyArray,r[j]);
                endTime = System.nanoTime();
                System.out.println("radixSort using r="+r[j]+" Costs:" + (endTime - startTime) / 1000 + "us.");
            }
        }
    }

    public void shellSortExperiment(){
        int[] sizeArray ={10000,100000,1000000,10000000};

        for(int i=0;i<sizeArray.length;i++){
            System.out.println("Data Size:"+sizeArray[i]);
            this.setSize(sizeArray[i]);
            this.getRandomNumber();
            long startTime,endTime;
            long [] copyArray;//保证在原始生成的数组上进行比较
            copyArray = Arrays.copyOf(this.getNumArray(),sizeArray[i]);
            startTime = System.nanoTime();
            this.shellSort2(copyArray);
            endTime = System.nanoTime();
            System.out.println("ShellSort2 Costs:"+(endTime-startTime)/1000+"us.");

            copyArray = Arrays.copyOf(this.getNumArray(),sizeArray[i]);
            startTime = System.nanoTime();
            this.shellSortHibbard(copyArray);
            endTime = System.nanoTime();
            System.out.println("ShellSortHibbard Costs:"+(endTime-startTime)/1000+"us.");

            copyArray = Arrays.copyOf(this.getNumArray(),sizeArray[i]);
            startTime = System.nanoTime();
            this.shellSortSedgewick(copyArray);
            endTime = System.nanoTime();
            System.out.println("ShellSortSedgewick Costs:"+(endTime-startTime)/1000+"us.");
        }

    }

    private void getRandomNumber(){
        MersenneTwister mt=new MersenneTwister();
        numArray=new long[size];
        for(int i=0;i<size;i++)
            numArray[i]=mt.genrand_int32();
    }

    //插入排序
    private void insertSort(long[] copyArray){

        int i = 1,j;
        long temp;
        while(i<size){
            temp = copyArray[i];
            j = i-1;
            while(j>=0&&temp<copyArray[j]){
                copyArray[j+1]=copyArray[j];
                j-=1;
            }
            copyArray[j+1]=temp;
            i++;
        }
        /*
        System.out.print("InsertSort:");
        for(int k=0;k<size;k++)
            System.out.print(" "+copyArray[k]);
        System.out.println();
        */
    }

    // gap:N/2 , N/4 , ..., 1 (repeatedly divide by 2);
    private void shellSort2(long[] copyArray){

        for(int gap=size/2;gap>0;gap/=2){
            baseShellInsert(copyArray,gap);
        }
    }

    //Hibbard's increments: 1, 3, 7, ..., 2^k - 1 ;
    private void shellSortHibbard(long[] copyArray){

        int i;
        for(i=0;i<29;i++)
            if(hibbardGap[i]<size)
                break;
        for(;i<29;i++){
            int gap=hibbardGap[i];
            baseShellInsert(copyArray,gap);
        }
    }
    //Sedgewick's increments: 1, 5, 19, 41, 109, ....
    private void shellSortSedgewick(long[] copyArray){
        int i;
        for(i=0;i<27;i++)
            if(sedgwickGap[i]<size)
                break;
        for(;i<27;i++){
            int gap=sedgwickGap[i];
            baseShellInsert(copyArray,gap);
        }
    }

    private void baseShellInsert(long [] copyArray,int gap){
        for(int i=gap;i<size;i++){
            int k=i-gap;
            if(copyArray[i]<copyArray[k]){
                long temp=copyArray[i];
                while(k>=0&&copyArray[k]>temp){
                    copyArray[k+gap]=copyArray[k];
                    k-=gap;
                }
                copyArray[k+gap]=temp;
            }
        }
    }

    private int partition(long[] copyArray, int low, int high){
        long pivot = copyArray[low];
        while(low<high){

            while(low<high&&copyArray[high]>pivot)
                high--;
            copyArray[low]=copyArray[high];
            while(low<high&&copyArray[low]<=pivot)
                low++;
            copyArray[high]=copyArray[low];
        }
        copyArray[low]=pivot;
        return low;
    }

    private void quickSort(long[] copyArray, int left, int right){
        if(left<right){
            int pivot = partition(copyArray,left,right);
            quickSort(copyArray,left,pivot-1);
            quickSort(copyArray,pivot+1,right);
        }
    }

    private void merge(long[] copyArray, int s, int mid, int e){
        int s1=s,e1=mid,s2=mid+1,e2=e;
        int p=0;
        while(s1<=e1&&s2<=e2){
            if(copyArray[s1]<=copyArray[s2])
                mergeArray[p++]=copyArray[s1++];
            else
                mergeArray[p++]=copyArray[s2++];
        }
        while(s1<=e1) mergeArray[p++]=copyArray[s1++];
        while(s2<=e2) mergeArray[p++]=copyArray[s2++];

        for(int i=0;i<p;i++)
            copyArray[s+i]=mergeArray[i];
    }

    private void mergeSort(long[] copyArray, int s, int e){
        if(s<e){
            int mid = (s+e)/2;
            mergeSort(copyArray,s,mid);
            mergeSort(copyArray,mid+1,e);
            merge(copyArray,s,mid,e);
        }
    }


    private void countingSort(long[] A, long[] B, int[] C, long maxRange, int offset){
        int i,j;
        int arrayLen=A.length;
        for(i=0;i<=maxRange;i++)
            C[i]=0;
        for(j=0;j<arrayLen;j++)
            C[(int)((A[j]>>offset)&maxRange)]++;
        for(i=1;i<=maxRange;i++)
            C[i]+=C[i-1];
        for(j=arrayLen-1;j>=0;j--){
            int temp=(int)((A[j]>>offset)&maxRange);
            B[C[temp]-1]=A[j];//因为数组下标从0开始 所以第i大的数 应该存放在i-1的位置
            C[temp]--;
        }
    }

    private void radixSort(long[] copyArray, int r){
        int round = unsignedSize/r;
        long maxRange=(1<<r)-1;//-的优先级高于<<
        int offset=0;
        int arrayLen=copyArray.length;
        cArray=new int[(int)maxRange+1];//[0,maxRange]
        bArray=new long[arrayLen];
        if(unsignedSize%r!=0)
            round++;
        int i;
        for(i=1;i<=round;i++){
            //避免不断进行数组元素复制的开销
            if(i%2==1)
                countingSort(copyArray,bArray,cArray,maxRange,offset);
            else
                countingSort(bArray,copyArray,cArray,maxRange,offset);
            offset+=r;
        }
        //如果结果保存在barray中要复制到copyarray中 且跳出循环时i++了
        if(i%2==0)
            for(int j=0;j<arrayLen;j++)
                copyArray[j]=bArray[j];

    }

    private void setSize(int size) {
        this.size = size;
    }


    private long[] getNumArray() {
        return numArray;
    }

}
