package com.fluerash.spacewind.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.fluerash.spacewind.SpacewindGame;
import com.fluerash.spacewind.ai_test.PathFindingGame;

public class PathFinderLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 800;
		config.height = 800;
		config.title = "PathFinder :: Space Wind v0.1";
		new LwjglApplication(new PathFindingGame(), config);
	}
}
