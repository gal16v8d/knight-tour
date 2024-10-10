package com.gsdd.knighttour;

import lombok.experimental.UtilityClass;

@UtilityClass
public final class BoardValidation {

	public static boolean isLegalSquare(int xPos, int yPos) {
		return isBoardSquare(xPos) && isBoardSquare(yPos);
	}
	
	private static boolean isBoardSquare(int pos) {
		return pos >= 0 && pos <= 7;
	}
}
