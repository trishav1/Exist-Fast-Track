package basic;

import java.util.Random;
import java.util.Scanner;

public class App {
   static Scanner scanner;
   static String[][] table;

   public App() {
   }

   public static void main(String[] var0) {
      initTable();
      menu();
   }

   public static void menu() {
      while(true) {
         System.out.println("\n__________________________");
         System.out.println("\nMENU:");
         System.out.println("[ 1 ] - Search");
         System.out.println("[ 2 ] - Edit");
         System.out.println("[ 3 ] - Print");
         System.out.println("[ 4 ] - Reset");
         System.out.println("[ x ] - Exit");
         System.out.print("Action: ");
         switch (scanner.nextLine()) {
            case "1":
               search();
               break;
            case "2":
               edit();
               break;
            case "3":
               printTable();
               break;
            case "4":
               initTable();
               break;
            case "x":
            case "X":
               System.out.println("Exiting program..."); //system.out
               return;
            default:
               System.out.println("Invalid. Enter a valid option.");
         }
      }
   }

   public static void initTable() {
      System.out.print("\nInput table dimension (e.g. 3x3): ");
      String var0 = scanner.nextLine();
      String[] var1 = var0.split("x");

      try {
         int var2 = Integer.parseInt(var1[0]);
         int var3 = Integer.parseInt(var1[1]);
         table = new String[var2][var3];

         for(int var4 = 0; var4 < var2; ++var4) {
            for(int var5 = 0; var5 < var3; ++var5) {
               table[var4][var5] = generateRandomASCII();
            }
         }

         printTable();
      } catch (Exception var6) {
         System.out.println("Invalid. Enter dimensions like 3x3.");
      }

   }

   public static String generateRandomASCII() {
      Random var0 = new Random();
      String var1 = "";

      for(int var2 = 0; var2 < 3; ++var2) {
         char var3 = (char)(33 + var0.nextInt(94));
         var1 = var1 + var3;
      }

      return var1;
   }

   public static void printTable() {
      System.out.println("\nTable:");

      for(int var0 = 0; var0 < table.length; ++var0) {
         for(int var1 = 0; var1 < table[var0].length; ++var1) {
            System.out.print(table[var0][var1] + "\t");
         }

         System.out.println();
      }

   }

   public static void search() {
      System.out.print("\nEnter character/s to search: ");
      String var0 = scanner.nextLine();
      boolean var1 = false;
      boolean var2 = false;
      System.out.println("Output:");

      for(int var3 = 0; var3 < table.length; ++var3) {
         for(int var4 = 0; var4 < table[var3].length; ++var4) {
            if (table[var3][var4].contains(var0)) {
               int var6 = 0;
               var2 = true;
               if (var0.length() != 1) {
                  if (var0.length() == 2 || var0.length() == 3) {
                     System.out.println("1 Occurrence/s at [" + var3 + ", " + var4 + "]");
                  }
                  else {
                     System.out.println("Multiple Occurrence/s at [" + var3 + ", " + var4 + "]");
                  }
               } else {
                  for(int var5 = 0; var5 < 3; ++var5) {
                     if (table[var3][var4].substring(var5, var5 + 1).equals(var0)) {
                        ++var6;
                     }
                  }

                  if (var6 > 0) {
                     System.out.println("" + var6 + " Occurrence/s at [" + var3 + ", " + var4 + "]");
                  }
               }
            }
         }
      }

      if (!var2) {
         System.out.println("String not found.");
      }

   }

   public static void edit() {
      System.out.print("Edit (e.g. 1x2): ");
      String var0 = scanner.nextLine();
      String[] var1 = var0.split("x");

      try {
         int var2 = Integer.parseInt(var1[0]);
         int var3 = Integer.parseInt(var1[1]);
         if (var2 < 0 || var3 < 0 || var2 >= table.length || var3 >= table[0].length) {
            System.out.println("Invalid index! Enter a valid index.");
            return;
         }

         System.out.print("New Value (3 chars): ");
         String var4 = scanner.nextLine();
         while (var4.length() != 3) {
            System.out.println("Must be exactly 3 characters!");
         }

         String var5 = table[var2][var3];
         table[var2][var3] = var4;
         System.out.println("Output: " + var5 + " -> " + var4);
      } catch (Exception var6) {
         System.out.println("Invalid. Use format like 1x2");
      }

   }

   static {
      scanner = new Scanner(System.in);
   }
}
