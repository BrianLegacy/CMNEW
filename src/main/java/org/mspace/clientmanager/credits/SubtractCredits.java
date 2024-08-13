/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mspace.clientmanager.credits;

import java.sql.SQLException;
import java.util.Date;
import javax.faces.context.FacesContext;
import ke.co.mspace.nonsmppmanager.service.UserServiceApi;
import ke.co.mspace.nonsmppmanager.service.UserServiceImpl;
import ke.co.mspace.nonsmppmanager.util.JsfUtil;
import org.mspace.clientmanager.credits.model.SMSCredits;
import org.mspace.clientmanager.credits.services.ManageCreditApi;
import org.mspace.clientmanager.credits.services.ManageCreditImpl;
import org.slf4j.LoggerFactory;

/**
 *
 * @author developer
 */
public class SubtractCredits implements ManageCreditsOperations {

    org.slf4j.Logger logger = LoggerFactory.getLogger(SubtractCredits.class);

    @Override
    public void execute(CreditRequestObject creditRequestObject) {
        SMSCredits smsCredit = new SMSCredits();
        ManageCreditApi creditManager = new ManageCreditImpl();
        UserServiceApi userService = new UserServiceImpl();
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("credit_type", "update");
        final int previous_balance2 = creditRequestObject.smsCredits;
        if (creditRequestObject.creditsToManage > previous_balance2 && previous_balance2 != -1) {
            JsfUtil.addErrorMessage("Balance is lower than SMS to deduct");
            //System.out.println("Balance is lower than sms todeduct");
        } else {
            try {
                int afterBalance = previous_balance2 - creditRequestObject.creditsToManage;
                creditRequestObject.smsCredits = creditRequestObject.creditsToManage;
                final int new_balance2 = afterBalance;
                smsCredit.setUsername(creditRequestObject.username);
                smsCredit.setActionType(creditRequestObject.adminv == '1' ? '2' : '3');
                smsCredit.setNumCredits(creditRequestObject.creditsToManage);
                smsCredit.setNew_balance(new_balance2);
                smsCredit.setPrevious_balance(previous_balance2);
                //
                //creditManager.persistUpdate(smsCredit, conn,);
                creditManager.persistUpdate2(smsCredit, creditRequestObject.connection,
                        creditRequestObject.creditsToManage, creditRequestObject.current,
                        creditRequestObject.current+creditRequestObject.creditsToManage);
                
                
                userService.updateCredits(creditRequestObject.username,
                        previous_balance2 - creditRequestObject.smsCredits, creditRequestObject.connection);
//                        newBalace = creditRequestObject.adminv == '1' ? -1 : previous_balance2 - smsCredits;
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("new_balace", previous_balance2 - creditRequestObject.creditsToManage);
                userService.updateAgentCredits(creditRequestObject.agent, creditRequestObject.current,
                        creditRequestObject.creditsToManage, previous_balance2 - creditRequestObject.creditsToManage,
                        creditRequestObject.connection);
                userService.alterAgentCredits(creditRequestObject.agent,
                        creditRequestObject.current, previous_balance2, creditRequestObject.creditsToManage,
                        creditRequestObject.connection);
// System.out.println("Credits to manage ==" + creditsToManage + "\n" + "Previous balance is ==" + previous_balance2 + "And Curent:" + current);
//System.out.println("SMS after alter is :" + smsCredits);
//FacesContext.getCurrentInstance().getExternalContext().redirect(toRedirect);
                String message = "You have successfully deducted "
                        + creditRequestObject.creditsToManage + " SMS  " + "from  " + creditRequestObject.username;
                JsfUtil.addSuccessMessage(message);
                logger.info(message);
            } catch (SQLException ex) {
                logger.error(ex.getMessage());
            }
        }
    }

}
