import Util.ReaderUtil;
import model.*;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Analysis {
    static Cmd cmd=new Cmd();
    public static void analysis(String root,classPath cp){
        var cmd_template="javap -c -l -private ";
        var p=cmd.run(root+cp.getPath(),cmd_template+cp.getClass_name());
        var br=ReaderUtil.getReader(p);

        String line=null;
        try {
            while(true)
            {

                if ((line=br.readLine())==null) break;
                //处理line
                if(line.contains("(") && line.contains(");")){

                    visitMethod(line,br,getPackageClassName(cp));
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static String findName(String func){
        return func.substring(0,func.indexOf("("));
    }

    public static String changeToPackageClassName(String FullName){

        return FullName.replaceAll("\\.","/");
    }

    public static String getPackageClassName(classPath cp){
        if(cp.getPath().equals("")){
            return cp.getClass_name();
        }
        var packageName=cp.getPath().substring(1);
        packageName=packageName.replaceAll("\\\\","/");
        return packageName+"."+cp.getClass_name();
    }


    private static String[] findParam(String func){
        int begin = func.indexOf("(");
        int end=func.indexOf(");");
        String parmS=func.substring(begin+1,end);
        parmS=parmS.replaceAll(" ","");
        if(!parmS.equals("")){
            return parmS.split(",");
        }
        return null;
    }

    private static JavaMethod extractMethod(String line,String PackageClassName){
        var sub_str=line.substring(line.indexOf("Method "));
        var invoke_m=sub_str.substring(7,sub_str.indexOf(":"));
        JavaMethod jm = null;
        if(!invoke_m.contains("<init>") && !invoke_m.contains("java/")){
            String[] split = invoke_m.split("\\.");

            if(split.length == 1){
                jm = convertToJavaMethod(invoke_m,PackageClassName);
            }else{
                jm=convertToJavaMethod(split[1],split[0]);
            }
        }
        return jm;
    }

    private static  JavaMember extractMember(String line,String PackageClassName){
        var sub_str=line.substring(line.indexOf("Field "));
        var invoke_f=sub_str.substring(6,sub_str.indexOf(":"));
        String[] split = invoke_f.split("\\.");
        JavaMember jm=null;
        if(split.length == 1){
            jm = convertToMember(invoke_f,PackageClassName);

        }else{
            jm=convertToMember(split[1],split[0]);
        }
        return jm;
    }

    private static void visitMethod(String line,BufferedReader br,String PackageClassName) throws IOException {

        String funcName=findFunc(line);
        var Caller = convertToJavaMethod(funcName,PackageClassName);
        if(Caller == null){
            return;
        }
        boolean inCode=false;

        if(funcName.equals(PackageClassName) || funcName.contains("$") ){
            return;
        }
        String[] paramlist = findParam(line);
        String returnThing = findReturn(line);

        HashSet<JavaMethod> i_method=new HashSet<>();
        HashSet<JavaMember> i_member = new HashSet<>();

        while(true)
        {
            if ((line=br.readLine()).equals("") || line.equals("}")) break;

            if(line.equals("    Code:")){
                inCode=true;
            }
            if(line.startsWith("    Line")){
                inCode=false;
            }
            if(inCode){
                if(line.contains("invokespecial") || line.contains("invokevirtual")){
                    var sub_str=line.substring(line.indexOf("Method "));
                    var invoke_m=sub_str.substring(7,sub_str.indexOf(":"));

                    if(!invoke_m.contains("<init>") && !invoke_m.contains("java/")){
                        var jm = extractMethod(line,PackageClassName);
                        if(jm != null){
                            i_method.add(jm);
                        }
                    }
                }else if(line.contains("getfield")){
                    //有可能是自己的成员变量，也可能是别的类的成员变量
                    var jm = extractMember(line,PackageClassName);
                    if(jm != null){
                        i_member.add(jm);
                    }
                }
            }
        }


        for(var m:i_method){
            var ir=new InvokeRelation();
            assert Caller != null;
            buildIRofMethod(ir,Caller,m,returnThing,paramlist,i_member);
            DatabBase.addIV(ir);
        }
        for(var m:i_member){
            var ir=new InvokeRelation();


            buildIRofMember(ir,Caller,m,returnThing,paramlist,i_member);
            DatabBase.addIV(ir);
        }
    }



    private static JavaMethod convertToJavaMethod(String invoke_m, String packageClassName) {
        Integer c_id = DatabBase.getIdClass(packageClassName.replaceAll("/","\\."));

        while(c_id != null && c_id>0){
            for(int i =0;i<DatabBase.methods.size();i++){
                var jm = DatabBase.methods.get(i);
                if(jm.getClassId().equals(c_id) && jm.getName().equals(invoke_m)){
                    return jm;
                }
            }
            var jc=DatabBase.getClass(c_id);
            c_id = jc.getParent_id();
        }

        return null;

    }

    private static JavaMember convertToMember(String invoke_f, String packageClassName) {
        Integer c_id = DatabBase.getIdClass(packageClassName.replaceAll("/","\\."));

        while(c_id != null && c_id>0){
            for(int i =0;i<DatabBase.members.size();i++){
                var jm = DatabBase.members.get(i);
                if(jm.getClass_id().equals(c_id) && jm.getName().equals(invoke_f)){
                    return jm;
                }
            }
            var jc=DatabBase.getClass(c_id);
            c_id = jc.getParent_id();
        }

        return null;
    }

    private static InvokeRelation buildIR(InvokeRelation ir,JavaMethod caller, JavaClass callee_Jc,
                                          String returnThing, String[] paramlist, HashSet<JavaMember> i_member){
        ir.setId(DatabBase.calIVId());

        ir.setCaller_method_id(caller.getId());
        if(!callee_Jc.getId().equals(caller.getClassId())){
            for(var member:i_member){
                if(member.getType_id().equals(callee_Jc.getId())){
                    ir.setMember(true);
                }
            }
            if(paramlist!=null){
                for(var parm:paramlist){
                    if(parm.equals(callee_Jc.getFullname())){
                        ir.setParam(true);
                        break;
                    }
                }
            }

            if(!ir.isParam() && !ir.isMember()){
                if(notParent(callee_Jc.getId(),caller.getClassId())){
                    ir.setLocal(true);
                }

            }
            if(callee_Jc.getFullname().equals(returnThing)){
                ir.setReturn(true);
            }
        }
        return ir;
    }

    private static InvokeRelation buildIRofMember(InvokeRelation ir, JavaMethod caller, JavaMember callee, String returnThing, String[] paramlist, HashSet<JavaMember> i_member) {

        ir.setCallee_id_member(callee.getId());

        var callee_Jc=DatabBase.getClass(callee.getClass_id());


        buildIR(ir, caller, callee_Jc, returnThing, paramlist, i_member);

        ir.setType(2);
        return ir;
    }

    private static InvokeRelation buildIRofMethod(InvokeRelation ir,JavaMethod caller,JavaMethod callee,String returnThing,String[] parms,
                                          HashSet<JavaMember>i_member){

        ir.setCallee_id_method(callee.getId());

        var callee_Jc=DatabBase.getClass(callee.getClassId());
        buildIR(ir, caller, callee_Jc, returnThing, parms, i_member);

        ir.setType(1);
        return ir;
    }

    private static boolean notParent(Integer callee_calss_id, Integer classId) {
        Integer c_id = classId;
        while (c_id!=null && !c_id.equals(callee_calss_id)){
            c_id = DatabBase.getClass(c_id).getParent_id();
        }
        return c_id == null;
    }


    private static String findReturn(String line) {
        int end = line.indexOf("(");
        String sub=line.substring(0,end);
        String[] ss = sub.split(" ");
        return ss[ss.length-2];
    }

    private static String findFunc(String line) {
        int end = line.indexOf("(");
        String sub=line.substring(0,end);
        String[] ss = sub.split(" ");
        return ss[ss.length-1];
    }


}
