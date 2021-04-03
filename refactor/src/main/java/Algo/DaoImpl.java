package Algo;

import model.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DaoImpl {


    static HashMap<String,Object> mapList=new HashMap<>();
    static HashMap<String,String> str_map_class=new HashMap();

    public static HashMap<Integer,Integer> oriRelation = new HashMap<>();

    //invoke 是 方法 与 invoke 表的 join 结果
    static ArrayList<ArrayList<Integer> >invoke=new ArrayList<>();


    private static void init(){
        HashMap<Integer, JavaClass> classMap=new HashMap<>();
        HashMap<Integer, JavaMethod> methodMap=new HashMap<>();
        HashMap<Integer, JavaMember> memberMap=new HashMap<>();
        HashMap<Integer, InvokeRelation> invokeMap=new HashMap<>();
        JavaClass jc=new JavaClass();
        JavaMember jmb=new JavaMember();
        JavaMethod jmt=new JavaMethod();
        InvokeRelation ir=new InvokeRelation();
        mapList.put(jc.getClass().getName(),classMap);
        mapList.put(jmb.getClass().getName(),memberMap);
        mapList.put(jmt.getClass().getName(),methodMap);
        mapList.put(ir.getClass().getName(),invokeMap);
        str_map_class.put("class",jc.getClass().getName());
        str_map_class.put("method",jmt.getClass().getName());
        str_map_class.put("invoke",ir.getClass().getName());
        str_map_class.put("member",jmb.getClass().getName());
    }

    static {
        init();
    }



    public static void put(Base base){


        for(String k : str_map_class.keySet()){
            String className=base.getClass().getName();
            if(str_map_class.get(k).equals(className)){
                ((HashMap<Integer,Object>)(mapList.get(className))).put(base.getId(),base);
            }
        }
    }

    public static Object get(int id,String classType){

        var map=mapList.get(str_map_class.get(classType));

        var obj=((HashMap)(map)).get(id);
        return obj;
    }

    public static int getSize(String classType){
        var map=mapList.get(str_map_class.get(classType));
        return ((HashMap)(map)).size();
    }

    static HashMap getMap(String key){
        return ((HashMap)mapList.get(str_map_class.get(key)));
    }

    public static void loadInvoke(){
        invoke.clear();
        for(int i=0;i<=getMap("method").size();i++){
            //invoke.get(id)直接调用即可
            invoke.add(new ArrayList<>());
        }

        for(int i=0;i<getMap("invoke").size();i++){
            InvokeRelation ir = (InvokeRelation) getMap("invoke").get(i+1);
            int m_id=ir.getCaller_method_id();
            int i_id=ir.getId();
            invoke.get(m_id).add(i_id);
        }

    }

    public static List<InvokeRelation> getInvokes(int m_id){
        List<InvokeRelation> irs=new ArrayList<>();
        var list=invoke.get(m_id);
        for(int i:list){
            var ir=get(i,"invoke");
            irs.add((InvokeRelation) ir);
        }
        return irs;
    }

    public static double calStability(){
        //需要在refact算法调用后，进行计算稳定性
        int cnt = 0;
        for (Integer methodId:oriRelation.keySet()){
            JavaMethod jm = (JavaMethod) get(methodId,"method");
            if (jm.getClassId().equals(oriRelation.get(methodId))){
                cnt++;
            }
        }
        return cnt*1.0 / oriRelation.keySet().size();
    }

    public static void clear() {
        mapList=new HashMap<>();
        str_map_class=new HashMap();

        oriRelation = new HashMap<>();

        invoke=new ArrayList<>();
        init();
    }
}
