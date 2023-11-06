//
//import com.mspace1.survey.model.TsurveyData;
//import com.mspace1.util.HibernateUtil4;
//import java.math.BigInteger;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Map;
//import org.hibernate.HibernateException;
//import org.hibernate.Session;
//
///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
///**
// *
// * @author Samson
// */
//public class SurveyReport {
//
//    public static List<TsurveyData> surveyList;
//
//    public static void getSurveyList() {
//
//        try (Session hsession = HibernateUtil4.getSessionFactory().openSession()) {
//            hsession.beginTransaction();
////            surveyList = hsession.createQuery("from TsurveyData t where t.surveyId = 1 and t.txType = 2 "
////                    + "and t.time between '" + new java.sql.Timestamp(startDate.getTime()) + "' and "
////                    + "'" + new java.sql.Timestamp(endDate.getTime()) + "' order by t.id desc")
////                    .getResultList();
////            surveyList = hsession.createQuery("from TsurveyData t where t.surveyId = 1 and t.txType = 2 "
////                    + "and t.time between '" + sdate1 + "' and "
////                    + "'" + edate1 + "' order by t.id desc")
////                    .getResultList();
//            /*select t.sessionId, t.message,t.scale ,t.status from tSurveyData t where t.scale!=-1 and t.response!="" and t.status=1 order by  t.sessionId, t.id desc;*/
////            surveyList = hsession.createQuery("from TsurveyData t where t.time between '" + sdate1 + "' and "
////                    + "'" + edate1 + "' and t.txType!=2  and t.response !='' order by t.time desc, t.mobile desc")
////SELECT * FROM dbSMSCCamps.tSurveyData t where t.response !="" and t.txType !=2  order by t.sessionId 
//            surveyList = hsession.createQuery("from TsurveyData t where t.response !='' and t.txType !=2  order by t.sessionId")
//                    .getResultList();
//
//            //System.out.println(map.toString());
//            hsession.getTransaction().commit();
//
//        } catch (HibernateException e) {
//            e.printStackTrace();
//        }
//
//        Map<String, List<TsurveyData>> map = new HashMap<String, List<TsurveyData>>();
//        for (TsurveyData data : surveyList) {
//            String sessionId = data.getSessionId();
//            // System.out.println("Session ID " + sessionId);
//            if (!map.containsKey(sessionId)) {
//                //System.out.println("Map does not contain key "+sessionId);
//                List<TsurveyData> list = new ArrayList<>();
//                list.add(data);
//                map.put(sessionId, list);
//            } else {
//                // System.out.println("Is this executing ever");
//                map.get(sessionId).add(data);
//
//            }
//
//        }
//       
//        System.out.println(map);
//    }
//
//    public static void main(String[] args) {
//        getSurveyList();
//    }
//}
