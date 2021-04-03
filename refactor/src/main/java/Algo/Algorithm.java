package Algo;


import model.JavaClass;
import model.JavaMember;
import model.JavaMethod;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Algorithm {
    static final double candidate_coeff=0.9;

    BinaryGraph binGra;
    int m,n;
    //m代表classCnt,n代表methodCnt
    public Algorithm(int m, int n){
        this.m=m;
        this.n=n;
        binGra=new BinaryGraph(m,n);
    }
    public void fortravel(){
        for(int i=1;i<=m;i++){
            for (int j=1;j<=n;j++){
                JavaMethod jm=(JavaMethod)DaoImpl.get(j,"method");
                JavaClass jc=(JavaClass)DaoImpl.get(i,"class");
                if(binGra.getOwns(i,j).value>0){
                    System.out.println(jc.getFullname()+"  owns:"+jm.getName());

                }
            }
        }
        for(int i=1;i<=n;i++){
            for (int j=1;j<=m;j++){
                JavaMethod jm=(JavaMethod)DaoImpl.get(i,"method");
                JavaClass jc=(JavaClass)DaoImpl.get(j,"class");
                if(binGra.getUse(i,j).value>0){
                    System.out.println(jm.getId()+" "+jm.getName()+" use "+jc.getFullname()+" times "+ binGra.getUse(i,j).value);
                }
            }
        }
    }

    public void draw(String filename) throws IOException {
        binGra.jsonGraph(filename);
    }

    public void refact(){

        var methodList=getCandidates();
        var refactList=getRefactMethod(methodList);
        doRefact(refactList);
        afterCare();
    }

    public double calHL(){
        return binGra.calHL();
    }

    public void afterCare(){
        DaoImpl.loadInvoke();
        binGra=new BinaryGraph(m,n);
    }

    private boolean hasRelationWithParent(JavaMethod jm){

        var jc=(JavaClass)DaoImpl.get(jm.getClassId(),"class");
        boolean hasRelationWithClass=false;

        ///判断是否 与 自己的父类或更上层的类有关联？
        do{
            if(binGra.getUse(jm.getId(),jc.getId()).value>0){
                hasRelationWithClass=true;
                break;
            }
            if(jc.getParent_id()==null){
                break;
            }
            jc=(JavaClass)DaoImpl.get(jc.getParent_id(),"class");
        }while (true);
        if(hasRelationWithClass){
            return true;
        }
        return false;
    }

    List<UseMehthod> getCandidates(){
        int max_use_cnt,max_class_id,sum_use;
        int m_id,class_id;
        List<UseMehthod>res=new ArrayList<>();

        for(int i=0;i<n;i++)
        {
            sum_use=0;max_use_cnt=0;max_class_id=-1;
            m_id=i+1;

            var jm=(JavaMethod)DaoImpl.get(m_id,"method");

            if(hasRelationWithParent(jm)){
                continue;
            }

            for(int j=0;j<m;j++){
                class_id=j+1;
                int use=binGra.getUse(m_id,class_id).value;
                if(use>max_use_cnt){
                    max_use_cnt=use;
                    max_class_id=class_id;
                }
                sum_use+=use;
            }

            if( sum_use>0 && (max_use_cnt * 1.0 / sum_use  > candidate_coeff) ){
                res.add(new UseMehthod(jm,max_class_id));
                var jc=(JavaClass) DaoImpl.get(max_class_id,"class");
                System.out.println("候选函数："+((JavaClass)(DaoImpl.get(jm.getClassId(),"class")))
                        .getFullname()+"."+jm.getName()+", 和 类 "+jc.getFullname()+" 关系紧密");
            }
        }
        return res;
    }

    List<UseMehthod> getRefactMethod(List<UseMehthod> methodList){
        System.out.println("判 断 哪 些 候选 方 法 将 获 得 重构...");
        List<UseMehthod> refactList=new ArrayList<>();
        for(var um:methodList){
            Condition con=calculateCondition(um);
            if(canMove( con  )){
                String ClassName=((JavaClass)DaoImpl.get(um.classId,"class")).getFullname();
                System.out.println("方法"+um.jm.getId()+" "+um.jm.getName()+"可以被搬移至 类" +ClassName);
                refactList.add(um);
            }
        }

        System.out.println("一共有"+refactList.size()+"个方法可以进行重构");
        return refactList;
    }

    boolean isParentClass(Integer son_id, Integer class_id){
        var jc=((JavaClass) DaoImpl.get(son_id,"class"));
        if(jc.getParent_id()==null){
            return false;
        }
        return jc.getParent_id().equals(class_id);
    }

    Condition calculateCondition(UseMehthod um){
        var irs=DaoImpl.getInvokes(um.jm.getId());
        Condition con = new Condition();

        if(um.jm.isCoverParent()){
            con.setHasOOproperty(true);
            //判断是否有子类
            for(int i=0;i<m;i++){
                int c_id=i+1;
                if( isParentClass( c_id , um.getClassId()) ){
                    con.setHasOOproperty(true);
                }
            }
        }
        if(um.jm.isVirtual()){
            con.setHasOOproperty(true);
        }

        for (var ir:irs){
            if(ir.isLocal()){
                con.setLocalInFunc(true);
            }
            if(ir.isMember()){
                con.setClassMember(true);
            }
            if(ir.isParam()){
                con.setParamOfFunc(true);
            }
            if(ir.isReturn()){
                con.setLikelyReturn(true);
            }
            if(ir.getType()==1){
                //方法
                var callee_id=ir.getCallee_id_method();
                var method=(JavaMethod)DaoImpl.get(callee_id,"method");
                if(!method.isStatic()){
                    con.setOnlyRelateWithStatic(false);
                }
            }else{
                var callee_id=ir.getCallee_id_member();
                var member=(JavaMember)DaoImpl.get(callee_id,"member");
                if(!member.isStatic()){
                    con.setOnlyRelateWithStatic(false);
                }
            }
        }

        return con;
    }

    boolean canMove(Condition con){
//        System.out.println(con);
        if(con.isHasOOproperty()){
            return false;
        }
        if(con.isOnlyRelateWithStatic()){
            return true;
        }
        if(con.isParamOfFunc()){
            return true;
        }
        if(con.isLocalInFunc() && con.isLikelyReturn()){
            return  false;
        }
        if(con.isLocalInFunc() && !con.isLikelyReturn()){
            return true;
        }
        if(con.isClassMember() && con.isLikelyReturn()){
            return false;
        }
        if(con.isClassMember() && !con.isLikelyReturn()){
            return false;
        }
//        System.out.println("出现了额外的情况");
        return true;
    }

    void doRefact(List<UseMehthod> refactList){
        System.out.println("重构中......");
        if(refactList.size()>0){
            for(var um:refactList){
                moveTo(um.jm,um.classId);
            }
        }

    }

    void moveTo(JavaMethod method,int classId){
        var methodMap=DaoImpl.getMap("method");
        method.setClassId(classId);
        methodMap.replace(method.getId(),method);
        if(((JavaMethod)DaoImpl.get(method.getId(),"method")).getClassId() != classId){
            System.out.println("方法"+method.getId()+" "+method.getName()+"重构失败");
        }
    }

}

class UseMehthod{
    JavaMethod jm;
    int classId;

    public JavaMethod getJm() {
        return jm;
    }

    public void setJm(JavaMethod jm) {
        this.jm = jm;
    }

    public UseMehthod(JavaMethod jm, int classId) {
        this.jm = jm;
        this.classId = classId;
    }

    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }
}
