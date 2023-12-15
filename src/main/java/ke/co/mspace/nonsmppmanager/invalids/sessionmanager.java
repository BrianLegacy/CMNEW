/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.co.mspace.nonsmppmanager.invalids;

import javax.inject.Named;
import javax.enterprise.context.Dependent;




import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

/**
 *
 * @author mspace
 */


@ManagedBean(name = "sessionmanager")
@ViewScoped
public class sessionmanager implements Serializable {

    HttpSession session = getsession.getSession();
    long remainigsms;
    String company_logopath;
    private String info;

    public String getInfo() {
        
          int temporaladmin =Character.getNumericValue( (char) session.getAttribute("temporaladmin"));
       
       String agent =(String) session.getAttribute("agent");
       
       if(!agent.isEmpty()){
           return " ";
       }
       if(temporaladmin==3){
           return " ";
       }
       
        Object obj = session.getAttribute("resellerId");

        if (obj != null) {
            info = obj.toString().equals("0") ? "We hope you enjoy your MSpace SMS Experience.<br/><br/>"
                    + "For any comments or queries, please do not hesitate to contact our support desk "
                    + "support@mspace.co.ke or call us on 0722 962 934" : "";
        }
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public long getRemainigsms() {
        System.out.println("Long Range Validate rmaining sms"+remainigsms);
        return remainigsms;
    }

    public void setRemainigsms(long remainigsms) {
        this.remainigsms = remainigsms;
    }

    public String getCompany_logopath() {
        return company_logopath;
    }

    public void setCompany_logopath(String company_logopath) {
        this.company_logopath = company_logopath;
    }

    public sessionmanager() {
    }

    public String getsessionname() {

        String m = "Welcome   ";
        String username = m + (String) session.getAttribute("username");
        remainigsms = (long) session.getAttribute("max_total");

        return username;
    }
    
      public Boolean showChat() {

      String uname=FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("admin").toString();
      String agent= (String) getsession.getSession().getAttribute("agent") ;
        if(!agent.isEmpty()){
            return false;
        }
        return !uname.equalsIgnoreCase("3")?true:false;
    }

    public String getsubaccstatus() {
        String username = (String) session.getAttribute("subaccount");
        return username;
    }

    public String getsessionnameonly() {
        HttpSession session = getsession.getSession();
        String username = (String) session.getAttribute("username");
        return username;
    }

    public String getPayBill() {
        String resPayBill = (String) session.getAttribute("resellerpaybill");
        int resId = (int) session.getAttribute("resellerId");

        return resId > 0 ? resPayBill : "778827";
    }

    public String getLogoPath() {
        HttpSession session = getsession.getSession();
        company_logopath = (String) session.getAttribute("logopath");

        //String path = new File("").getAbsolutePath();
        //String paath = session.getServletContext().getRealPath("/");
        System.out.println("String real path: "+company_logopath);
         System.out.println("String real path: "+(String) session.getAttribute("logopath2"));
//         return "https://i.pinimg.com/originals/cf/8a/11/cf8a11b44a748c4ce286fb020f920ada.png";
        return company_logopath;
        
      
    }
    
    public String getUrl() {
        HttpSession session = getsession.getSession();
             String agent =(String) session.getAttribute("agent");
        int temporaladmin =Character.getNumericValue( (char) session.getAttribute("temporaladmin"));
         if(!agent.isEmpty()){
            return "";
        }
        if(temporaladmin==3){
            return "";
        }
        return (String) session.getAttribute("urlLink");
    }
    public String getClientname() {
         String agent =(String) session.getAttribute("agent");
        int temporaladmin =Character.getNumericValue( (char) session.getAttribute("temporaladmin"));
        if(!agent.isEmpty()){
            return "";
        }
        if(temporaladmin==3){
            return "";
        }
        HttpSession session = getsession.getSession();
        return (String) session.getAttribute("clientname");
    }
}
