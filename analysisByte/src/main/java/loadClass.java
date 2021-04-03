import model.JavaClass;
import model.JavaMember;
import model.JavaMethod;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.net.URLClassLoader;

public class loadClass {

    public static String splitBy(String s){
        StringBuilder sb=new StringBuilder();
        while(true){
            int i=s.indexOf('\\');
            if (i==-1)break;
            sb.append(s.substring(0,i));
            if(!s.substring(0, i).equals("")){

                sb.append(".");
            }
            s=s.substring(i+1);
        }
        sb.append(s);
        if(!s.equals("")){
            sb.append(".");
        }

        return sb.toString();
    }

    private static String calFullName(classPath cp){
        StringBuilder res=new StringBuilder(splitBy(cp.getPath()));
        res.append(cp.getClass_name());
        return res.toString();
    }



    public static void load(String root,classPath cp){

        try{

            URLClassLoader urlCL=new URLClassLoader(new URL[]{new URL("file:\\"+root+"\\"),
            new URL("file:\\D:\\Interpreter\\java\\Maven\\my_maven_local_repository\\"),
                    new URL("file:\\C:\\Users\\Administrator\\.m2\\repository\\")}
                    );
            Class<?> Aclass = urlCL.loadClass(calFullName(cp));
            //获取类相关，获取类的方法，获取类的成员
            var jc=new JavaClass();
            jc.Aclass=Aclass;
            jc.setId(DatabBase.calClassId());
            jc.setFullname(Aclass.getName());
            jc.setListOfInterface("[]");
            DatabBase.addClass(jc);

        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void loadMethod(JavaClass jc,Method m){
        if(m.getName().contains("$")){
            return;
        }
        var jm=new JavaMethod();

        jm.setId(DatabBase.calMethodId());
        jm.setClassId(jc.getId());
        jm.setName(m.getName());

        jm.setStatic(Modifier.isStatic(m.getModifiers()));

        jm.setVirtual(Modifier.isAbstract(m.getModifiers()));

        if(Modifier.isPrivate(m.getModifiers())){
            jm.setAccessLevel("private");
        }else if(Modifier.isProtected(m.getModifiers())){
            jm.setAccessLevel("protected");
        }else if(Modifier.isPublic(m.getModifiers())){
            jm.setAccessLevel("public");
        }else{
            jm.setAccessLevel("default");
        }

        var parent_class=jc.Aclass.getSuperclass();
        jm.setCoverParent(false);
        if(parent_class != null){
            Method[] pms = parent_class.getDeclaredMethods();


            for(Method pm:pms){

                if(pm.getName().equals(jm.getName())){
                    jm.setCoverParent(true);
                    break;
                }
            }
        }



        DatabBase.addMethod(jc,jm);

    }

    public static void loadMember(JavaClass jc,Field f){
        if(f.getName().contains("$")){
            return;
        }
        JavaMember jm=new JavaMember();
        jm.setId(DatabBase.calMemberId());
        jm.setClass_id(jc.getId());
        jm.setName(f.getName());
        jm.setStatic(Modifier.isStatic(f.getModifiers()));
        if(Modifier.isPrivate(f.getModifiers())){
            jm.setAccessLevel("private");
        }else if(Modifier.isProtected(f.getModifiers())){
            jm.setAccessLevel("protected");
        }else if(Modifier.isPublic(f.getModifiers())){
            jm.setAccessLevel("public");
        }else{
            jm.setAccessLevel("default");
        }

        jm.setType_id(DatabBase.getIdClass(f.getType().getName()));
        DatabBase.addMember(jc,jm);

    }

    public static void loadClass(String root, classPath cp) {
        try{

            URLClassLoader urlCL=new URLClassLoader(new URL[]{new URL("file:\\"+root+"\\")});
            Class<?> Aclass = urlCL.loadClass(calFullName(cp));
            //获取类相关，获取类的方法，获取类的成员
            JavaClass jc;
            int c_id=DatabBase.getIdClass(Aclass.getName());
            jc = DatabBase.getClass(c_id);
            if(Aclass.getSuperclass()!=null &&DatabBase.getIdClass(Aclass.getSuperclass().getName())!=-1){
                int p_id=DatabBase.getIdClass(Aclass.getSuperclass().getName());
                jc.setParent_id(p_id);
                DatabBase.jcs.set(c_id-1,jc);
            }

            Method[] methods = Aclass.getDeclaredMethods();

            for(var m:methods){
                loadMethod(jc,m);
            }

            Field[] fields = Aclass.getDeclaredFields();
            for(var f:fields){
                loadMember(jc,f);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
