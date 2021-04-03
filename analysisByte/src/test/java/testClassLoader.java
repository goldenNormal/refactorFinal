import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public class testClassLoader {
    public static void main(String[] args) throws MalformedURLException, ClassNotFoundException, NoSuchMethodException {
//        final String path1="C:\\Users\\Administrator\\Desktop\\code\\java\\demo_java\\out\\production\\demo_java";
//        final String path2="C:\\Users\\Administrator\\Desktop\\code\\java\\refactor\\target\\classes";
//        final String root=path1;
//        var cp_list=classPath.getAllClassPath(root);
//        URLClassLoader loader=new URLClassLoader(new URL[]{new URL("file:\\"+root+"\\")});
//        Class<?> c = loader.loadClass("C");
//        Method[] ms = c.getDeclaredMethods();
//        for(var m:ms){
//            System.out.println(m.getName());
//        }
//        System.out.println("-------");
//        Method m=c.getDeclaredMethod("AbstractMethod");
////        System.out.println();
//        System.out.println(Modifier.isAbstract(m.getModifiers()));

        var s="abc$2";
        System.out.println(s.contains("$"));


    }
}
