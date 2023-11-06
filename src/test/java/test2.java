///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package com.mspace1.controller;
//
//import com.mspace1.model.Tsmsout;
//import com.mspace1.model.TsmsoutComplete;
//import com.mspace1.model.Tusersmsschedule;
//import com.mspace1.model.sentsm;
//import static com.mspace1.util.HibernateUtil.getSessionFactory;
//import com.mspace1.util.getsession;
//import java.io.Serializable;
//import java.text.DateFormat;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.Iterator;
//import java.util.List;
//import javax.annotation.PostConstruct;
//import javax.faces.application.FacesMessage;
//import javax.faces.bean.ManagedBean;
//import javax.faces.bean.SessionScoped;
//import javax.faces.context.FacesContext;
//import javax.servlet.http.HttpSession;
//import org.hibernate.Session;
//import org.primefaces.event.SelectEvent;
//
///**
// *
// * @author mspace
// */
//@ManagedBean(name = "sentsms")
//@SessionScoped
//public class sentsms implements Serializable {
// private sentsm sentsmz;
//      DateFormat inFormat2 = new SimpleDateFormat("yyyyMMddHHmmss");
//    DateFormat outFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//    DateFormat dateFormat0 = new SimpleDateFormat("yyyyMMdd");
//    DateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
//      private List<sentsm> SMSList = new ArrayList<>();
//    private Date sdate;
//    private Date edate;
//    private Date date2;
//    private Date date3;
//    private Long smssentcount;
//    private List<sentsm> filteredsms;
//    
//    //values sentsm
//    
//      private List<Tsmsout> u = new ArrayList<>();
//       private   List<Tusersmsschedule> s = new ArrayList<>();
//      private  List<TsmsoutComplete> m = new ArrayList<>();
//    
//
//    HttpSession session1 = getsession.getSession();
//    String user = (String) session1.getAttribute("username");
//
//    public sentsms() {
//    }
//
//    @PostConstruct
//    public void init() {
//           sentsmz = new sentsm();
//    }
//
//    public List<sentsm> getSMSList() {
//        return SMSList;
//    }
//
//   
//
// 
//    public Long getSmssentcount() {
//        return smssentcount;
//    }
//
//    public void setSmssentcount(Long smssentcount) {
//        this.smssentcount = smssentcount;
//    }
//
//    public List<sentsm> getFilteredsms() {
//        return filteredsms;
//    }
//
//    public void setFilteredsms(List<sentsm> filteredsms) {
//        this.filteredsms = filteredsms;
//    }
//
//    public sentsm getSentsmz() {
//        return sentsmz;
//    }
//
//    public void setSentsmz(sentsm sentsmz) {
//        this.sentsmz = sentsmz;
//    }
//
//    public Date getSdate() {
//        return sdate;
//    }
//
//    public void setSdate(Date sdate) {
//        this.sdate = sdate;
//    }
//
//    public Date getEdate() {
//        return edate;
//    }
//
//    public void setEdate(Date edate) {
//        this.edate = edate;
//    }
//
////    public void getsentsmsdetail() {     
////
////        smssentcount = 0l;
////        if (this.getSdate() == null || this.getEdate() == null) {
////            SMSList = null;
////        }
////        String sdate1 = (dateFormat0.format(sdate));
////        String edate1 = (dateFormat0.format(edate));
//////        
//////        String sdate_shedule=(outFormat.format(sdate));
//////          String edate_shedule=(outFormat.format(edate));
////
////        sdate1 = sdate1 + "000001";
////        edate1 = edate1 + "235959";
////
////        String tschedule = "from Tusersmsschedule s where s.username=:user order by s.id desc";
////        String tsmcomplete = "from TsmsoutComplete m where m.user=:user and m.timeSubmitted between '" + sdate1 + "' and '" + edate1 + "' order by m.id desc";
////        String tsmsout = "from Tsmsout k where k.user=:user and k.timeSubmitted between '" + sdate1 + "' and '" + edate1 + "' order by k.id desc";
////
////        //tsmsout fetching 
////        Session session5 = getSessionFactory().openSession();
////        try {
////
////            session5.beginTransaction();
////            u = session5.createQuery(tsmsout)
////                    .setParameter("user",user)
////                    .list();
////            session5.getTransaction().commit();
////
////        } catch (Exception k) {
////            k.printStackTrace();
////        } finally {
////            session5.close();
////        }
////
////        //tsmsschedule
////        Session session4 = getSessionFactory().openSession();
////        try {
////
////            session4.beginTransaction();
////            s = session4.createQuery(tschedule)
////                    .setParameter("user", user)
////                    .list();
////            session4.getTransaction().commit();
////
////        } catch (Exception k) {
////            k.printStackTrace();
////        } finally {
////            session4.close();
////        }
////        //tsmscomplete fetch
////        Session session3 = getSessionFactory().openSession();
////        try {
////            session3.beginTransaction();
////            m = session3.createQuery(tsmcomplete)
////                    .setParameter("user", user)
////                    .list();
////            session3.getTransaction().commit();
////
////        } catch (Exception k) {
////            k.printStackTrace();
////        } finally {
////            session3.close();
////        }
////
////        //tsmsout
////        if (u != null) {
////            for (Iterator iterator = u.iterator(); iterator.hasNext();) {
////                sentsm sentsmh = new sentsm();
////                Tsmsout employee = (Tsmsout) iterator.next();
////                 System.out.println(employee.getId()+"haiyaa keveol");
////    
////                sentsmh.setUser1(employee.getUser());
////                 System.out.println(sentsmh.getUser1()+"haiyaa 2wkeveol");
////                sentsmh.setDestination_addr(employee.getDestinationAddr());
////                sentsmh.setMessage_payload(employee.getMessagePayload());
////                sentsmh.setSource_addr(employee.getSourceAddr());
////                sentsmh.setSmscount(getSmsCount(employee.getMessagePayload()));
////                smssentcount = getSmsCount(employee.getMessagePayload()) + smssentcount;
////            sentsmh.setId(employee.getId());
////                String stato = employee.getStatus();
////                if (stato.equalsIgnoreCase("0")) {
////                    sentsmh.setStatus("To Be Sent");
////                } else if (stato.equalsIgnoreCase("1")) {
////
////                    sentsmh.setStatus("Submitted To Network");
////                } else if (stato.equalsIgnoreCase("2")) {
////
////                    sentsmh.setStatus("Network");
////                } else if (stato.equalsIgnoreCase("3")) {
////
////                    sentsmh.setStatus("Network");
////                } else if (stato.equalsIgnoreCase("4")) {
////
////                    sentsmh.setStatus("Delivered");
////                } else if (stato.equalsIgnoreCase("5")) {
////
////                    sentsmh.setStatus("Network");
////                } else if (stato.equalsIgnoreCase("6")) {
////
////                    sentsmh.setStatus("Undelivered");
////                } else if (stato.equalsIgnoreCase("7")) {
////
////                    sentsmh.setStatus("Expired");
////                } else if (stato.equalsIgnoreCase("8")) {
////
////                    sentsmh.setStatus("Submit Failed");
////                } else if (stato.equalsIgnoreCase("9")) {
////
////                    sentsmh.setStatus("Network");
////                } else if (stato.equalsIgnoreCase("9")) {
////
////                    sentsmh.setStatus("OptedOut");
////                } else if (stato.equalsIgnoreCase("11")) {
////
////                    sentsmh.setStatus("Scheduled");
////                }
////
////                try {
////
////                    date2 = inFormat2.parse(employee.getTimeSubmitted());
////                    date3 = inFormat2.parse(employee.getTimeProcessed());
////
////                    sentsmh.setTime_processed(outFormat.format(date3));
////                    sentsmh.setTime_submitted(outFormat.format(date2));
////                } catch (ParseException ex) {
////                    sentsmh.setTime_processed(employee.getTimeProcessed());
////                    sentsmh.setTime_submitted(employee.getTimeSubmitted());
////                }
////
////                try{
////                SMSList.add(sentsmh);
////                }catch(Exception k){
////                k.printStackTrace();
////                }
////                System.out.println(employee.getId() + "tsmsout");
////            }
////        }
////
////        //tsmschedule
////        if (s != null) {
////            for (Iterator iterator = s.iterator(); iterator.hasNext();) {
////                   sentsm sentsmh = new sentsm();
////                Tusersmsschedule employee = (Tusersmsschedule) iterator.next();
////
////                sentsmh.setId(employee.getId());
////                sentsmh.setUser1(employee.getUsername());
////                sentsmh.setStatus("scheduled");
////                sentsmh.setDestination_addr(employee.getDest());
////                sentsmh.setMessage_payload(employee.getMessage());
////                sentsmh.setSource_addr(employee.getSource());
////                sentsmh.setTime_processed(null);
////                sentsmh.setSmscount(getSmsCount(employee.getMessage()));
////                smssentcount = getSmsCount(employee.getMessage()) + smssentcount;
////
////                DateFormat df = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
////                sentsmh.setTime_submitted(df.format(employee.getSendTime()));
////                System.out.println(employee.getId() + "schedule");
////                SMSList.add(sentsmh);
////            }
////        }
////
//////         tsmscomplete
////        if (m != null) {
////            for (Iterator iterator = m.iterator(); iterator.hasNext();) {
////                sentsm sentsmh = new sentsm();
////                TsmsoutComplete employee = (TsmsoutComplete) iterator.next();
////                sentsmh.setId(employee.getId());
////                sentsmh.setUser1(employee.getUser());
////                sentsmh.setDestination_addr(employee.getDestinationAddr());
////                sentsmh.setMessage_payload(employee.getMessagePayload());
////                sentsmh.setSource_addr(employee.getSourceAddr());
////                sentsmh.setSmscount(getSmsCount(employee.getMessagePayload()));
////                smssentcount = getSmsCount(employee.getMessagePayload()) + smssentcount;
////                String stato = employee.getStatus();
////                if (stato.equalsIgnoreCase("0")) {
////                    sentsmh.setStatus("To Be Sent");
////                } else if (stato.equalsIgnoreCase("1")) {
////
////                    sentsmh.setStatus("Submitted To Network");
////                } else if (stato.equalsIgnoreCase("2")) {
////
////                    sentsmh.setStatus("Network");
////                } else if (stato.equalsIgnoreCase("3")) {
////
////                    sentsmh.setStatus("Network");
////                } else if (stato.equalsIgnoreCase("4")) {
////
////                    sentsmh.setStatus("Delivered");
////                } else if (stato.equalsIgnoreCase("5")) {
////
////                    sentsmh.setStatus("Network");
////                } else if (stato.equalsIgnoreCase("6")) {
////
////                    sentsmh.setStatus("Undelivered");
////                } else if (stato.equalsIgnoreCase("7")) {
////
////                    sentsmh.setStatus("Expired");
////                } else if (stato.equalsIgnoreCase("8")) {
////
////                    sentsmh.setStatus("Submit Failed");
////                } else if (stato.equalsIgnoreCase("9")) {
////
////                    sentsmh.setStatus("Network");
////                } else if (stato.equalsIgnoreCase("9")) {
////
////                    sentsmh.setStatus("OptedOut");
////                } else if (stato.equalsIgnoreCase("11")) {
////
////                    sentsmh.setStatus("Scheduled");
////                }
////
////                try {
////
////                    date2 = inFormat2.parse(employee.getTimeSubmitted());
////                    date3 = inFormat2.parse(employee.getTimeProcessed());
////
////                    sentsmh.setTime_processed(outFormat.format(date3));
////                    sentsmh.setTime_submitted(outFormat.format(date2));
////                } catch (ParseException ex) {
////                    sentsmh.setTime_processed(employee.getTimeProcessed());
////                    sentsmh.setTime_submitted(employee.getTimeSubmitted());
////                }
////
////                SMSList.add(sentsmh);
////                System.out.println(employee.getId() + "completeee");
////            }
////        }
////
////        System.out.println(SMSList.size() + "ggggggggggggggggggggggg size");
////
////    }
////
////    public List<sentsm> outputvalue() {
////        return SMSList;
////    }
////
////    public String nameExcel() {
////        String name = "";
////        String dateString;
////        String dateString2;
////
////        if (sdate != null) {
////            SimpleDateFormat sdfr = new SimpleDateFormat("dd/MMM/yyyy");
////            dateString = sdfr.format(sdate);
////            dateString2 = sdfr.format(edate);
////            String name1 = "Sentsms_from";
////            String range = "to";
////            name = name1 + dateString + range + dateString2;
////        } else {
////        }
////        return name;
////    }
////
////    public String nameExcel2() {
////        String name = "";
////        String dateString;
////        Date date = new Date();
////        if (sdate != null) {
////            SimpleDateFormat sdfr = new SimpleDateFormat("dd:MMM:yyyy");
////            dateString = sdfr.format(date);
////            String name1 = user;
////            name = name1 + dateString;
////        } else {
////        }
////        return name;
////    }
////
////    public void onRowselect(SelectEvent event) {
////        FacesMessage msg = new FacesMessage("Sent Sms ", ((sentsm) event.getObject()).getMessage_payload());
////        FacesContext.getCurrentInstance().addMessage(null, msg);
//////        sentsmsview editeduser = (sentsmsview) event.getObject();
////    }
////
////    public void reset() {
////        SMSList = null;
////        smssentcount = 0l;
////    }
////
////    private static long getSmsCount(String msg) {
////        long ret = 0;
////        if (msg == null || msg.length() <= 160) {
////            return 1;
////        }
////        ret = msg.length() / 134;
////        if (msg.length() % 134 > 0) {
////            ret++;
////        }
////        return ret;
////    }
//}
