package org.jd.cfb;

public interface Board {

	Coin getCoin(int x, int y);

	void setCoin(int x, int y, Coin coin);
}
