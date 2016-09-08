package sk.patkan.fluffy;

import com.badlogic.gdx.Game;

import sk.patkan.fluffy.screens.GameScreen;

public class MainGame extends Game {
	@Override
	public void create () {
		setScreen(new GameScreen(this));
	}
}
