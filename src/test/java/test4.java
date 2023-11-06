
import java.util.Random;



public class test4 {

    public static void main(String[] args) {
        String dest="abcdefghighj";
    
        
        
        
         int length = 7;
        String characters = "abcdefghjkmnpqrstwyz23456789";
        Random rng = new Random();
        char[] text = new char[length];
        for (int i = 0; i < length; i++) {
            text[i] = characters.charAt(rng.nextInt(characters.length()));
        }
        
        
        
        
       System.out.println( text);
        
        
    }

    

}
