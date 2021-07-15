import java.util.Scanner;

/*A class containing the main program.
 * Receives to the console two numbers from the user,
 * and returns the results of the arithmetic operations on the two numbers obtained -
 * addition, subtraction, multiplication, division, equalization check, check larger number.
 * Finally, it asks the user if he is interested in entering two numbers for further calculation.*/
public class Main {

    private static final int LARGER_THAN = 1;

    public static void main(String[] args) {
        Scanner userInput = new Scanner(System.in);
        boolean newCalculation = true;

        while (newCalculation) {
            System.out.println("Enter the first number, with no value limit.\n" + "An integer with digits only: ");
            BigInt firstBigIntNum = getUserBigInt(userInput);
            System.out.println("Enter the second number, with no value limit.\n" + "An integer with digits only: ");
            BigInt secondBigIntNum = getUserBigInt(userInput);

            resultsPrint(firstBigIntNum, secondBigIntNum);
            newCalculation = askNewCalculation();
        }
        userInput.close();
    }

     /*The function receives the output entered by the user.
     * If no error occurs - a BigInt number will be returned.
     * Otherwise, if a division is made at zero, the division operation alone will not be performed.
     * If the entire input is incorrect, an appropriate error will be printed, and input will continue to be requested from the user.*/
    private static BigInt getUserBigInt(Scanner userInput) {
        try{
            return new BigInt(userInput.nextLine());
        }catch (IllegalArgumentException e) {
            /* Blocking of an abnormal occurrence on invalid input.*/
            System.out.println(e.getMessage());
            System.out.println("Please try again.\n" +
                    "Enter a number with only digits. You can add a +/- sign only at the beginning of the number.");
            return getUserBigInt(userInput);
        }
    }
        /*The function returns the calculation results of the two numbers received from the user.*/
        private static void resultsPrint(BigInt firstNumber, BigInt secondNumber){
            String resultEqual = firstNumber.equals(secondNumber) ? "equals":"not equals";
            String resultCompareTo = firstNumber.compareTo(secondNumber) == LARGER_THAN ? "larger" : "smaller";

            System.out.println(firstNumber + " + "+ secondNumber+ " = "+ firstNumber.plus(secondNumber)+"\n");
            System.out.println(firstNumber + " - "+ secondNumber+ " = "+ firstNumber.minus(secondNumber)+"\n");
            System.out.println(firstNumber + " * "+ secondNumber+ " = "+ firstNumber.multiply(secondNumber)+"\n");

            try{
                System.out.println(firstNumber + " / "+ secondNumber+ " = "+ firstNumber.divide(secondNumber)+"\n");
            }catch (ArithmeticException e) {
                /* Blocking the occurrence of an arithmetic exception - division by zero.*/
                System.out.println(e.getMessage());
                System.out.println("The division operation will not be performed.\n\n");
            }

            System.out.println("The two BigInt numbers are "+ resultEqual +" equal"+"\n");
            if(resultEqual.equals("not equals")) {
                System.out.println("The first BigInt number is "+ resultCompareTo +" than the second BigInt number"+"\n");
            }
        }

    /*The function returns true or false, according to the user's answer, whether he wants to perform another calculation.*/
    private static boolean askNewCalculation() {
        Scanner userInputAnswer = new Scanner(System.in);
            System.out.println("Do you want to enter additional numbers for the calculation? Enter Y if yes or N if no");
            while (true){
                String answerUser = userInputAnswer.next();
                if(!answerUser.equals("Y") && !answerUser.equals("N"))
                    System.out.println("Incorrect entry.\n" +
                            "please enter Y if you want want to enter additional numbers for the calculation. Otherwise N");
                else
                    return answerUser.equals("Y");
            }
    }
}
