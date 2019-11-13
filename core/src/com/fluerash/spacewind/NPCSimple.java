package com.fluerash.spacewind;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
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
import com.fluerash.spacewind.maps.Map;

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

    public static enum BoundingBoxLocation{
        BOTTOM_LEFT,
        BOTTOM_CENTER,
        CENTER,
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
    private static final float RUNNING_FRAME_DURATION_MOVE = 15f*(2.5f * NPC_SIZE_X/NPC_SPEED);
    protected static final float VELOCITY_ABS = 2f;

    protected Vector2 nextPosition;
    protected Vector2 currentPosition;
    protected Vector2 velocity;
    protected TextureRegion currentFrame;
    protected Entity.State currentState;
    protected Entity.Direction currentDirection;
    protected Hashtable<Entity.AnimationType, Animation> animations;
    protected float frameTime = 0f;
    protected Rectangle boundingBox;
    protected BoundingBoxLocation boundingBoxLocation;
    private Color tintColor;

    public NPCSimple() {
        currentPosition = new Vector2(MathUtils.random(5, 195),MathUtils.random(5, 195));
        currentState = Entity.State.WALKING;
        currentDirection = Entity.Direction.DOWN;
        nextPosition = currentPosition.cpy();
        velocity = new Vector2(VELOCITY_ABS, VELOCITY_ABS);
        animations = new Hashtable<>();
        loadAnimationTable();
        boundingBoxLocation = BoundingBoxLocation.CENTER;
        boundingBox = new Rectangle();
        initBoundingBox(0.4f, 0.15f);
        tintColor = new Color(MathUtils.random(0.5f, 1f), MathUtils.random(0.5f, 1f), MathUtils.random(0.5f, 1f), 1 );
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

        updateBoundingBoxPosition(nextPosition);
        if(  currentState != Entity.State.IDLE && currentState != Entity.State.IMMOBILE ) {
            if (currentState == Entity.State.WALKING && !isCollisionWithMapLayer(map)) {
                setNextPositionToCurrent();
            }
            calculateNextPosition(delta);
        } else {
            updateBoundingBoxPosition(currentPosition);
        }

        updateAnimations(delta);


        // apply color tint effect
        batch.setColor(tintColor);
        batch.begin();
        batch.draw(currentFrame, currentPosition.x, currentPosition.y, 1, 1);
        batch.end();
        batch.setColor(Color.WHITE);
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
        if (boundingBox.x < 0 || boundingBox.y <0)
            return true;
        if ( boundingBox.x + boundingBox.width > map.getWidthInPixel() || boundingBox.y + boundingBox.height > map.getHeightInPixel())
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
//        if (animations.size() != 0)
//            return;

        TextureAtlas atlas = Utility.getTextureCharactersAtlas();

        Array<TextureRegion> walkRightFrames = new Array<>();
        for (int i=0; i<3; i++) {
            walkRightFrames.add(atlas.findRegion("charset-" + String.format("%04d",i+6)) );
        }

        Animation walkRightAnimation = new Animation(RUNNING_FRAME_DURATION_MOVE, walkRightFrames, Animation.PlayMode.LOOP);
        TextureRegion stayRightFrame  = atlas.findRegion("charset-" + String.format("%04d",7) );

        Array<TextureRegion> walkLeftFrames =new Array<>();
        for (int i=0; i<3; i++) {
            TextureRegion tr = new TextureRegion(walkRightFrames.get(i));
            tr.flip(true, false);
            walkLeftFrames.add(tr);
        }
        Animation walkLeftAnimation = new Animation(RUNNING_FRAME_DURATION_MOVE, walkLeftFrames, Animation.PlayMode.LOOP);
        TextureRegion stayLeftFrame  = atlas.findRegion("charset-" + String.format("%04d",4) );

        Array<TextureRegion> walkUpFrames = new Array<>();
        for (int i=0; i<3; i++) {
            walkUpFrames.add(atlas.findRegion("charset-" + String.format("%04d",i+9)) );
        }
        Animation walkUpAnimation = new Animation(RUNNING_FRAME_DURATION_MOVE, walkUpFrames, Animation.PlayMode.LOOP);
        TextureRegion stayUpFrame  = atlas.findRegion("charset-" + String.format("%04d",10) );

        Array<TextureRegion> walkDownFrames = new Array<>();
        for (int i=0; i<3; i++) {
            walkDownFrames.add(atlas.findRegion("charset-" + String.format("%04d",i+0)) );
        }
        Animation walkDownAnimation = new Animation(RUNNING_FRAME_DURATION_MOVE, walkDownFrames, Animation.PlayMode.LOOP);
        TextureRegion stayDownFrame  = atlas.findRegion("charset-" + String.format("%04d",1) );

        animations.put(Entity.AnimationType.WALK_LEFT, walkLeftAnimation);
        animations.put(Entity.AnimationType.WALK_RIGHT, walkRightAnimation);
        animations.put(Entity.AnimationType.WALK_UP, walkUpAnimation);
        animations.put(Entity.AnimationType.WALK_DOWN, walkDownAnimation);
        animations.put(Entity.AnimationType.IDLE, new Animation(1f,stayDownFrame,true));
    }

    public void dispose() {
    }

    protected void initBoundingBox(float percentageWidthReduced, float percentageHeightReduced){
        //Update the current bounding box
        float width;
        float height;

        float origWidth =  Entity.FRAME_WIDTH;
        float origHeight = Entity.FRAME_HEIGHT;

        float widthReductionAmount = 1.0f - percentageWidthReduced; //.8f for 20% (1 - .20)
        float heightReductionAmount = 1.0f - percentageHeightReduced; //.8f for 20% (1 - .20)

        if( widthReductionAmount > 0 && widthReductionAmount < 1){
            width = Entity.FRAME_WIDTH * widthReductionAmount;
        }else{
            width = Entity.FRAME_WIDTH;
        }

        if( heightReductionAmount > 0 && heightReductionAmount < 1){
            height = Entity.FRAME_HEIGHT * heightReductionAmount;
        }else{
            height = Entity.FRAME_HEIGHT;
        }

        if( width == 0 || height == 0){
            Gdx.app.debug(TAG, "Width and Height are 0!! " + width + ":" + height);
        }

        //Need to account for the unitscale, since the map coordinates will be in pixels
        float minX;
        float minY;

        if( Map.UNIT_SCALE > 0 ) {
            minX = nextPosition.x / Map.UNIT_SCALE;
            minY = nextPosition.y / Map.UNIT_SCALE;
        }else{
            minX = nextPosition.x;
            minY = nextPosition.y;
        }

        boundingBox.setWidth(width);
        boundingBox.setHeight(height);

        switch(boundingBoxLocation){
            case BOTTOM_LEFT:
                boundingBox.set(minX, minY, width, height);
                break;
            case BOTTOM_CENTER:
                boundingBox.setCenter(minX + origWidth/2, minY + origHeight/4);
                break;
            case CENTER:
                boundingBox.setCenter(minX + origWidth/2, minY + origHeight/2);
                break;
        }

        //Gdx.app.debug(TAG, "SETTING Bounding Box for " + entity.getEntityConfig().getEntityID() + ": (" + minX + "," + minY + ")  width: " + width + " height: " + height);
    }

    protected void updateBoundingBoxPosition(Vector2 position){
        float minX;
        float minY;

        if( Map.UNIT_SCALE > 0 ) {
            minX = position.x / Map.UNIT_SCALE;
            minY = position.y / Map.UNIT_SCALE;
        }else{
            minX = position.x;
            minY = position.y;
        }

        switch(boundingBoxLocation){
            case BOTTOM_LEFT:
                boundingBox.set(minX, minY, boundingBox.getWidth(), boundingBox.getHeight());
                break;
            case BOTTOM_CENTER:
                boundingBox.setCenter(minX + Entity.FRAME_WIDTH/2, minY + Entity.FRAME_HEIGHT/4);
                break;
            case CENTER:
                boundingBox.setCenter(minX + Entity.FRAME_WIDTH/2, minY + Entity.FRAME_HEIGHT/2);
                break;
        }

        //Gdx.app.debug(TAG, "SETTING Bounding Box for " + entity.getEntityConfig().getEntityID() + ": (" + minX + "," + minY + ")  width: " + width + " height: " + height);
    }




}
