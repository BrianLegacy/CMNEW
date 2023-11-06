//
//import com.mspace1.model.Tsmsout;
//import static com.mspace1.util.HibernateUtil.getSessionFactory;
//import java.util.List;
//import org.hibernate.Query;
//
//import org.hibernate.Session;
//
//public class test3 {
//
//    public static void main(String[] args) {   
//       
//        Session session5 = getSessionFactory().openSession();        
//        try {
//           session5.beginTransaction();
//            String hql = "FROM Tsmsout m, Tuser t WHERE t.username=m.user";
//
////      String sql2=   "(SELECT count(*) as net, s.user,left(s.time_submitted,8) as tarehe,u.firstname,u.surname,d.department FROM dbSMS.tSMSOUT s, dbSMS.tUSER u, dbTASK.tDEPARTMENT d where u.username = s.user and d.id = u.department_no "
////                    + " "+sql+" group by tarehe,user)"   ;
//            
//             Query query = session5.createQuery(hql);
//             List <Tsmsout>listOrders = query.list();             
//                       session5.getTransaction().commit();
//             try{
//                 System.out.println(listOrders.get(1));
//                 listOrders.forEach((p) -> 
//                         System.out.printf("%s %s; ", p.getId(), p.getUser()));
//                 
//             }catch(Exception k){
//        k.printStackTrace();
//             }
//         
//          
//
//        } catch (Exception k) {
//            k.printStackTrace();
//            session5.getTransaction().rollback();
//        } finally {
//            session5.close();
//        }        
//       
//    }
//
//}
