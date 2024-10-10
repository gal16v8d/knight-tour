package com.gsdd.knighttour;

import java.awt.BorderLayout;
import java.io.Serial;
import java.util.Arrays;

import javax.swing.JInternalFrame;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Knight extends JInternalFrame implements Runnable {

	@Serial
	private static final long serialVersionUID = -9190855903626590564L;
	// 8 ways
	public static final int[] OFFSET_X = { 1, 1, 2, 2, -1, -1, -2, -2 };
	public static final int[] OFFSET_Y = { 2, -2, 1, -1, 2, -2, 1, -1 };
	public static final int DIMENTION = 8;
	// start position
	private final int posx;
	private final int posy;
	private final Board display;
	private final int[][] chessBoard;

	public Knight(int x, int y) {
		this.posx = x;
		this.posy = y;
		chessBoard = new int[DIMENTION][DIMENTION];
		display = new Board(chessBoard);
		add(display, BorderLayout.CENTER);
	}
	
	@Override
	public void run() {
		int[] exits = new int[DIMENTION];
		int[] pathX = new int[DIMENTION];
		int[] pathY = new int[DIMENTION];
		int x = posx;
		int y = posy;
		chessBoard[x][y] = 1;
		display.move();
		for (int i = 2; i <= 64; i++) {
			Arrays.fill(exits, 0);
			int count = 0;
			// try on 8 diferent ways
			for (int dir = 0; dir < DIMENTION; dir++) {
				int tmpX = x + OFFSET_X[dir];
				int tmpY = y + OFFSET_Y[dir];

				if (!isLegal(tmpX, tmpY)) {
					continue;
				}
				// legal movements
				if (chessBoard[tmpX][tmpY] == 0) {
					pathX[count] = tmpX;
					pathY[count] = tmpY;
					count++;
				}
			}
			int min = -1;
			// no step found
			if (count == 0) {
				return;
			} else if (count == 1) { // last step
				min = 0;
			} else {
				min = selectNextStep(exits, pathX, pathY, count);
			}
			x = pathX[min];
			y = pathY[min];
			chessBoard[x][y] = i;
			display.move();
			waitForNextMove();
		}
	}

	private int selectNextStep(int[] exits, int[] pathX, int[] pathY, int count) {
		int min;
		// for select our next step
		for (int j = 0; j < count; j++) {
			for (int dir = 0; dir < DIMENTION; dir++) {
				int tmpX = pathX[j] + OFFSET_X[dir];
				int tmpY = pathY[j] + OFFSET_Y[dir];
				if (!isLegal(tmpX, tmpY)) {
					continue;
				}
				if (chessBoard[tmpX][tmpY] == 0) {
					exits[j]++;
				}
			}
		}
		// find the next step
		min = 0;
		int tmp = exits[min];
		for (int j = 1; j < count; j++) {
			if (exits[j] < tmp) {
				tmp = exits[j];
				min = j;
			}
		}
		return min;
	}

	private void waitForNextMove() {
		try {
			// sleeps 0.5 seconds for see what happens
			Thread.sleep(500);
		} catch (InterruptedException ex) {
			log.debug("Thread was interrupted", ex);
			Thread.currentThread().interrupt();
		}
	}

	public boolean isLegal(int moveX, int moveY) {
		return BoardValidation.isLegalSquare(moveX, moveY);
	}
	
}