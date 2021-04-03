import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class classPath {
    String class_name;
    String path;

    public static ArrayList<classPath> getAllClassPath(String root){
        var res=new ArrayList<classPath>();
        dfs(root,"",res);
        return res;
    }

    private static void dfs(String root,String relativePath, List<classPath> res){
        var file=new File(root+relativePath);
        File[] fs = file.listFiles();	//遍历path下的文件和目录，放在File数组中
        if(fs!=null)
        for(File f:fs) {                    //遍历File[]数组
            if (!f.isDirectory()) {
                if(f.getName().endsWith(".class")){
                    if(f.getName().contains("$")){
                        continue;
                    }
                    String s=f.getName();
                    String className=s.substring(0,s.indexOf(".class"));
                    var cp=new classPath();
                    cp.setClass_name(className);
                    cp.setPath(relativePath);
                    res.add(cp);
                }
            }
            else{
//                if(!f.getName().equals("GUI"))
                dfs(root,relativePath+"\\"+f.getName(),res);
            }
        }

    }

    public String getClass_name() {
        return class_name;
    }

    @Override
    public String toString() {
        return "classPath{" +
                "class_name='" + class_name + '\'' +
                ", path='" + path + '\'' +
                '}';
    }

    public void setClass_name(String class_name) {
        this.class_name = class_name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
