package com.fluerash.spacewind;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.fluerash.spacewind.maps.Map;

public class NPCGraphicsComponent extends GraphicsComponent {


    private static final float NPC_SIZE_Y = 2.0f;
    private static final float NPC_SIZE_X = 2.0f;
    private static final float NPC_SPEED = 4.0f * Entity.FRAME_WIDTH; // unit per second
    private static final float RUNNING_FRAME_DURATION_MOVE = 0.15f*(2.5f * NPC_SIZE_X/NPC_SPEED);

    private Color tintColor;

    public NPCGraphicsComponent(){
        super();
        loadAnimationTable();
        tintColor = new Color(MathUtils.random(0.5f, 1f), MathUtils.random(0.5f, 1f), MathUtils.random(0.5f, 1f), 1 );
    }

    @Override
    public void update(Entity entity, Map map, Batch batch, float delta) {
        updateAnimations(delta);

        batch.setColor(tintColor); // apply color tint effect
        batch.begin();
        batch.draw(currentFrame, currentPosition.x, currentPosition.y, 1, 1);
        batch.end();
        batch.setColor(Color.WHITE);

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


    @Override
    public void dispose() {
    }

    @Override
    public void receiveMessage(MESSAGE messageType, Object... args) {

    }
}
