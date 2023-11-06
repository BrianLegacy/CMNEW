/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.co.mspace.nonsmppmanager.invalids;

import java.util.List;
import ke.co.mspace.nonsmppmanager.invalids.Tuser;

/**
 *
 * @author mspace
 */
public interface tuserinterface {

    public Tuser getUser(String username, String password);
        public Tuser getUserJDBC(String username, String password);
public Tuser getUserWithSha256(String username, String password);
    public void update(Tuser user);

    public void remove(Tuser user);

    public List<Tuser> list();

    public Tuser getbyid(long id);

    public void saveuser(Tuser user);

    public void logindetailupdate();

}
