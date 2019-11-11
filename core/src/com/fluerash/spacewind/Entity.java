package com.fluerash.spacewind;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.fluerash.spacewind.maps.Map;

import java.nio.file.Watchable;
import java.util.ArrayList;

public class Entity {

    public static enum Direction {
        UP,
        RIGHT,
        DOWN,
        LEFT;

        static public Direction getRandomNext() {
            return Direction.values()[MathUtils.random(Direction.values().length - 1)];
        }

        public Direction getOpposite() {
            if( this == LEFT){
                return RIGHT;
            }else if( this == RIGHT){
                return LEFT;
            }else if( this == UP){
                return DOWN;
            }else{
                return UP;
            }
        }
    }

    public static enum State {
        IDLE,
        WALKING,
        IMMOBILE;//This should always be last

        static public State getRandomNext() {
            //Ignore IMMOBILE which should be last state
            //return State.values()[MathUtils.random(State.values().length - 2)];
            return WALKING;
        }
    }

    public static enum AnimationType {
        WALK_LEFT,
        WALK_RIGHT,
        WALK_UP,
        WALK_DOWN,
        IDLE,
        IMMOBILE
    }

    public static final int FRAME_WIDTH = 32;
    public static final int FRAME_HEIGHT = 32;
    private static final int MAX_COMPONENTS = 5;

    private InputComponent inputComponent;
    private  GraphicsComponent graphicsComponent;
    private PhysicsComponent physicsComponent;
    private ArrayList<Component> components;

    public Entity(InputComponent inputComponent, PhysicsComponent physicsComponent, GraphicsComponent graphicsComponent){
        //entityConfig = new EntityConfig();

        components = new ArrayList<>(MAX_COMPONENTS);

        this.inputComponent = inputComponent;
        this.physicsComponent = physicsComponent;
        this.graphicsComponent = graphicsComponent;

        components.add(inputComponent);
        components.add(physicsComponent);
        components.add(graphicsComponent);
    }

    public void update(Map map, Batch batch, float delta){
        inputComponent.update(this, delta);
        physicsComponent.update(this, map, delta);
        graphicsComponent.update(this, map, batch, delta);
    }

    public void setState(State state) {
        physicsComponent.setState(state);
        graphicsComponent.setState(state);
        inputComponent.setState(state);
    }

    public void setDirection(Direction direction) {
        physicsComponent.setDirection(direction);
        graphicsComponent.setDirection(direction);
        inputComponent.setDirection(direction);
    }

    public void setPosition(Vector2 position){
        physicsComponent.setCurrentPosition(position);
        graphicsComponent.setCurrentPosition(position);
    }

}
