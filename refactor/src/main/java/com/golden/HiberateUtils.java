package com.golden;

import Algo.DaoImpl;
import model.Base;
import model.JavaClass;
import model.JavaMethod;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Random;

public class HiberateUtils {
    static Session session = null;

    static boolean Disturbance = true;
    static double DisturbanceRate = 0.1;



    static Session configSession() {
        Configuration configuration = new Configuration();

        //不给参数就默认加载hibernate.cfg.xml文件，
        configuration.configure("hibernate.cfg.xml");

        //创建Session工厂对象
        SessionFactory factory = configuration.buildSessionFactory();

        //得到Session对象
        Session session = factory.openSession();
        return session;
    }

    static Session getSession() {
        if (HiberateUtils.session != null) {
            return session;
        }

        session = configSession();
        return session;
    }

    static void putInMap(String ClassName) {
        Session session = getSession();
        Query query = session.createQuery("FROM " + ClassName + " ");
        List class_list = query.list();
        Random random = new Random();
        for (Object jc : class_list) {
            //不如在这里进行扰动
            if (Disturbance && ClassName.equals("JavaMethod")) {
                JavaMethod jm = (JavaMethod) jc;
                if (!jm.isCoverParent() && !jm.isVirtual()) {
                    if (random.nextDouble() < DisturbanceRate) {
                        JavaClass jm_class = (JavaClass) DaoImpl.get(jm.getClassId(), "class");
                        System.out.print(jm.getName() + "从类" + jm_class.getFullname());

                        DaoImpl.oriRelation.put(jm.getId(),jm_class.getId());

                        jm.setClassId(random.nextInt(DaoImpl.getSize("class") - 1) + 1);
                        jm_class = (JavaClass) DaoImpl.get(jm.getClassId(), "class");
                        System.out.println("扰动到了" + jm_class.getFullname());

                    }

                }
            }

            DaoImpl.put((Base) jc);
        }
    }

    static void LoadData(Boolean disturbance,Double rate) {
        DaoImpl.clear();
        Disturbance = disturbance;
        DisturbanceRate = rate;
        putInMap("JavaClass");
        putInMap("JavaMethod");
        putInMap("JavaMember");
        putInMap("InvokeRelation");

        close();
        DaoImpl.loadInvoke();
    }

    static void close() {
        if (session != null) {
            session.close();
            session = null;
        }
    }


}
