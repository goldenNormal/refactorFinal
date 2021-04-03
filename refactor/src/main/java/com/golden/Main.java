package com.golden;

import Algo.Algorithm;
import Algo.DaoImpl;
import org.hibernate.Session;


import java.io.IOException;
import java.lang.reflect.Field;


public class Main {
    @SuppressWarnings("all")


    public static void main(String[] args) throws IOException {
//        SpringApplication.run(Main.class,args);
        start("demo_java",true,0.1);
    }

    public static void start(String projectName,boolean on,double rate) throws IOException {
        System.out.println(projectName+" "+on+" "+rate);


        //加载数据,第一个参数是是否扰动，第二个参数是扰动的概率
        HiberateUtils.LoadData(on, rate);
        //获得类和方法的数量
        int classCnt,methodCnt,memberCnt,invokeCnt;
        classCnt= DaoImpl.getSize("class");
        methodCnt= DaoImpl.getSize("method");
        memberCnt = DaoImpl.getSize("member");
        invokeCnt = DaoImpl.getSize("invoke");



//       调用算法
        Long now = System.currentTimeMillis();

        Algorithm algo=new Algorithm(classCnt,methodCnt);
        Double oriScore = algo.calHL();
        System.out.println("重构前的指标是："+oriScore);
//        algo.draw("static/"+projectName+"Ori.json");
        algo.refact();
        Double refactScore = algo.calHL();
        System.out.println("重构后的指标是："+refactScore);

        Double stability ;
        if(on){
            stability = DaoImpl.calStability();
        }else{
            stability = null;
        }

        Long end = System.currentTimeMillis();
        String execTime = (end-now)*1.0/1000+"s";
        System.out.println("算法执行时间："+execTime);


        Statistics stat = new Statistics();
        stat.setProjectName(projectName);
        if(on)
            stat.setDisturbanceRate(rate);
        else
            stat.setDisturbanceRate(0.0);
        stat.setClassCnt(classCnt);
        stat.setExecTime(execTime);
        stat.setMethodCnt(methodCnt);
        stat.setMemberCnt(memberCnt);
        stat.setInvokeCnt(invokeCnt);
        stat.setOriScore(oriScore);
        stat.setRefactScore(refactScore);
        stat.setStability(stability);


//        appendData(stat);
//        algo.draw("static/"+projectName+"refact.json");
    }

    private static void appendData(Statistics stat) {
        Session session = StatisticsSession.getSession();

//        String hql = "from Statistics where projectName = ?1";
//        List list =session.createQuery(hql)
//                .setParameter(1,stat.projectName).list();
//        if (list.size()==0){
//            session.save(stat);
//        }else{
//            Statistics find = (Statistics) list.get(0);
//
//            System.out.println("更新"+stat.projectName);
//            objectCopy(stat,find);
//
//
//            Transaction ts=session.beginTransaction();
//            session.update(find);
//            ts.commit();
//
//        }
        session.save(stat);

        session.close();
    }

    private static void objectCopy(Object stat, Object find)   {
        try{
            Field[] fields = find.getClass().getDeclaredFields();
            for (var field:fields){
                if(!field.getName().equals("id")){
                    field.set(find,field.get(stat));
                }
            }
        }catch (IllegalAccessException e){
            e.printStackTrace();
        }


    }


}


