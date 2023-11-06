/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.co.mspace.export;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.Query;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import ke.co.mspace.nonsmppmanager.model.tLargeFileExport;

/**
 *
 * @author Samson
 */
@ManagedBean
@RequestScoped
public class LargeFileExportController implements Serializable{
  
    private List<tLargeFileExport> largeFileExport;
    private String catalina;
    private LargeFileExport lfe=new LargeFileExport();
    
    

    public String getCatalina() throws MalformedObjectNameException, UnknownHostException {
        MBeanServer beanServer = ManagementFactory.getPlatformMBeanServer();
        Set<ObjectName> objectNames = beanServer.queryNames(new ObjectName("*:type=Connector,*"),
                Query.match(Query.attr("protocol"), Query.value("HTTP/1.1")));
        String host = InetAddress.getLocalHost().getHostAddress();
        String port = objectNames.iterator().next().getKeyProperty("port");
        String ipadd = "http" + "://" + host + ":" + port;
        System.out.println(ipadd);
       return  ipadd;
    }

    public void setCatalina(String catalina) {
        this.catalina = catalina;
    }
    
    @PostConstruct
    public void init(){
        System.out.println("post init called");
        largeFileExport=lfe.getExports();
         System.out.println("post init called2");
    }
    public LargeFileExportController() {
    }

    public LargeFileExportController(List<tLargeFileExport> largeFileExport) {
        this.largeFileExport = largeFileExport;
    }

    public List<tLargeFileExport> getLargeFileExport() {
//        System.out.println("reloaded");
//        System.out.println("a getlarge hit with "+largeFileExport.size());

        return largeFileExport;
    }

    public void setLargeFileExport(List<tLargeFileExport> largeFileExport) {
        this.largeFileExport = largeFileExport;
    }
    
    public double round(double str) {
      return  Math.round(str);
    }

    @Override
    public String toString() {
        return "LargeFileExportController{" + "largeFileExport=" + largeFileExport + '}';
    }
    
    public boolean filtermethod(Object obj){
        tLargeFileExport le=(tLargeFileExport)obj;
        if(le.getId()==128)
            return true;
        else 
            return true;
    }
    
    public ServletOutputStream download() throws IOException{
         
                                 
            String filename = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("file");
            HttpServletResponse httpServletResponse = (HttpServletResponse)FacesContext.getCurrentInstance().getExternalContext().getResponse();
             ServletOutputStream servletOutputStream=httpServletResponse.getOutputStream();
            File file=new File(filename);
            

            if(file==null){
                return null;
            }
            try{
              
                
            
           ServletContext servletContext= (ServletContext)FacesContext.getCurrentInstance().getExternalContext().getContext();
           String content_type=servletContext.getMimeType(file.getName());
                
                httpServletResponse.setHeader("Pragma", "no-cache");
                httpServletResponse.setDateHeader("Expires", 0);
                httpServletResponse.setHeader("Content-disposition", "attachment;filename="+file.getName());
                httpServletResponse.setContentType(content_type);
                System.out.println("called"+file.getName()); 
                   
                FileInputStream fileInputStream=new FileInputStream(file);
           //
             
           //
           int i=fileInputStream.read();
           while(i!=-1){
               servletOutputStream.write(i);
               fileInputStream.read();
           }
           servletOutputStream.flush();
           fileInputStream.close();
           servletOutputStream.close();
            } catch (FileNotFoundException ex) {
            Logger.getLogger(LargeFileExportController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return servletOutputStream;
    }

    
    public void downloadFile() {
       
    String filename = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("file");
        
    File file = new File(filename);
    Long length=file.length();
      
      String mit=URLConnection.guessContentTypeFromName(file.getName());
        System.out.println(mit+"contetn");
         System.out.println(length+"content length");
         System.out.println(file.getAbsolutePath());
    HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();  
    response.reset();
    String mimeType= "application/vnd.ms-excel";
       //Set Headers
      response.setHeader("Content-Type",mimeType);
       response.setHeader("Content-Disposition", "attachment;filename=\""+file.getName()+"\"");
       //response.setHeader("Content-Length",String.valueOf(length));
      
      
   
    ServletOutputStream out = null;  
    try {  
        FileInputStream input = new FileInputStream(file);  
        byte[] buffer = new byte[1024];  
        out = response.getOutputStream();  
        int i = 0;  
        while ((i = input.read(buffer)) != -1) {  
            
            out.write(buffer);  
            out.flush();  
        }  
       
        FacesContext.getCurrentInstance().getResponseComplete();  
        input.close();
    } catch (IOException err) {  
        err.printStackTrace();  
    } finally {  
        try {  
            if (out != null) {  
                out.close();  
            }  
             
        } catch (IOException err) {  
            err.printStackTrace();  
        }  
    }  
   

}

    public LargeFileExport getLfe() {
        return lfe;
    }

    public void setLfe(LargeFileExport lfe) {
        this.lfe = lfe;
    }
    
    
 public void reload() throws IOException {
//       String url=FacesContext.getCurrentInstance().getViewRoot().getViewId() + "?faces-redirect=true";
    
//     ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
//    ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI());
//          LargeFileExport lfeReload=new LargeFileExport();
//         largeFileExport= lfe.getExports();
//       setLargeFileExport(getLfe().getExports());
        
        System.out.println("a hit with "+largeFileExport.size());
         
    }
    
}
