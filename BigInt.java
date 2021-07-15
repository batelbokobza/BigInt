import java.util.ArrayList;
import java.util.Collections;

/*BigInt class - This class performs arithmetic operations -
 * addition, subtraction, multiplication and division on BigInt numbers.*/
public class BigInt implements Comparable<BigInt> {

    private interface CompareConstants{
        int SMALLER_THAN = -1;
        int EQUALS = 0;
        int LARGER_THAN = 1;
    }
    private final ArrayList<Integer> arrayIntNumber;
    private boolean numberIsNegative = false;     //If the number is negative, the value will be true, otherwise false.

    /*constructor -
     * Receives the user input and generates a large number from it.
     * The constructor will throw an error if the input is incorrect - if the input is blank,
     * or if it does not contain only digits.
     * For a +/- sign at the beginning of the number only you will not throw an error.
     * In addition, leading zeros will be deleted if any.*/
    public BigInt(String strBigInt) {
        if(strBigInt.isEmpty())
            throw new IllegalArgumentException("Invalid input! Please enter a number containing only digits.");
        this.arrayIntNumber = new ArrayList<>();
        this.numberIsNegative = strBigInt.charAt(0) == '-';
        int indInput = strBigInt.charAt(0) == '+' || strBigInt.charAt(0) == '-' ? 1 : 0;
        for (; indInput < strBigInt.length(); indInput++){
            if(Character.isDigit(strBigInt.charAt(indInput)))
                this.arrayIntNumber.add(strBigInt.charAt(indInput) - '0'); //Convert a character in a string to int.
            else
                throw new IllegalArgumentException("Invalid input! Please enter a number containing only digits.");
        }
        deleteExtraZeros(this.arrayIntNumber);
    }

    /*A constructor used by the getBigIntCopy function to copy a bigInt arrayList to a new bitInt.*/
    private BigInt(ArrayList<Integer> numAsArr) {
        this.arrayIntNumber = numAsArr;
    }

    /*The function checks the marks of the two numbers that need to be connected.
     * Depending on the number marks, the appropriate calculation operation will be performed,
     * and the sign changed if necessary.*.
     */
    public BigInt plus(BigInt paramNumber){
        BigInt resultPlus;
        BigInt absOfNegativeNum;
        if(this.numberIsNegative != paramNumber.numberIsNegative) {
            absOfNegativeNum = this.compareTo(paramNumber) == CompareConstants.SMALLER_THAN ? abs(this) : abs(paramNumber);

            if (this.compareTo(absOfNegativeNum) == CompareConstants.EQUALS ||
                    paramNumber.compareTo(absOfNegativeNum) == CompareConstants.EQUALS)
                return new BigInt("0");
            else {
                if (this.compareTo(paramNumber) == CompareConstants.SMALLER_THAN)  //this number is negative and this number by absolute < param
                    resultPlus = paramNumber.compareTo(absOfNegativeNum) == CompareConstants.LARGER_THAN ?
                            minus(paramNumber, absOfNegativeNum) : minus(absOfNegativeNum, paramNumber);
                else  //if ParamNumber is absolute < this number is absolute
                    resultPlus = this.compareTo(absOfNegativeNum) == CompareConstants.LARGER_THAN ?
                            minus(this, absOfNegativeNum) : minus(absOfNegativeNum, this);

                if (this.compareTo(paramNumber) == CompareConstants.SMALLER_THAN &&
                        absOfNegativeNum.compareTo(paramNumber) == CompareConstants.LARGER_THAN)
                    resultPlus.numberIsNegative = true;
                else if (this.compareTo(paramNumber) == CompareConstants.LARGER_THAN &&
                        absOfNegativeNum.compareTo(this) == CompareConstants.LARGER_THAN)
                    resultPlus.numberIsNegative = true;
            }
        }
        else {  //If the signs of the two numbers are the same - if both are negative, change the result sign.
            resultPlus = plus(this, paramNumber);
            resultPlus.numberIsNegative = this.numberIsNegative;
        }
        return resultPlus;
    }

    /*A function that connects two BigInt numbers in the conventional way of connecting vertically.*/
    private BigInt plus(BigInt firstNumber, BigInt secondNumber) {
        int carry = 0;
        int firstNumberSize = firstNumber.arrayIntNumber.size() - 1;
        int secondNumberSize = secondNumber.arrayIntNumber.size() - 1;
        int sizeArrayResult = Math.max(firstNumberSize, secondNumberSize);
        ArrayList<Integer> arrayResult = new ArrayList<>(Collections.nCopies(sizeArrayResult + 1, 0));
        int tempResultPlus, firstNumberCurDigit, secondNumberCurDigit;

        for (; sizeArrayResult >= 0; sizeArrayResult--, firstNumberSize--, secondNumberSize--) {
            firstNumberCurDigit = firstNumberSize >= 0 ? firstNumber.arrayIntNumber.get(firstNumberSize) : 0;
            secondNumberCurDigit = secondNumberSize >= 0 ? secondNumber.arrayIntNumber.get(secondNumberSize) : 0;
            tempResultPlus = firstNumberCurDigit + secondNumberCurDigit + carry;

            carry =  tempResultPlus > 9 ? 1 : 0;
            tempResultPlus = tempResultPlus % 10;

            arrayResult.set(sizeArrayResult, tempResultPlus);
        }
        if (carry != 0)
            arrayResult.add(0, carry);

        return new BigInt(arrayResult);
    }

    /*The function checks the signs of the two numbers that should be missing.
     * Depending on the number sign, the appropriate calculation operation will be performed,
     * and the mark changed if necessary.
     * If the signs of the numbers are different, a connection will be made between them.
     * If the first number is negative, the sign will change.
     * if two numbers is negative When both numbers are negative,
     * compareTo performs a comparison on their absolute value,
     * so when both are negative, the larger number will actually be the smaller one.*/
    public BigInt minus(BigInt paramNumber){
        BigInt resultMinus;

        if(this.numberIsNegative != paramNumber.numberIsNegative) {
            resultMinus = plus(this, paramNumber);
            resultMinus.numberIsNegative = this.numberIsNegative;
        }
        else{   //If the signs of the numbers are not different.
            if(this.compareTo(paramNumber) == CompareConstants.EQUALS)
               return new BigInt("0");
            else if(this.numberIsNegative) {
                if(this.compareTo(paramNumber) == CompareConstants.SMALLER_THAN) {
                    resultMinus = minus(this, paramNumber);
                    resultMinus.numberIsNegative = this.numberIsNegative;
                } else
                    resultMinus = minus(paramNumber, this);
            }
            else { // two numbers is positive
                resultMinus = this.compareTo(paramNumber) == CompareConstants.LARGER_THAN ?
                        minus(this, paramNumber) : minus(paramNumber, this);
                resultMinus.numberIsNegative = this.compareTo(paramNumber) == CompareConstants.SMALLER_THAN;
            }
        }
        return resultMinus;
    }
    /* A function that subtracts two BigInt numbers in the usual way of vertical subtraction. */
    private BigInt minus(BigInt firstNumber, BigInt secondNumber) {
        int firstNumReadInd = firstNumber.arrayIntNumber.size() - 1;
        int secondNumReadInd = secondNumber.arrayIntNumber.size() - 1;
        int resultNumWriteInd = Math.max(firstNumReadInd, secondNumReadInd);
        ArrayList<Integer> subtractResult = new ArrayList<>(Collections.nCopies(resultNumWriteInd + 1, 0));
        int carry = 0;
        for (; resultNumWriteInd >= 0; resultNumWriteInd--, firstNumReadInd--, secondNumReadInd--) {
            int res = 0;
            if (firstNumReadInd >= 0 && secondNumReadInd >= 0)
                res = firstNumber.arrayIntNumber.get(firstNumReadInd) - secondNumber.arrayIntNumber.get(secondNumReadInd) - carry;
            else if (firstNumReadInd >= 0)
                res = firstNumber.arrayIntNumber.get(firstNumReadInd) - carry;
            else if (secondNumReadInd >= 0)
                res = secondNumber.arrayIntNumber.get(secondNumReadInd) - carry;

            carry = res < 0 ? 1 : 0;
            if(res < 0){
                if(resultNumWriteInd != 0)
                    res = res + 10;
            }
            subtractResult.set(resultNumWriteInd, res);
        }
        deleteExtraZeros(subtractResult);
        if (carry != 0)
            subtractResult.add(0, carry);
        return new BigInt(subtractResult);
    }
    /*The function checks the two numbers obtained.
     * If one of them is zero, the result of the product is zero.
     * Otherwise, a multiplication will be performed between the two numbers and change the result sign if necessary.*/
    public BigInt multiply(BigInt paramNumber){
        BigInt resultMultiply;
        BigInt zero = new BigInt("0");
        if(this.compareTo(new BigInt("-0")) == CompareConstants.EQUALS ||
                paramNumber.compareTo(new BigInt("-0")) == CompareConstants.EQUALS ||
                this.compareTo(zero) == CompareConstants.EQUALS || paramNumber.compareTo(zero) == CompareConstants.EQUALS)
           return zero;
        else if(this.arrayIntNumber.size() > paramNumber.arrayIntNumber.size())
            resultMultiply = multiply(this, paramNumber);
        else
            resultMultiply = multiply(paramNumber, this);

        resultMultiply.numberIsNegative = this.numberIsNegative != paramNumber.numberIsNegative;
        return resultMultiply;
    }
    /*Function that multiplies two BigInt numbers in the usual way of multiplication vertically. */
    private BigInt multiply(BigInt multiplied, BigInt multiplier){
        int doubleOfTen = 0, carry = 0 , tempAddZero;
        BigInt arrResult = new BigInt("0");
        BigInt tempArr = new BigInt(new ArrayList<Integer>());
        for(int multiplierSize = multiplier.arrayIntNumber.size()-1; multiplierSize >= 0; carry = 0, multiplierSize--){
            for(int multiplicandSize = multiplied.arrayIntNumber.size()-1; multiplicandSize >= 0; multiplicandSize--){
                int ValDigit = multiplied.arrayIntNumber.get(multiplicandSize) * multiplier.arrayIntNumber.get(multiplierSize) + carry;
                carry = ValDigit / 10;
                tempArr.arrayIntNumber.add(0, ValDigit - carry*10);
                if(multiplicandSize == 0) {
                    if(carry!=0)
                        tempArr.arrayIntNumber.add(0, carry);
                    tempAddZero = doubleOfTen;
                    doubleOfTen +=1;
                    while (tempAddZero > 0){
                        int sizeArrTemp = tempArr.arrayIntNumber.size();
                        tempArr.arrayIntNumber.add(sizeArrTemp, 0);
                        tempAddZero -= 1;
                    }
                    arrResult = plus(arrResult, tempArr);
                    tempArr = tempArr.minus(tempArr);
                }
            }
        }
        return arrResult;
    }
    /*The function checks the two numbers obtained for division.
     * If the number denominator is zero, an error message will be thrown.
     * If the mass is zero, the result of the division is zero.
     * If the parameter on which the method was applied is smaller in absolute value than the calling number
     * as a parameter in absolute value or the two values are equal in absolute value - the result is zero.
     * Otherwise, a call to a function that will make a division, and change the result sign if necessary.*/
    public BigInt divide(BigInt paramNumber){
        BigInt resultDivision;
        BigInt zero = new BigInt("0");
        if (paramNumber.compareTo(zero) == CompareConstants.EQUALS)   // If the denominator is zero - Error.
            throw new ArithmeticException("Cannot divide by zero.");

        if (this.compareTo(zero) == CompareConstants.EQUALS)  // If numerator is zero, return zero
            return zero;

        BigInt dividedByAbs = abs(this);
        BigInt divisorByAbs = abs(paramNumber);
        if (dividedByAbs.compareTo(divisorByAbs) == CompareConstants.EQUALS)  // this by absolute == param by absolute
            resultDivision = new BigInt("1");

         else if (dividedByAbs.compareTo(divisorByAbs) == CompareConstants.SMALLER_THAN)  // this by absolute < param by absolute
            return zero;

         else  // this by absolute > param by absolute
             resultDivision = divide(dividedByAbs, divisorByAbs);

         if(this.numberIsNegative != paramNumber.numberIsNegative && resultDivision.compareTo(zero) != CompareConstants.EQUALS)
            resultDivision.numberIsNegative = true;
            return resultDivision;

    }
    /*The function divides between the two numbers obtained as parameters, according to the known method of long division.*/
    private BigInt divide(BigInt dividend, BigInt divisor){
        BigInt leftDividendDigits = new BigInt("0");
        BigInt result = new BigInt(new ArrayList<Integer>());
        for(int dividendIdx = 0; dividendIdx<=dividend.arrayIntNumber.size(); ++dividendIdx){
            int curResult = 0;
            deleteExtraZeros(leftDividendDigits.arrayIntNumber);
            if(dividendIdx<dividend.arrayIntNumber.size() && divisor.compareTo(leftDividendDigits) == CompareConstants.LARGER_THAN){
                leftDividendDigits.arrayIntNumber.add(dividend.arrayIntNumber.get(dividendIdx));
                result.arrayIntNumber.add(0);
            }
            else{
                BigInt divided = abs(leftDividendDigits);
                for(;divisor.compareTo(divided) != CompareConstants.LARGER_THAN; divided=divided.minus(divisor), curResult++);
                result.arrayIntNumber.add(curResult);
                BigInt multiplied = new BigInt("0");
                for(;curResult>0; curResult--, multiplied=multiplied.plus(divisor));
                BigInt subtracted = minus(leftDividendDigits,multiplied);
                if(dividendIdx<dividend.arrayIntNumber.size())
                    subtracted.arrayIntNumber.add(dividend.arrayIntNumber.get(dividendIdx));
                leftDividendDigits = subtracted;
            }
        }
        deleteExtraZeros(result.arrayIntNumber);
        if(result.arrayIntNumber.size() == 0)
            result.arrayIntNumber.add(0);
        return result;
    }
    /*The function returns the number as a string.*/
    public String toString() {
        StringBuilder strBigNumInt = new StringBuilder(this.numberIsNegative ? "-" : "");
        for (int indDigits = 0; indDigits < this.arrayIntNumber.size(); indDigits++) {
            strBigNumInt.append(this.arrayIntNumber.get(indDigits));
        }
        return strBigNumInt.toString();
    }
    /*The function returns true or false accordingly,
     * if the number on which the method was applied, and the number obtained as a parameter are equal.*/
    public boolean equals(BigInt paramNumber) {
        if(this.numberIsNegative != paramNumber.numberIsNegative)
            return false;
        if (this.arrayIntNumber.size() != paramNumber.arrayIntNumber.size())
            return false;
        for (int indComparison = 0; indComparison < this.arrayIntNumber.size(); indComparison++) {
            if (!this.arrayIntNumber.get(indComparison).equals(paramNumber.arrayIntNumber.get(indComparison)))
                return false;
        }
        return true;
    }

    /*A function that compares two numbers. If the number on which the method was applied is greater than the
     * number obtained as a parameter, 1 is returned, if they are equal, 0. otherwise -1 is returned.
     * If the signs are different - if the first is negative, return -1, otherwise 1.
     * Otherwise - the two signs and size are equal in sign - both positive or negative -
     * If the digit in the first number is larger than the digit in the second number,and both are negative -
     * the first number is smaller.
      * Otherwise - both are positive, and the first number is larger. */
    @Override
    public int compareTo(BigInt bigNumInt) {
        int index = 0;
        if (this.numberIsNegative != bigNumInt.numberIsNegative)
            return this.numberIsNegative ? CompareConstants.SMALLER_THAN : CompareConstants.LARGER_THAN;

        else if (this.arrayIntNumber.size() != bigNumInt.arrayIntNumber.size()) {
            if (this.numberIsNegative)
                return this.arrayIntNumber.size() > bigNumInt.arrayIntNumber.size() ?
                        CompareConstants.SMALLER_THAN : CompareConstants.LARGER_THAN;
            else
                return this.arrayIntNumber.size() < bigNumInt.arrayIntNumber.size() ?
                        CompareConstants.SMALLER_THAN : CompareConstants.LARGER_THAN;
        } else {
            for (; index < this.arrayIntNumber.size(); index++) {
                if (this.arrayIntNumber.get(index) > bigNumInt.arrayIntNumber.get(index)) {
                    return this.numberIsNegative ? CompareConstants.SMALLER_THAN : CompareConstants.LARGER_THAN;
                }
                if (this.arrayIntNumber.get(index) < bigNumInt.arrayIntNumber.get(index))
                    return this.numberIsNegative ? CompareConstants.LARGER_THAN : CompareConstants.SMALLER_THAN;
            }
        }  //Otherwise, the digits are equal between the numbers and also the signs are the same and therefore they are equal.
        return CompareConstants.EQUALS;
    }

    /*The function receives a number, and returns it in absolute value.*/
    public BigInt abs(BigInt number){
        return new BigInt(new ArrayList<>(number.arrayIntNumber));
    }

    /*The function deletes unnecessary zeros from the number if necessary.
     * In arithmetic operations such as connection, unnecessary zeros are sometimes created for proper calculation purposes.
     * This function deletes these unwanted zeros.*/
    private void deleteExtraZeros(ArrayList<Integer> bigNumInt) {
        for(int numSize = bigNumInt.size(); numSize > 1 && bigNumInt.get(0) == 0; numSize--){
            bigNumInt.remove(0);
        }
    }
}


