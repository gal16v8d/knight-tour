package com.gsdd.knighttour;

import lombok.RequiredArgsConstructor;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.io.Serial;
import javax.swing.JPanel;

@RequiredArgsConstructor
public class Board extends JPanel {

	@Serial
	private static final long serialVersionUID = 8441500980328696014L;
	private final int[][] chessBoard;

	public void move() {
		repaint();
	}

	@Override
	public void paint(Graphics g) {
		int width = getWidth();
		int height = getHeight();
		int tam = chessBoard.length;
		int celdatam = Math.min(width, height) / tam;
		int x0 = (width - tam * celdatam) / 2;
		int y0 = (height - tam * celdatam) / 2;
		g.setColor(Color.WHITE);
		g.fillRect(x0, y0, tam * celdatam, tam * celdatam);

		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if ((i + j) % 2 == 0) {
					g.setColor(Color.BLACK);
					int xpos = x0 + j * celdatam;
					int ypos = y0 + i * celdatam;
					g.fillRect(xpos, ypos, celdatam, celdatam);
				}
				if (chessBoard[i][j] != 0) {
					g.setColor(Color.RED);
					g.setFont(new Font("Serif", Font.BOLD, celdatam / 2));
					int xpos = x0 + i * celdatam + celdatam / 2;
					int ypos = y0 + j * celdatam + celdatam / 2;
					g.drawString(Integer.toString(chessBoard[i][j]), ypos, xpos);
				}
			}
		}
	}
}