package com.fluerash.spacewind.maps;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.fluerash.spacewind.*;

import java.util.HashMap;
import java.util.Hashtable;

public class NPCSimple {
    private static final String TAG = NPCSimple.class.getSimpleName();

    public static enum Direction {
        UP,
        RIGHT,
        DOWN,
        LEFT;
    }

    public static enum State {
        IDLE,
        WALKING;
    }

    public static enum AnimationType {
        WALK_LEFT,
        WALK_RIGHT,
        WALK_UP,
        WALK_DOWN,
        IDLE,
        IMMOBILE
    }

    protected enum Keys {
        LEFT, RIGHT, UP, DOWN, PAUSE, QUIT
    }

    protected static java.util.Map<Keys, Boolean> keys = new HashMap<Keys,Boolean>();
    //initialize the hashmap for inputs
    static {
        keys.put(Keys.LEFT, false);
        keys.put(Keys.RIGHT, false);
        keys.put(Keys.UP, false);
        keys.put(Keys.DOWN, false);
        keys.put(Keys.QUIT, false);
        keys.put(Keys.PAUSE, false);
    };

    private static final float NPC_SIZE_Y = 1.0f;
    private static final float NPC_SIZE_X = 1.0f;
    private static final float NPC_SPEED = 4.0f * Entity.FRAME_WIDTH; // unit per second
    private static final float RUNNING_FRAME_DURATION_MOVE = 0.15f*(2.5f * NPC_SIZE_X/NPC_SPEED);
    protected static final float VELOCITY_ABS = 2f;

    protected Vector2 nextPosition;
    protected Vector2 currentPosition;
    protected Vector2 velocity;
    protected TextureRegion currentFrame;
    protected Entity.State currentState;
    protected Entity.Direction currentDirection;
    protected Hashtable<Entity.AnimationType, Animation> animations;
    protected float frameTime = 0f;

    public NPCSimple() {
        currentPosition = new Vector2(MathUtils.random(5, 195),MathUtils.random(5, 195));
        currentState = Entity.State.WALKING;
        currentDirection = Entity.Direction.DOWN;
        nextPosition = currentPosition;
        velocity = new Vector2(VELOCITY_ABS, VELOCITY_ABS);
        animations = new Hashtable<>();
        loadAnimationTable();
    }

    public void update(Map map, Batch batch, float delta) {

        if(keys.get(Keys.QUIT)) {
            Gdx.app.exit();
        }

        //If IMMOBILE, don't update anything
        if( currentState != Entity.State.IMMOBILE ) {
            frameTime += delta;

            //Change direction after so many seconds
            if (frameTime > MathUtils.random(1, 5)) {
                currentState = Entity.State.getRandomNext();
                currentDirection = Entity.Direction.getRandomNext();
                frameTime = 0.0f;
            }
        }

        if(  currentState != Entity.State.IDLE && currentState != Entity.State.IMMOBILE ) {

            if (!isCollisionWithMapLayer(map) && currentState == Entity.State.WALKING) {
                setNextPositionToCurrent();
            }
            calculateNextPosition(delta);
        }

        updateAnimations(delta);

        batch.begin();
        batch.draw(currentFrame, currentPosition.x, currentPosition.y, 1, 1);
        batch.end();
    }

    protected void calculateNextPosition(float deltaTime){
        if(currentState == Entity.State.WALKING) {
            if (currentDirection == null) return;

            float testX = currentPosition.x;
            float testY = currentPosition.y;

            velocity.scl(deltaTime);

            switch (currentDirection) {
                case LEFT:
                    testX -= velocity.x;
                    break;
                case RIGHT:
                    testX += velocity.x;
                    break;
                case UP:
                    testY += velocity.y;
                    break;
                case DOWN:
                    testY -= velocity.y;
                    break;
                default:
                    break;
            }

            nextPosition.x = testX;
            nextPosition.y = testY;

            //velocity
            velocity.set(VELOCITY_ABS, VELOCITY_ABS);
        }
    }

    protected void setNextPositionToCurrent(){
        this.currentPosition.x = nextPosition.x;
        this.currentPosition.y = nextPosition.y;
    }

    protected boolean isCollisionWithMapLayer(Map map){
        if (nextPosition.x < 0 || nextPosition.y <0)
            return true;
        if (nextPosition.x > map.getWidth() - NPC_SIZE_X|| nextPosition.y > map.getHeight() - NPC_SIZE_Y)
            return true;
        return false;
    }

    protected void updateAnimations(float delta){
        frameTime = (frameTime + delta)%5; //Want to avoid overflow

        //Look into the appropriate variable when changing position
        switch (currentDirection) {
            case DOWN:
                if (currentState == Entity.State.WALKING) {
                    Animation animation = animations.get(Entity.AnimationType.WALK_DOWN);
                    if( animation == null ) return;
                    currentFrame = (TextureRegion) animation.getKeyFrame(frameTime);
                } else if(currentState == Entity.State.IDLE) {
                    Animation animation = animations.get(Entity.AnimationType.WALK_DOWN);
                    if( animation == null ) return;
                    currentFrame = (TextureRegion) animation.getKeyFrames()[0];
                } else if(currentState == Entity.State.IMMOBILE) {
                    Animation animation = animations.get(Entity.AnimationType.IMMOBILE);
                    if( animation == null ) return;
                    currentFrame = (TextureRegion) animation.getKeyFrame(frameTime);
                }
                break;
            case LEFT:
                if (currentState == Entity.State.WALKING) {
                    Animation animation = animations.get(Entity.AnimationType.WALK_LEFT);
                    if( animation == null ) return;
                    currentFrame = (TextureRegion) animation.getKeyFrame(frameTime);
                } else if(currentState == Entity.State.IDLE) {
                    Animation animation = animations.get(Entity.AnimationType.WALK_LEFT);
                    if( animation == null ) return;
                    currentFrame = (TextureRegion) animation.getKeyFrames()[0];
                } else if(currentState == Entity.State.IMMOBILE) {
                    Animation animation = animations.get(Entity.AnimationType.IMMOBILE);
                    if( animation == null ) return;
                    currentFrame =(TextureRegion) animation.getKeyFrame(frameTime);
                }
                break;
            case UP:
                if (currentState == Entity.State.WALKING) {
                    Animation animation = animations.get(Entity.AnimationType.WALK_UP);
                    if( animation == null ) return;
                    currentFrame =(TextureRegion) animation.getKeyFrame(frameTime);
                } else if(currentState == Entity.State.IDLE) {
                    Animation animation = animations.get(Entity.AnimationType.WALK_UP);
                    if( animation == null ) return;
                    currentFrame =(TextureRegion) animation.getKeyFrames()[0];
                } else if(currentState == Entity.State.IMMOBILE) {
                    Animation animation = animations.get(Entity.AnimationType.IMMOBILE);
                    if( animation == null ) return;
                    currentFrame = (TextureRegion)animation.getKeyFrame(frameTime);
                }
                break;
            case RIGHT:
                if (currentState == Entity.State.WALKING) {
                    Animation animation = animations.get(Entity.AnimationType.WALK_RIGHT);
                    if( animation == null ) return;
                    currentFrame =(TextureRegion) animation.getKeyFrame(frameTime);
                } else if(currentState == Entity.State.IDLE) {
                    Animation animation = animations.get(Entity.AnimationType.WALK_RIGHT);
                    if( animation == null ) return;
                    currentFrame =(TextureRegion) animation.getKeyFrames()[0];
                } else if(currentState == Entity.State.IMMOBILE) {
                    Animation animation = animations.get(Entity.AnimationType.IMMOBILE);
                    if( animation == null ) return;
                    currentFrame =(TextureRegion) animation.getKeyFrame(frameTime);
                }
                break;
            default:
                break;
        }
    }

    //Specific to two frame animations where each frame is stored in a separate texture
    protected Animation loadAnimation(String firstTexture, String secondTexture, Array<GridPoint2> points, float frameDuration){
        Utility.loadTextureAsset(firstTexture);
        Texture texture1 = Utility.getTextureAsset(firstTexture);

        Utility.loadTextureAsset(secondTexture);
        Texture texture2 = Utility.getTextureAsset(secondTexture);

        TextureRegion[][] texture1Frames = TextureRegion.split(texture1, Entity.FRAME_WIDTH, Entity.FRAME_HEIGHT);
        TextureRegion[][] texture2Frames = TextureRegion.split(texture2, Entity.FRAME_WIDTH, Entity.FRAME_HEIGHT);

        Array<TextureRegion> animationKeyFrames = new Array<TextureRegion>(2);

        GridPoint2 point = points.first();

        animationKeyFrames.add(texture1Frames[point.x][point.y]);
        animationKeyFrames.add(texture2Frames[point.x][point.y]);

        return new Animation(frameDuration, animationKeyFrames, Animation.PlayMode.LOOP);
    }

    protected Animation loadAnimation(String textureName, Array<GridPoint2> points, float frameDuration){
        Utility.loadTextureAsset(textureName);
        Texture texture = Utility.getTextureAsset(textureName);

        TextureRegion[][] textureFrames = TextureRegion.split(texture, Entity.FRAME_WIDTH, Entity.FRAME_HEIGHT);

        Array<TextureRegion> animationKeyFrames = new Array<TextureRegion>(points.size);

        for( GridPoint2 point : points){
            animationKeyFrames.add(textureFrames[point.x][point.y]);
        }

        return new Animation(frameDuration, animationKeyFrames, Animation.PlayMode.LOOP);
    }

    private void loadAnimationTable(){
        TextureAtlas atlas = Utility.getTextureCharactersAtlas();

        TextureRegion[] walkRightFrames = new TextureRegion[3];
        for (int i=0; i<3; i++) {
            walkRightFrames[i] = atlas.findRegion("charset-" + String.format("%04d",i+6) );
        }

        Animation walkRightAnimation = new Animation(RUNNING_FRAME_DURATION_MOVE, walkRightFrames);
        TextureRegion stayRightFrame  = atlas.findRegion("charset-" + String.format("%04d",7) );

        TextureRegion[] walkLeftFrames = new TextureRegion[3];
        for (int i=0; i<3; i++) {
            walkLeftFrames[i] =  new TextureRegion(walkRightFrames[i]);
            walkLeftFrames[i].flip(true, false);
        }
        Animation walkLeftAnimation = new Animation(RUNNING_FRAME_DURATION_MOVE, walkLeftFrames);
        TextureRegion stayLeftFrame  = atlas.findRegion("charset-" + String.format("%04d",4) );

        TextureRegion[] walkUpFrames = new TextureRegion[3];
        for (int i=0; i<3; i++) {
            walkUpFrames[i] = atlas.findRegion("charset-" + String.format("%04d",i+9) );
        }
        Animation walkUpAnimation = new Animation(RUNNING_FRAME_DURATION_MOVE, walkUpFrames);
        TextureRegion stayUpFrame  = atlas.findRegion("charset-" + String.format("%04d",10) );

        TextureRegion[] walkDownFrames = new TextureRegion[3];
        for (int i=0; i<3; i++) {
            walkDownFrames[i] = atlas.findRegion("charset-" + String.format("%04d",i+0) );
        }
        Animation walkDownAnimation = new Animation(RUNNING_FRAME_DURATION_MOVE, walkDownFrames);
        TextureRegion stayDownFrame  = atlas.findRegion("charset-" + String.format("%04d",1) );

        animations.put(Entity.AnimationType.WALK_LEFT, walkLeftAnimation);
        animations.put(Entity.AnimationType.WALK_RIGHT, walkRightAnimation);
        animations.put(Entity.AnimationType.WALK_UP, walkUpAnimation);
        animations.put(Entity.AnimationType.WALK_DOWN, walkDownAnimation);
        animations.put(Entity.AnimationType.IDLE, new Animation(1,stayDownFrame,false));

    }

    public void dispose() {
    }

}
