/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hedera22.hackathon.smartcontractfx;

/**
 *
 * @author shakir.gusaroff
 */

class ContractIdFormatException extends Exception{
   String str1;
   /* Constructor of custom exception class
    * here I am copying the message that we are passing while
    * throwing the exception to a string and then displaying 
    * that string along with the message.
    */
   ContractIdFormatException (String str2) {
	str1=str2;
   }
   public String toString(){ 
	return ("Failed pre-check with the status `INVALID_CONTRACT_ID`: "+str1) ;
   }
}