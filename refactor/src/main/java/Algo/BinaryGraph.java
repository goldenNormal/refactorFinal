package Algo;

import com.fasterxml.jackson.databind.ObjectMapper;
import model.JavaClass;
import model.JavaMember;
import model.JavaMethod;
import visualize.JsonUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;


public class BinaryGraph {
    private Score[][] ownsGraph;
    private Score[][] useGraph;
    int m,n;//m 是 类的数量 ， n是 方法的数量


    double HL=0;
    // 第一个索引的是类，第二个索引的是方法
    public BinaryGraph(int class_cnt, int method_cnt){

        m=class_cnt;
        n=method_cnt;
        ownsGraph=new Score[m+5][];
        for(int i=0;i<m+5;i++){
           ownsGraph[i]=new Score[n+5];
           for(int j=0;j<n+5;j++){
               ownsGraph[i][j]=new Score(0);
           }
        }

        useGraph=new Score[n+5][];
        for(int i=0;i<n+5;i++){
            useGraph[i]=new Score[m+5];
            for(int j=0;j<m+5;j++){
                useGraph[i][j]=new Score(0);
            }
        }

        fillOwns();
        fillUse();
    }

    void fillOwns(){
        for (int i=0;i<n;i++){
            int id=i+1;
            JavaMethod method =(JavaMethod)DaoImpl.get(id,"method");
            JavaClass jc=(JavaClass)DaoImpl.get(method.getClassId(),"class");
            ownsGraph[method.getClassId()][method.getId()].setValue(1);
        }
    }

    void fillUse(){
        //use[i][j] 是 方法i使用到了一个类j的成员变量或是成员函数

        for(int i=0;i<n;i++){
            int m_id=i+1;
            JavaMethod method =(JavaMethod)DaoImpl.get(m_id,"method");
            var irs=DaoImpl.getInvokes(method.getId());
            for(int j=0;j<irs.size();j++){
                var ir=irs.get(j);
                int callee_id,class_id;
                int type=ir.getType();
                if(type==1){
                    callee_id=ir.getCallee_id_method();
                    class_id=((JavaMethod)DaoImpl.get(callee_id,"method")).getClassId();
                }else{
                    callee_id=ir.getCallee_id_member();
                    class_id=((JavaMember)DaoImpl.get(callee_id,"member")).getClass_id();
                }
                useGraph[m_id][class_id].incValue();

            }
        }
    }

    Score getOwns(int class_id, int method_id){
        return ownsGraph[class_id][method_id];
    }

    Score getUse(int method_id, int class_id){
        return useGraph[method_id][class_id];
    }

    void jsonGraph(String filename) throws IOException {
        ObjectMapper mapper= JsonUtils.getMapper();
        var root=mapper.createObjectNode();

        var classes=root.putArray("classes");
        var methods=root.putArray("methods");
        var use=root.putArray("use");

        for(int i=0;i<m;i++){
            int c_id=i+1;
            var jc=(JavaClass)DaoImpl.get(c_id,"class");
            var classObj=mapper.createObjectNode();
            classObj.put("id",jc.getId());
            classObj.put("name",jc.getFullname());
            classes.add(classObj);
        }



        for(int i=0;i<n;i++){
            int m_id=i+1;
            var jm=(JavaMethod)DaoImpl.get(m_id,"method");
            var methodObj=mapper.createObjectNode();
            methodObj.put("id",jm.getId());
            methodObj.put("name",jm.getName());
            methodObj.put("class_id",jm.getClassId());
            methods.add(methodObj);
        }

        for(int i=0;i<n;i++){
            int m_id=i+1;
            for(int j=0;j<m;j++){
                int c_id=j+1;
                if(getUse(m_id,c_id).value>0){
                    var useObj=mapper.createObjectNode();
                    useObj.put("m_id",m_id);
                    useObj.put("c_id",c_id);
                    useObj.put("use_cnt",getUse(m_id,c_id).value);
                    use.add(useObj);
                }
            }
        }


        mapper.writeValue(new File(filename),root);

    }

    private HashMap<Integer,HashSet<Integer>> calGetMethods(){
        HashMap<Integer,HashSet<Integer>> getMethods=new HashMap<>();
        for(int i=0;i<m;i++){
            int c_id=i+1;
            HashSet<Integer> al=new HashSet<>();
            for(int j=0;j<n;j++){
                int m_id=j+1;
                if(getOwns(c_id,m_id).value>0){
                    al.add(m_id);
                }
            }
            getMethods.put(c_id,al);
        }
        return getMethods;
    }

    private HashMap<Integer,HashSet<Integer> > calR_methods(){
        HashMap<Integer,HashSet<Integer> > R_methods=new HashMap<>();
        for(int i=0;i<n;i++){
            int m_id=i+1;
            HashSet<Integer> al=new HashSet<>();
            var irs=DaoImpl.getInvokes(m_id);
            for(var j=0;j<irs.size();j++){
                var ir=irs.get(j);
                if(ir.getType()==1){
                    al.add(ir.getCallee_id_method());
                }
            }

            R_methods.put(m_id,al);
        }
        return R_methods;
    }

    private HashSet<Integer> CapSet(HashSet<Integer> s1,HashSet<Integer> s2){
        var s=new HashSet<Integer>();
        for(var i:s1){
            if(s2.contains(i)){
                s.add(i);
            }
        }
        return  s;
    }

    private Integer[][] calWeight(HashMap<Integer,HashSet<Integer>> getMethods,
                                 HashMap<Integer,HashSet<Integer>> R_method){
        int m_id,c_id;
        Integer[][] weight= new Integer[m+5][n+5];
        for(int i=0;i<m;i++){
            c_id=i+1;
            var set1=getMethods.get(c_id);

            for(int j=0;j<n;j++){
                m_id=j+1;
                var set2=R_method.get(m_id);
//                System.out.println("set1:"+set1);
//                System.out.println("set2:"+set2);
                var s=CapSet(set1,set2);
                weight[c_id][m_id]=s.size();
            }
        }


        return weight;
    }

    private Integer calM(Integer[][] weight){
        int _m=0;
        for(int i=1;i<=m;i++){
            for(int j=1;j<=n;j++){
                _m +=weight[i][j];
            }
        }
        return _m;
    }

    private int cal_i(Integer[][] weight,int i){
        Integer sum=0;
        for(int j=0;j<n;j++){
            int m_id=j+1;
            sum +=weight[i][m_id];
        }
        return sum;
    }
    private int cal_j(Integer[][] weight,int j){
        Integer sum=0;
        for(int i=0;i<m;i++){
            int c_id=i+1;
            sum +=weight[c_id][j];
        }
        return sum;
    }

    private double[][] cal_q(int _m,Integer[][] weight){
        var _q=new double[m+5][n+5];
        for(int c_id=1;c_id<=m;c_id++){
            for(int m_id=1;m_id<=n;m_id++){
                int w_i=cal_i(weight,c_id);
                int w_j=cal_j(weight,m_id);
                _q[c_id][m_id]=(1.0/_m)*w_i*w_j;
            }
        }
        return _q;
    }

    private double calQ(int _m, double [][]_q, Integer[][] weight ){
        var Q=0.0;
        for(int i=1;i<=m;i++){
            for(int j=1;j<=n;j++){
                var jm=(JavaMethod)DaoImpl.get(j,"method");
                var jc=(JavaClass)DaoImpl.get(i,"class");

                boolean c=jm.getClassId().equals(jc.getId());
                if(c){

                    Q+=weight[i][j] -  _q[i][j];
                }
            }
        }
        Q*=(1.0/_m);
        return Q;
    }

    double calHL(){
        var getMethods=calGetMethods();
        var R_methods=calR_methods();
        var Weight=calWeight(getMethods,R_methods);
        var _m=calM(Weight);
        var _q=cal_q(_m,Weight);
        HL=calQ(_m,_q,Weight);

        return HL;
    }

    double getHL(){
        return HL;
    }
}

//包装类
class Score {
    int value;

    public Score(int value) {
        this.value = value;
    }

    public int isValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void incValue(){
        this.value++;
    }
}
