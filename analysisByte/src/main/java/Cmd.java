import java.io.*;

public class Cmd {
    public Process run(String path,String cmd_str){
        Process p=null;
        try {
            p=Runtime.getRuntime().exec(cmd_str,null,new File(path));

        } catch (IOException e) {
            e.printStackTrace();
        }
        return p;
    }
}
