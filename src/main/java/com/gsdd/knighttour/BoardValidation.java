package com.gsdd.knighttour;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class BoardValidation {

	public static boolean isLegalSquare(int xPos, int yPos) {
		return isBoardSquare(xPos) && isBoardSquare(yPos);
	}
	
	private static boolean isBoardSquare(int pos) {
		return pos >= 0 && pos <= 7;
	}
}
