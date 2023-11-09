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
public class AddCredits implements ManageCreditsOperations{
    org.slf4j.Logger logger= LoggerFactory.getLogger(AddCredits.class);

    @Override
    public void execute(CreditRequestObject creditRequestObject) {
         SMSCredits smsCredit = new SMSCredits();
          ManageCreditApi creditManager = new ManageCreditImpl();
          UserServiceApi userService = new UserServiceImpl();
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("credit_type", "add");
                    final int previous_balance = creditRequestObject.smsCredits;
                    creditRequestObject.smsCredits = creditRequestObject.smsCredits + creditRequestObject.creditsToManage;
                    final int new_balance = creditRequestObject.smsCredits;
                    int newBalace;
                    //if((curent>0) && (creditsToManage<=current) && (currennt!=-1))
                    if (creditRequestObject.current < creditRequestObject.creditsToManage &&
                            creditRequestObject.current != -1) {

                        JsfUtil.addErrorMessage("You have insufficient SMS balance.Your balance is: "
                                + creditRequestObject.current + " SMS");
                        // System.out.println(" The Operation is invalid ");
                    } else {
             try {
                 String updateUser = ("User Update::: " + new Date() + " User: "
                         +creditRequestObject.username + " Previous Balance: " +
                         previous_balance + " Credit Allocated: " + creditRequestObject.creditsToManage + " New Balance: " + new_balance);
                 //System.out.println(updateUser);
                 //System.out.println("Sms credits to manage :" + creditsToManage + "New balance: " + new_balance + "prevoius Balance: " + previous_balance);
                 smsCredit.setUsername(creditRequestObject.username);
                 smsCredit.setActionTime(new Date());
                 smsCredit.setActionType(creditRequestObject.adminv == '1' ? '1' : '3');
                 smsCredit.setNumCredits(creditRequestObject.creditsToManage);
                 smsCredit.setNew_balance(new_balance);
                 smsCredit.setPrevious_balance(previous_balance);
                 //creditManager.persistUpdate(smsCredit, conn);
                 creditManager.persistUpdate2(smsCredit, creditRequestObject.connection, creditRequestObject.creditsToManage, creditRequestObject.current, creditRequestObject.current - creditRequestObject.creditsToManage);
                 newBalace = creditRequestObject.adminv == '1' ? -1 : creditRequestObject.current - creditRequestObject.creditsToManage;
                 FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("new_balace", newBalace);
                 userService.updateCredits(creditRequestObject.username, creditRequestObject.smsCredits, creditRequestObject.connection);
                 userService.updateAgentCredits(creditRequestObject.agent,creditRequestObject.current, creditRequestObject.creditsToManage
                         , creditRequestObject.current - creditRequestObject.creditsToManage, creditRequestObject.connection);
                 //=======================
                 String message="You have successfully added " + creditRequestObject.creditsToManage
                         + " SMS Credits to user " + creditRequestObject.username;
                 JsfUtil.addSuccessMessage(message);
                 logger.info(message);
             } catch (SQLException ex) {
                 logger.error(ex.getMessage());
             }
                    }
             }
    }
    

