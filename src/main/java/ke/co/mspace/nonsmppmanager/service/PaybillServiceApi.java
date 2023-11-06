package ke.co.mspace.nonsmppmanager.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import ke.co.mspace.nonsmppmanager.model.Paybill;

public abstract interface PaybillServiceApi
{
  public abstract List<Paybill> getAllPaybill(Connection paramConnection);
  
  public abstract Paybill loadAlphanumericByUsername(String paramString, Connection paramConnection)
    throws SQLException;
  
  public abstract void updatePaybill(String paramString1, String paramString2, Connection paramConnection)
    throws SQLException;
  
  public abstract void persistPaybill(String paramString1, String paramString2, Connection paramConnection)
    throws SQLException;
  
  public abstract void updatePaybillByUsername(String paramString1, String paramString2, Connection paramConnection)
    throws SQLException;
  
  public abstract void generateXSL();
}


/* Location:              /home/admin/Downloads/navicat100_lite_en/clientmanager.war!/WEB-INF/classes/ke/co/mspace/nonsmppmanager/service/PaybillServiceApi.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
