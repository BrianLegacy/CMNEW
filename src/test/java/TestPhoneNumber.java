
import com.google.i18n.phonenumbers.PhoneNumberToCarrierMapper;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;
import java.util.Locale;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Samson
 */
public class TestPhoneNumber {

    public static void main(String[] args) {
        new TestPhoneNumber().runPhoneNumber();
    }

    public void runPhoneNumber() {
        PhoneNumber num = new PhoneNumber();
        num.setCountryCode(254);
        num.setNationalNumber(731893456);
        PhoneNumberToCarrierMapper mapper = PhoneNumberToCarrierMapper.getInstance();
        System.out.println("Carrier is :" + mapper.getNameForValidNumber(num, Locale.US));
    }

}
