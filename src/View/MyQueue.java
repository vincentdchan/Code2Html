package View;
import java.io.File;

/**
 * Created by ZMYang on 2016/11/18.
 */
public class MyQueue {
    private File[] elem;
    private int size;
    private int curIndex = 0;
    private final int EXPANDSIZE = 8;

    int getSize(){
        return size;
    }

    public MyQueue(int size){
        elem = new File[size];
        this.size = size;
    }
    public MyQueue(){
        elem = new File[8];
        size = 8;
    }
    public void enQueue(File element){
        if(curIndex >= size){
            File[] temp = new File[size + EXPANDSIZE];
            System.arraycopy(elem , 0 , temp , 0 , elem.length);
            elem = temp ;
            size = size + EXPANDSIZE;
        }
        elem[curIndex++] = element;
    }
    public File deQueue(){
        if(isEmpty()){
            return null;
        }
        File front =  elem[0];
        for(int i = 1 ; i < curIndex ; i++){
            elem[i-1] = elem[i];
        }
        curIndex--;
//        if(curIndex < size / 2 && elem.length != 0){
//            int[] temp = new int[size / 2];
//            for(int i = 0 ; i < curIndex ; i++){
//                temp[i] = elem[i];
//            }
//            System.arraycopy(elem, 0 , temp , 0 , elem.length);
//            elem = temp;
//            size = size / 2;
//        }
        return front;
    }

    public File Peek(){
        return elem[0];
    }

    public int getNumberOfElement(){
        return curIndex ;
    }
    public boolean isEmpty(){
        if(curIndex <= 0){
            return true;
        }else{
            return false;
        }
    }
}

