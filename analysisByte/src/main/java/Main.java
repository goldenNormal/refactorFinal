import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.*;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static List<String> paths = new ArrayList<>();


    public static void request(String name,String on,String rate){
        Connection conn = Jsoup.connect("http://localhost:8080/api/refact");
        conn.data("projectName",name);
        conn.data("on",on);
        conn.data("rate",rate);

        try {
            conn.get();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException, NoSuchMethodException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, InstantiationException {

        String projectPath = "E:\\code\\java\\Trama\\output";
        process(projectPath);

//        String projectName = "Comiler";
//        postRequests(projectName);

    }

    public static  void sleep(int second){
        try {
            Thread.sleep(second* 1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static  void postRequests(String pjn){
        request(pjn,"false","0.0");
        System.out.println("发送成功");
        String [] rates = {"0.1","0.2","0.3","0.4"};
        for (String rate:rates){
            sleep(2);
            request(pjn,"true",rate);
            System.out.println("发送成功");

        }


    }

    public static void process(String path){

        deleteAll();
        var cp_list=classPath.getAllClassPath(path);

        for(var cp:cp_list){
            loadClass.load(path,cp);
        }

        for(var cp:cp_list){
            loadClass.loadClass(path,cp);
        }

        for(var cp:cp_list){
            //解析方法之间的调用关系
            Analysis.analysis(path,cp);
        }
//        printAll();
        saveAll();
    }

    private static void deleteAll() {
        var session = HiberateUtils.getSession();
        String[] tableList={"invoke","member","method","class"};
        for(var table:tableList){
            Transaction tx = session.beginTransaction();
            String sql = " truncate table  " + table;
            Query query = session.createSQLQuery(sql);
            query.executeUpdate();
            tx.commit();
        }
//        session.close();

    }

    static void printAll(){
        for(var jc:DatabBase.jcs){
            System.out.println(jc);
        }

        for(var jm:DatabBase.methods){
            System.out.println(jm);
        }

        for(var jm:DatabBase.members){
            System.out.println(jm);
        }
        for(var ir:DatabBase.irs){
            if(ir.getType()==1){
                System.out.println(DatabBase.getMethod(ir.getCaller_method_id()).getName());
                System.out.println(DatabBase.getMethod(ir.getCallee_id_method()).getName());
            }
            System.out.println(ir);
            System.out.println();
        }
    }

    private static void saveAll() {
        var session = HiberateUtils.getSession();

        for(var obj:DatabBase.jcs){

            session = HiberateUtils.getSession();
            session.save(obj);

        }


        for(var obj:DatabBase.methods){
            session = HiberateUtils.getSession();
            session.save(obj);
        }

        for(var obj:DatabBase.members){
            session = HiberateUtils.getSession();
            session.save(obj);
        }

        for(var obj:DatabBase.irs){
            session = HiberateUtils.getSession();
            session.save(obj);
        }

    }


}
