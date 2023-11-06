/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.co.mspace.nonsmppmanager.invalids;

//import com.mspace1.controller.tuseraddressbook;
import org.mspace.clientmanager.util.getsession;
import java.io.IOException;
import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import ke.co.mspace.nonsmppmanager.util.SessionUtil;
import org.primefaces.context.RequestContext;

/**
 *
 * @author mspace
 */
@ManagedBean(name = "userInfo")
@SessionScoped
public class UserInfo implements Serializable {

//    private tuseraddressbook user;
    private final String lagent = (String) getsession.getSession().getAttribute("agent");
    @ManagedProperty(value = "#{facePainter}")
    public FacePainter facePainter;

    private long maxTotal = (long) getsession.getSession().getAttribute("max_total");

    public boolean isShortCode() {
        return getsession.getSession().getAttribute("agent").equals("shortcode");
    }

    public String getShortCodeStatus() {
        return lagent.equals("shortcode") ? "none" : "show";
    }

    public FacePainter getFacePainter() {
        return facePainter;
    }

//    public tuseraddressbook getUser() {
//        return user;
//    }
//
//    public void setUser(tuseraddressbook user) {
//        this.user = user;
//    }

    public void setFacePainter(FacePainter facePainter) {
        this.facePainter = facePainter;
    }
             public void viewpaybills(){
        facePainter.setMainContent("clientmanager/paybill/managepaybills.xhtml");
    }

            public void addPaybill() {
        facePainter.setMainContent("clientmanager/paybill/addpaybill.xhtml");
    }
            public void viewcallbacks() {
        facePainter.setMainContent("clientmanager/callbacks/managecallbacks.xhtml");
    }
            public void smsoutreport() {
        facePainter.setMainContent("clientmanager/reports/smsoutreport.xhtml");
    }
               public void smppoutreport() {
        facePainter.setMainContent("clientmanager/reports/smppoutreport.xhtml");
    }
             public void optoutreport() {
        facePainter.setMainContent("clientmanager/reports/optoutreport.xhtml");
    }
         public void alluservisualreport() {
        facePainter.setMainContent("clientmanager/reports/alluservisualreport.xhtml");
    }
             public void singleuservisualreport() {
        facePainter.setMainContent("clientmanager/reports/singleuservisualreport.xhtml");
    }
            
         public void credithistoryreport() {
        facePainter.setMainContent("clientmanager/reports/credithistoryreport.xhtml");
    }
                public void addcallback() {
        facePainter.setMainContent("clientmanager/callbacks/addcallback.xhtml");
    }
                           public void bulkreports() {
        facePainter.setMainContent("clientmanager/reports/bulkreports.xhtml");
    }

    public void smshome() {
        facePainter.setMainContent("clientmanager/groups/addgroupuser.xhtml");
    }
      public void addReseller() {
        facePainter.setMainContent("clientmanager/managereseller/addreseller.xhtml");
    }
       public void pricingTable() {
        facePainter.setMainContent("clientmanager/pricing/emailpricing.xhtml");
    }
        public void myProfile() {
        facePainter.setMainContent("clientmanager/myaccount/myprofile.xhtml");
    }
         public void accountTopup() {
        facePainter.setMainContent("clientmanager/myaccount/account_top_up.xhtml");
    }
          public void sendSMS() throws IOException {
             
              int agent=Integer.valueOf((String)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("agent"));
              
              String url="https://smsgateway.mspace.co.ke/newSMS/reseller.jsf?id="+agent;
     FacesContext.getCurrentInstance().getExternalContext()
    .redirect(url);
    }
              public void manageReseller() {
        facePainter.setMainContent("clientmanager/managereseller/managereseller.xhtml");
    }
     public void addsmsuser() {
          facePainter.setMainContent("clientmanager/manageuserdetails/addsmsuser.xhtml");
    }
     public void addemailuser() {
          facePainter.setMainContent("clientmanager/manageuserdetails/addemailuser.xhtml");
    }
      public void viewsmsuser() {
          facePainter.setMainContent("clientmanager/manageuserdetails/showsmsusers.xhtml");
    }
       public void viewalphanumerics() {
          facePainter.setMainContent("clientmanager/manageuserdetails/managealphanumerics.xhtml");
    }
      
        public void viewemailuser() {
          facePainter.setMainContent("clientmanager/manageuserdetails/manageemailusers.xhtml");
    }
    
 public void addgroupuser() {
          facePainter.setMainContent("clientmanager/groups/addgroupuser.xhtml");
    }
 
 public void viewgroupuser() {
          facePainter.setMainContent("clientmanager/groups/viewgroups.xhtml");
    }
    public void newsms() {
        facePainter.setMainContent("sms/newsms.xhtml");
    }
    
      public void billingSuccessRedirect() {
        facePainter.setMainContent("sms/billingsuccess.xhtml");
    }
      
       public void billingFailureRedirect() {
      facePainter.setMainContent("sms/billingsuccess.xhtml");
    }

    public void newsmsgroup() {
        facePainter.setMainContent("sms/newgroupsms.xhtml");
    }

    public void uploadexcel() {
        facePainter.setMainContent("sms/excelupld.xhtml");
    }

    public void upload_addressExcel() {
        facePainter.setMainContent("sms/addressupload.xhtml");
    }

    public void smsreceived() {
        System.out.println("received clicked.....................");
        facePainter.setMainContent("sms/smsin.xhtml");
    }

    public void sentsms() {
        facePainter.setMainContent("sms/sentsms.xhtml");
    }
    
    public void ussd() {
        facePainter.setMainContent("ussd/ussdrprt.xhtml");
    }

    public void userprofile() {
        facePainter.setMainContent("sms/account.xhtml");
    }

    public void addressbook() {

        facePainter.setMainContent("sms/addressbook.xhtml");
    }

    public void birthdaySchedule() {

        facePainter.setMainContent("Birthday/birthdaySchedule.xhtml");
    }

    public void messagetemplate() {
       System.out.println("Templates loading");

        facePainter.setMainContent("sms/messagetemplates.xhtml");
    }
    public void bulkyReports(){

        facePainter.setMainContent("bulkreports/report.xhtml");
    }

    public void task() {
        facePainter.setMainContent("sms/task.xhtml");
    }

    public void email() {

        facePainter.setMainContent("sms/email.xhtml");
    }

    public void billing() {

        facePainter.setMainContent("sms/billing.xhtml");
//  facePainter.setMainContent("sms/billingModal.xhtml");
    }
    
    public void topups() {

        facePainter.setMainContent("sms/topups.xhtml");
    }
    //invisible

    public void addcontact() {
        facePainter.setMainContent("sms/addcontacts.xhtml");
    }

    public void viewuploaded() {
        facePainter.setMainContent("sms/viewuploaded.xhtml");
    }

    public void shortCode() {
        facePainter.setMainContent("sms/shortcode.xhtml");
    }

    //mspacepesa
    public void dashboard() {
        
        if (maxTotal > 1) {
            facePainter.setMainContent("MspacePESA/Mspesa_dashboard.xhtml");
        } else {
            RequestContext context = RequestContext.getCurrentInstance();
            context.execute("PF('myDialogVar2').show();");
        }
    }

    public void description() {
        facePainter.setMainContent("MspacePESA/mspesa_description.xhtml");
    }
    
      public void processApi() {
        if (maxTotal > 1) {
            facePainter.setMainContent("API/paybilldetails.xhtml");
        } else {
            RequestContext context = RequestContext.getCurrentInstance();
            context.execute("PF('myDialogVar2').show();");
        }
    }

    public void processed() {
        if (maxTotal > 1) {
            facePainter.setMainContent("MspacePESA/processed.xhtml");
        } else {
            RequestContext context = RequestContext.getCurrentInstance();
            context.execute("PF('myDialogVar2').show();");
        }
    }

    public void unprocessed() {
        if (maxTotal > 1) {
            facePainter.setMainContent("MspacePESA/unprocessed.xhtml");
        } else {
            RequestContext context = RequestContext.getCurrentInstance();
            context.execute("PF('myDialogVar2').show();");
        }
    }

    public void paybillDetails() {

        if (maxTotal > 1) {
            facePainter.setMainContent("MspacePESA/paybilldetails.xhtml");
        } else {
            RequestContext context = RequestContext.getCurrentInstance();
            context.execute("PF('myDialogVar2').show();");
        }
    }

    //survey
    public void survey() {
        facePainter.setMainContent("survey/survey.xhtml");
    }

    public void surveySummary() {
        facePainter.setMainContent("survey/summary.xhtml");
    }

    public void surveyView() {

        facePainter.setMainContent("survey/details.xhtml");
    }
}
