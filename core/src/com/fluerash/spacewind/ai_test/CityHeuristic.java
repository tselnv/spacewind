package com.fluerash.spacewind.ai_test;

import com.badlogic.gdx.ai.pfa.Heuristic;
import com.badlogic.gdx.math.Vector2;

public class CityHeuristic implements Heuristic<City> {

    @Override
    public float estimate(City currentCity, City golaCity) {
        return Vector2.dst( currentCity.x, currentCity.y, golaCity.x, golaCity.y);
    }
}
