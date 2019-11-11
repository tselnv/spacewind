package com.fluerash.spacewind;

import com.badlogic.gdx.Game;

public class SpacewindGame extends Game {
	private static final String TAG = SpacewindGame.class.getSimpleName();

	private static MainGameScreen mainGameScreen;

	@Override
	public void create() {
		mainGameScreen = new MainGameScreen();
		setScreen(mainGameScreen);
	}

	@Override
	public void dispose() {
		mainGameScreen.dispose();
	}
}
