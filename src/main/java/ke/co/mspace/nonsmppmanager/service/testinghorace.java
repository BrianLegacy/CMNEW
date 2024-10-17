/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.co.mspace.nonsmppmanager.service;
import java.io.File;
import java.util.*;

public class testinghorace {

    public static void main(String[] args) throws Exception {
        File myFile = new File("word.txt");
        System.out.println("Attempting to read from file in: "+myFile.getCanonicalPath());

        Scanner input = new Scanner(myFile);
        String in = "";
        in = input.nextLine();
    }

}
