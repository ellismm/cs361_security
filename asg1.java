// Mark Ellis
// MME839
// Assignment #1

import java.lang.*;
import java.io.*;
import java.util.*;


public class asg1 {

  //public static char[][] shift_block(char[][], char, int, int); 
  public static void main(String argc[]) {
    char[][] block1 = {{'A', 'B', 'C', 'D', 'E'}, {'F', 'G', 'H', 'I', 'J'}, {'K', 'L', 'M', 'N', 'O'}, {'P', 'R', 'S', 'T', 'U'}, {'V', 'W', 'X', 'Y', 'Z'}};
    char[][] block2 = {{'A', 'B', 'C', 'D', 'E'}, {'F', 'G', 'H', 'I', 'J'}, {'K', 'L', 'M', 'N', 'O'}, {'P', 'R', 'S', 'T', 'U'}, {'V', 'W', 'X', 'Y', 'Z'}};
     
    Scanner sc = new Scanner(System.in);
    
    // User option
    System.out.print("\nDo you want to encrypt(type 'e') or decrypt (type 'd') a message? ");
    String option = sc.next();
    while((option.toLowerCase()).charAt(0) != 'e' && (option.toLowerCase()).charAt(0) != 'd') {
      System.out.print("This is not a a valid option: ");
      System.out.print("\nDo you want to encrypt(type 'e') or decrypt (type 'd') a message? ");
      option = sc.next();
    }

    // First keyword 
    System.out.print("\nWhat is your first keyword? "); 
    String keyword1 = sc.next();
    keyword1 = keyword1.replaceAll("[^a-zA-Z]","");
    keyword1 = keyword1.replaceAll("[qQ]", "");

    // Second keyword
    System.out.print("\nWhat is your second keyword? "); 
    String keyword2 = sc.next();
    keyword2 = keyword2.replaceAll("[^a-zA-Z]","");
    keyword2 = keyword2.replaceAll("q[qQ]", "");
    
    // Set up the two blocks
    keyword1 = keyword1.toUpperCase();
    keyword2 = keyword2.toUpperCase();
    block1 = two_square(block1, keyword1);
    block2 = two_square(block2, keyword2);

    // Determine to encrypt or to decrypt the user message 
    if((option.toLowerCase()).charAt(0) == 'e') {
      System.out.print("\nWhat is the message you want to encrypt? ");
      sc.nextLine();
      String message = sc.nextLine();
      message = message.replaceAll("[qQ]", "");
      message = message.replaceAll("[^a-zA-Z]","");
      message = encrypt(block1, block2, message);
      System.out.println("\nencrypted: \n" + message);
    }
    else if((option.toLowerCase()).charAt(0) == 'd') {
      System.out.print("\nWhat is the message you want to decrypt? ");
      sc.nextLine();
      String message = sc.nextLine();
      message = message.replaceAll("[^a-zA-Z]","");
      message = message.replaceAll("[qQ]", "");
      message = decrypt(block1, block2, message);
      System.out.println("\nDecrypted: \n" + message);
    }   
  }

  // block two_square set up given a keyword
  public static char[][] two_square(char[][] block, String key) {
    char[] shift = key.toCharArray();
    int r = 0;
    int c = 0;
    for(int i = 0; i < shift.length; i++) {
      boolean dup = false;
      for(int j = 0; j < i; j++) {
        if(shift[j] == shift[i]) {
          dup = true;
        }
      }

      if(dup) {
        continue;
      }

      char temp = block[r][c];
      char temp2 = shift[i];
      block[r][c] = shift[i];
      if(temp != temp2) {
        block = shift_block(block, temp, r, c);
      }

      if(c == 4) {
        c = 0;
        r++;
      }
      else
        c++;
    }
    for(int i = 0; i < 5; i++) {
      for(int j = 0; j < 5; j++) {
        System.out.print(block[i][j] + " ");
      }
      System.out.println();
    }
    System.out.println();
    return block;
  }

  //shifts the each letter of a given block by one
  public static char[][] shift_block(char[][] block, char key, int r, int c) {
    char remove = block[r][c];
    if(c == 4) {
      r++;
      c = 0;
    }
    else
      c++;

    //determine where the letter is that is being replaced
    boolean found = false;
    for(; r < 5; r++) {
      for(; c < 5; c++) {
        found = false;
        if(block[r][c] == remove) {
          found = true;
          break;
        }
      }

      if(found) {
        break;
      }
      c = 0;
    }
    
    //shift the block
    boolean shifted = false;
    for(; r > -1; r--) {
      for(; c > -1; c--) {
        if(c == 0) {
          if(block[r-1][4] == remove) {
            block[r][c] = key;
            shifted = true;
            break;
          }
          block[r][c] = block[r-1][4];
        }
        else {
          if(block[r][c-1] == remove) {
            block[r][c] = key;
            shifted = true;
            break;
          }
          block[r][c] = block[r][c-1];
        }

        if(shifted) {
          break;
        }
      }

      if(shifted) {
        break;
      }

      c = 4;

    }
    return block;
  }


  // returns plain text of given message
  public static String plain_text(String message) {
    String encrypted = "";

    for(int i = 0; i < message.length(); i++) {
      while(Character.isWhitespace(message.charAt(i)) && i <= message.length())
        i++; 
      encrypted += message.charAt(i);

      i++;
      if(i == message.length()) {
        encrypted += 'z';
        encrypted += " ";
        continue;
      }
      while(Character.isWhitespace(message.charAt(i)) && i <= message.length()) 
        i++;
      encrypted += message.charAt(i) + " "; 
    }
    return encrypted.substring(0, encrypted.length() - 1);
  }

  public static String encrypt(char[][] block1, char[][] block2, String message) {
    String p_text = plain_text(message);
    System.out.println("p_text: " + p_text);
    String encrypted2 = "";
    int[] loc1 = {0,0};
    int[] loc2 = {0,0};

    p_text = p_text.toUpperCase();

    for(int i = 0; i < p_text.length(); i++) {
      if(p_text.charAt(i) == ' ')
        continue;
      for(int r = 0; r < 5; r++) {
        for(int c = 0; c < 5; c++) {
          if(block1[r][c] == p_text.charAt(i)) {
            loc1[0] = r;
            loc1[1] = c;
            i++;
            r = 5;
            break; 
          }
        }
      }

      while(p_text.charAt(i) == ' ')
        i++;
      for(int r = 0; r < 5; r++) {
        for(int c = 0; c < 5; c++) {
          if(block2[r][c] == p_text.charAt(i)) {
            loc2[0] = r;
            loc2[1] = c;
            r = 5;
            break;
          }
        }
      }
      encrypted2 += block1[loc1[0]][loc2[1]] + "" + block2[loc2[0]][loc1[1]] + " ";
    }
    return encrypted2.substring(0,encrypted2.length() - 1);
  }

  // Decrypt a given encrypted message
  public static String decrypt(char[][] block1, char[][] block2, String message) {
    String decrypted = "";
    int[] loc1 = {0, 0};
    int[] loc2 = {0, 0};
    message = message.toUpperCase();

    for(int i = 0; i < message.length(); i++) {
      if(Character.isWhitespace(message.charAt(i))) {
        continue;
      }
      for(int r = 0; r < 5; r++) {
        for(int c = 0; c < 5; c++) {
          if(block1[r][c] == message.charAt(i)) {
            loc1[0] = r;
            loc1[1] = c;
            i++;
            r = 5;
            break;
          }
        }
      }

      if(i == message.length())
        break;
      
      while(Character.isWhitespace(message.charAt(i))) {
        i++;
        if(i == message.length()) {
          break;
        }
      }
      if(i == message.length())
        break;
      for(int r = 0; r < 5; r++) {
        for(int c = 0; c < 5; c++) {
          if(block2[r][c] == message.charAt(i)) {
            loc2[0] = r;
            loc2[1] = c;
            r = 5;
            break;
          }
        }
      }
      decrypted += block1[loc1[0]][loc2[1]] + "" + block2[loc2[0]][loc1[1]] + " ";
    }
    return decrypted.substring(0, decrypted.length() - 1);
  }
}
