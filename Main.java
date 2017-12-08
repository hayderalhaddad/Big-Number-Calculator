package bigcalc;

/**
 *
 * @author djn
 */

import java.util.*;

public class Main
{
   static Scanner kbd = new Scanner(System.in);
   static Number currentValue = new Number("0");

   public static void main(String[] args)
   {
      boolean ok;
      do
      {
         menu();
         ok = commands();
         if (ok)
         {
            System.out.println(currentValue);
            System.out.println();
         }
      } while (ok);
   }

   private static void menu()
   {
    System.out.println("enter a value: e     add: a");
    System.out.println("subtract: s          multiply: m");
    System.out.println("reverse sign: r      clear: c");
    System.out.println("quit: q");
   }

   private static boolean commands()
   {
      boolean retVal = true;
      System.out.print("-> ");
      String input = kbd.next();
      String valueStr = null;
      Number newValue = new Number();
      switch (input.charAt(0))
      {
         case 'e':
            System.out.print("value: ");
            try
            {
               valueStr = kbd.next();
               currentValue.accept(valueStr);
            }
            catch (BadNumberException e)
            {
               beep();
               System.out.println("badly formed number: " + valueStr);
            }
            break;
         case 'a':
            System.out.print("value: ");
            try
            {
               valueStr = kbd.next();
               newValue.accept(valueStr);
               currentValue = currentValue.add(newValue);
            }
            catch (BadNumberException e)
            {
               beep();
               System.out.println("badly formed number: " + valueStr);
            }
            break;
          case 's':
            System.out.print("value: ");
            try
            {
               valueStr = kbd.next();
               newValue.accept(valueStr);
               currentValue = currentValue.subtract(newValue);
            }
            catch (BadNumberException e)
            {
               beep();
               System.out.println("badly formed number: " + valueStr);
            }
            break;
          case 'm':
            System.out.print("value: ");
            try
            {
               valueStr = kbd.next();
               newValue.accept(valueStr);
               currentValue = currentValue.multiply(newValue);
            }
            catch (BadNumberException e)
            {
               beep();
               System.out.println("badly formed number: " + valueStr);
            }
            break;
         case 'r':
            currentValue.reverseSign();
            break;
         case 'c':
            currentValue.accept("0");
            break;
         case 'q':
            retVal = false;
            break;
         default:
            beep();
      }
      return retVal;
   }

   private static void beep()
   {
      System.out.print((char)7);
   }
}
