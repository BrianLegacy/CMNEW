/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.co.mspace.nonsmppmanager.service;

//import com.sun.faces.config.rules.FacesConfigRuleSet;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.context.FacesContext;
import static ke.co.mspace.nonsmppmanager.model.AuthenticationBean.AUTH_KEY;
import ke.co.mspace.nonsmppmanager.model.tclientModel;
import ke.co.mspace.nonsmppmanager.util.JdbcUtil;
//import org.eclipse.jdt.internal.compiler.ast.ThisReference;

/**
 *
 * @author sofwaredeveloper
 */
public class tClientManager {
    
    String logo;

    public String getLogo() {
        
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }
    JdbcUtil dbcon= new JdbcUtil();
    Connection con;
    public tclientModel tmodel;
    
    public List <tclientModel> tclientlist;

    public tclientModel getTmodel() {
        return tmodel;
    }

    public void setTmodel(tclientModel tmodel) {
        this.tmodel = tmodel;
    }

    public List<tclientModel> getTclientlist() {
        return tclientlist;
    }

    public void setTclientlist(List<tclientModel> tclientlist) {
        this.tclientlist = tclientlist;
    }

    /**
     * Creates a new instance of tClientManager
     */
    public tClientManager() {
    }
    
    public String  loadTclient(int id){
        String sql="SELECT id,clientName email picPath from tClient where id ='"+id+"'";
        String logo="";
        try {
//            tclientlist= new ArrayList();
//            tclientModel model=new tclientModel();
            con=dbcon.getConnectionTodbPAYMENT();
            Statement st=con.createStatement();
            ResultSet rs=st.executeQuery(sql);
            
            while(rs.next()){
//               model.setClientName(rs.getString("clientName"));
//               model.setId(rs.getInt("id"));
//               model.setEmail("email");
//               model.setSystType(rs.getString("systemType"));
//               tclientlist.add(model);
                 logo=rs.getString("picPath");
                 
               
            }
           System.out.println("---------------"+sql);
        } catch (SQLException ex) {
            Logger.getLogger(tClientManager.class.getName()).log(Level.SEVERE, null, ex);
        }
       return logo;
    }
  
    
    
    
}
