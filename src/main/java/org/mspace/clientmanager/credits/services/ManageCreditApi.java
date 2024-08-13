/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mspace.clientmanager.credits.services;

import java.sql.Connection;
import java.sql.SQLException;
import ke.co.mspace.nonsmppmanager.model.EmailCredits;
import org.mspace.clientmanager.credits.model.SMSCredits;

/**
 *
 * @author mspace
 */
public interface ManageCreditApi {

    public void persistUpdate(SMSCredits smsCredits, Connection conn);

    public void newEmailPersist(SMSCredits smsCredits, Connection conn);

    public void persistUpdate2(SMSCredits smsCredits, Connection conn, int creditsToManage, int prevBal, int neBal);

    public void persistUpdateEmail(SMSCredits smsCredits, Connection conn, int creditsToManage, int prevBal, int neBal);

    public void persistUpdate(EmailCredits credits, Connection conn);

    public void persistUpdate(int emailCredits, Connection conn, int creditsToManage, int previous_balance2, int i);

}
