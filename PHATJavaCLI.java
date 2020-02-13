/*
#-------------------------------------------------------------------------------
# PHAT  - Password Hashing Algorithm Tool
# CLI Java Version
# v 0.4
#
# The purpose of this tool is to let an individual enter text and have a hashed
# output to use as the password to the site or program. Initially the program
# will hash the input in SHA 256 and output in hexadecimal. THe plans for this
# program are to allow the selection of three different SHA lengths (256, 384,
# and 512). Also, the output numbering system will be selectable between
# hexadecimal, base64, and base58. Also, the number of digits in the ouput
# will be selectable in case a site can only have a certain number of digits
# in a password. THe last step will be for the output to be copied to the
# clipboard so if can be pasted into the program or site.
#
# This was compiled with openjdk-13.0.2.
#
# (C) 2020 Lorne Cammack, USA
# email lowcam.socailvideo@gmail.com
# Released under GNU Public License (GPL) v3
#    This program is free software: you can redistribute it and/or modify
#    it under the terms of the GNU General Public License as published by
#    the Free Software Foundation, either version 3 of the License, or
#    (at your option) any later version.
#
#    This program is distributed in the hope that it will be useful,
#    but WITHOUT ANY WARRANTY; without even the implied warranty of
#    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#    GNU General Public License for more details.
#
#    You should have received a copy of the GNU General Public License
#    along with this program.  If not, see <https://www.gnu.org/licenses/>.
#-------------------------------------------------------------------------------
*/


import java.security.MessageDigest;
import java.util.Base64;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Scanner;
import java.awt.datatransfer.StringSelection;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;

public class PHATJavaCLI {

//The following section, until noted, is part of https://www.javatips.net/api/bitcoinj-master/core/src/main/java/org/bitcoinj/core/Base58.java
/**
* Copyright 2011 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

public static final char[] ALPHABET = "123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz".toCharArray();
    private static final char ENCODED_ZERO = ALPHABET[0];
    private static final int[] INDEXES = new int[128];
    static {
        Arrays.fill(INDEXES, -1);
        for (int i = 0; i < ALPHABET.length; i++) {
            INDEXES[ALPHABET[i]] = i;
        }
    }

    /**
     * Encodes the given bytes as a base58 string (no checksum is appended).
     *
     * @param input the bytes to encode
     * @return the base58-encoded string
     */
    public static  String encode58(byte[] input) {
        if (input.length == 0) {
            return "";
        }       
        // Count leading zeros.
        int zeros = 0;
        while (zeros < input.length && input[zeros] == 0) {
            ++zeros;
        }
        // Convert base-256 digits to base-58 digits (plus conversion to ASCII characters)
        input = Arrays.copyOf(input, input.length); // since we modify it in-place
        char[] encoded = new char[input.length * 2]; // upper bound
        int outputStart = encoded.length;
        for (int inputStart = zeros; inputStart < input.length; ) {
            encoded[--outputStart] = ALPHABET[divmod(input, inputStart, 256, 58)];
            if (input[inputStart] == 0) {
                ++inputStart; // optimization - skip leading zeros
            }
        }
        // Preserve exactly as many leading encoded zeros in output as there were leading zeros in input.
        while (outputStart < encoded.length && encoded[outputStart] == ENCODED_ZERO) {
            ++outputStart;
        }
        while (--zeros >= 0) {
            encoded[--outputStart] = ENCODED_ZERO;
        }
        // Return encoded string (including encoded leading zeros).
        return new String(encoded, outputStart, encoded.length - outputStart);
    }

        private static  byte divmod(byte[] number, int firstDigit, int base, int divisor) {
        // this is just long division which accounts for the base of the input digits
        int remainder = 0;
        for (int i = firstDigit; i < number.length; i++) {
            int digit = (int) number[i] & 0xFF;
            int temp = remainder * base + digit;
            number[i] = (byte) (temp / divisor);
            remainder = temp % divisor;
        }
        return (byte) remainder;
    }

//The above section was from https://www.javatips.net/api/bitcoinj-master/core/src/main/java/org/bitcoinj/core/Base58.java
//See note above

   	public static String getHash(byte[] inputBytes, String algorithm, String numSys){
		String hashValue = "";
		try{
			MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
			messageDigest.update(inputBytes);
			byte[] digestedBytes = messageDigest.digest();
			String s = new String(digestedBytes,java.nio.charset.StandardCharsets.ISO_8859_1);
			if (numSys.equals("Base64")){
				hashValue = Base64.getEncoder().encodeToString(s.getBytes(java.nio.charset.StandardCharsets.ISO_8859_1));
			}
			else if (numSys.equals("Hex")){
				hashValue = String.format("%040x", new BigInteger(1, digestedBytes));
			}
			else{
				hashValue = encode58(digestedBytes);
			}
		}
		catch(Exception e){

		}
		return hashValue;

	}

	public static void main(String[] args){
	   System.out.println ("");
	   System.out.println ("PHAT Copyright (C) 2020 Lorne Cammack");
	   System.out.println ("This program comes with ABSOLUTELY NO WARRANTY;");
	   System.out.println ("This is free software, and you are welcome to redistribute it");
	   System.out.println ("under certain conditions. See https://www.gnu.org/licenses/ for more details.");
	   System.out.println ("");
       Scanner input = new Scanner(System.in);
       String hashBitSend = "";
       String numSysIn = "";
       System.out.println("Input Text:");
       String inputText = input.next();
       int loopCounter=0;
       while (loopCounter < 1){
       System.out.println("How Many Bits in SHA? (256, 384, 512)");
       String hashBits = input.next();
       
       if (hashBits.equals("256")){
	    hashBitSend = "SHA-256";
        loopCounter = 1;
       }
       else if (hashBits.equals("384")){
	    hashBitSend = "SHA-384";
        loopCounter = 1;
       }
       else if (hashBits.equals("512")){
	    hashBitSend = "SHA-512";
        loopCounter = 1;
       }
       else{
	System.out.println("This is an incorrect value. Please try again.");
       }
       }
       
       int loopCounter2=0;
       while (loopCounter2<1){
       System.out.println("What number system would you like the output in? (Hex, Base58, Base64)");
       numSysIn = input.next();
       if (numSysIn.equals("Hex")){
	loopCounter2=1;
       }
       else if (numSysIn.equals("Base58")){
	loopCounter2=1;
       }
       else if (numSysIn.equals("Base64")){
	loopCounter2=1;
       }
       else{
	System.out.println("This is an incorrect value. Please try again.");
       }
       }
       String TextOut = getHash(inputText.getBytes(), hashBitSend, numSysIn);
       int loopCounter3=0;
       while (loopCounter3<1){
	System.out.println("Would you like to restrict the number of digits in the output? (y/n)");
        String restrictDig = input.next();
	if (restrictDig.equals("n")){
		loopCounter3=1;
	}
	else if (restrictDig.equals("y")){
		loopCounter3=1;
		int loopCounter4=0;
		while (loopCounter4<1){
			System.out.println("How many digits would you like? (1-128)");
			String numDigs = input.next();
			try{
				int numDigsInt = Integer.parseInt(numDigs);
				if (numDigsInt > 0 && numDigsInt < 129){
					loopCounter4=1;
					if (TextOut.length() > numDigsInt){
						String newTextOut = TextOut.substring(0,numDigsInt);
						TextOut = newTextOut;
					}
					else{}
					
				}
				else{
				System.out.println("This is an incorrect value. Please try again.");
				}

			}
			catch(Exception ee){
				System.out.println("This is an incorrect value. Please try again.");
			}
		}
		
	}
	else{
		System.out.println("This is an incorrect value. Please try again.");
	}
       }
       System.out.println(TextOut);
	   StringSelection clipText = new StringSelection(TextOut);
	   Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
	   clipboard.setContents(clipText, null);
	}

}