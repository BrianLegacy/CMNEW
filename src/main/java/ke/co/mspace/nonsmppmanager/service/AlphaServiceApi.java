/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.co.mspace.nonsmppmanager.service;

import com.sun.faces.taglib.jsf_core.SelectItemsTag;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import javax.faces.model.SelectItem;
import ke.co.mspace.nonsmppmanager.model.Alpha;
import ke.co.mspace.nonsmppmanager.model.Alpnumeric;

/**
 *
 * @author Norrey Osako
 */
public interface AlphaServiceApi {

    List<SelectItem> getAlphanumericsNames();

    List<Alpnumeric> getAlphanumerics(Connection conn);

    String getAlphanumericType(String alpha, Connection conn);

    List<String> getAirTelAlphas(Connection conn);

    List<Alpha> getAgentAlphas(Connection conn, String user);

    List<Alpha> getAllAlphanumerics(Connection conn) throws SQLException;

    List<Alpha> loadAlphanumerics(String username, Connection conn);

    Alpha loadAlphanumericByUsername(String selectedUsername,Connection conn) throws SQLException;
    boolean findAlphanumericByUsername(String selectedUsername, String alpha, Connection conn) throws SQLException;

    int updateAlpha(String username, String alpha, String sid_type, Connection conn) throws SQLException;

    int persistAlpha(String username, String alpha, String alphatype, Connection conn) throws SQLException;

    void updateAlphaByUsername(String previousUsername, String username, Connection conn) throws SQLException;

    public void generateXSL();

    public List<String> getUnusagnedphanumericsNames(Connection conn);

    public boolean updateAgentAlphas(String username, String alphanumeric, Connection conn) throws SQLException;

    public boolean removeAgentAlpha(Alpha selected, Connection conn) throws SQLException;

    public int removeUseAlpha(String alphanumeric, String username, Connection conn) throws SQLException;

}
