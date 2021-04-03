import model.Base;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import java.util.List;

public class HiberateUtils {
    static Session session=null;
    static Session configSession(){
        Configuration configuration = new Configuration();

        //不给参数就默认加载hibernate.cfg.xml文件，
        configuration.configure("hibernate.cfg.xml");

        //创建Session工厂对象
        SessionFactory factory = configuration.buildSessionFactory();

        //得到Session对象
        Session session = factory.openSession();
        return session;
    }

    static Session getSession(){
        if(HiberateUtils.session != null){
            return session;
        }

        session=configSession();
        return session;
    }

    static void close(){
        if(session!=null){
            session.close();
            session=null;
        }
    }





}
