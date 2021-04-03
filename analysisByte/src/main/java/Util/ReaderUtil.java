package Util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ReaderUtil {

    public static BufferedReader getReader(Process p){
        InputStream fis=p.getInputStream();
        //用一个读输出流类去读
        InputStreamReader isr=new InputStreamReader(fis);
        //用缓冲器读行
        return new BufferedReader(isr);
    }
}
