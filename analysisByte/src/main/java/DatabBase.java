import model.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatabBase {
    public static List<JavaClass> jcs=new ArrayList<>();
    public static List<JavaMethod> methods=new ArrayList<>();
    public static List<JavaMember> members=new ArrayList<>();
    public static List<InvokeRelation> irs=new ArrayList<>();

    public static Map<String,Integer> jc_map=new HashMap<>();
    public static Map<String,Integer> method_map=new HashMap<>();
    public static Map<String,Integer> member_map = new HashMap<>();

    public static int calClassId(){
        return jcs.size()+1;
    }

    public static int calMethodId(){
        return methods.size()+1;
    }

    public static int calMemberId(){
        return members.size()+1;
    }

    public static int calIVId(){
        return irs.size()+1;
    }

    public  static void addClass(JavaClass jc){
        jcs.add(jc);
        jc_map.put(jc.getFullname(),jc.getId());
    }
    public static void addMethod(JavaClass jc,JavaMethod method){
        methods.add(method);
        method_map.put(jc.getFullname()+"\\."+method.getName(),method.getId());
    }

    public static void addIV(InvokeRelation ir){
        irs.add(ir);
    }
    public static void addMember(JavaClass jc,JavaMember jm){
        members.add(jm);
        member_map.put(jc.getFullname()+"\\."+jm.getName(),jm.getId());
    }

    public static int getIdClass(String name){
        if(!jc_map.containsKey(name)){
            return -1;
        }
        return jc_map.get(name);
    }
    public static int getIdMehtod(String name){
        if(!method_map.containsKey(name)){
            return -1;
        }
        return method_map.get(name);
    }
    public static int getIdMember(String name){
        if(!member_map.containsKey(name)){
            return -1;
        }
        return member_map.get(name);
    }

    public static JavaClass getClass(int id){
        return jcs.get(id-1);
    }
    public static JavaMethod getMethod(int id){
        return methods.get(id-1);
    }
    public static JavaMember getMember(int id){
        return members.get(id-1);
    }
    public static InvokeRelation getIR(int id){
        return irs.get(id-1);
    }


}
