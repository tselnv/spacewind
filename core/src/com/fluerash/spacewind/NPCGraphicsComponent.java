package com.fluerash.spacewind;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.fluerash.spacewind.maps.Map;

public class NPCGraphicsComponent extends GraphicsComponent {


    private static final float NPC_SIZE_Y = 2.0f;
    private static final float NPC_SIZE_X = 2.0f;
    private static final float NPC_SPEED = 4.0f * Entity.FRAME_WIDTH; // unit per second
    private static final float RUNNING_FRAME_DURATION_MOVE = 0.15f*(2.5f * NPC_SIZE_X/NPC_SPEED);

    public NPCGraphicsComponent(){
        loadAnimationTable();
    }

    @Override
    public void update(Entity entity, Map map, Batch batch, float delta) {
        updateAnimations(delta);

        batch.begin();
        batch.draw(currentFrame, currentPosition.x, currentPosition.y, 1, 1);
        batch.end();

        //Used to graphically debug boundingboxes
        /*
        Rectangle rect = entity.getCurrentBoundingBox();
        Camera camera = mapMgr.getCamera();
        _shapeRenderer.setProjectionMatrix(camera.combined);
        _shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        _shapeRenderer.setColor(Color.BLACK);
        _shapeRenderer.rect(rect.getX() * Map.UNIT_SCALE, rect.getY() * Map.UNIT_SCALE, rect.getWidth() * Map.UNIT_SCALE, rect.getHeight() * Map.UNIT_SCALE);
        _shapeRenderer.end();
        */
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

    @Override
    public void dispose() {
    }

    @Override
    public void receiveMessage(MESSAGE messageType, Object... args) {

    }
}
