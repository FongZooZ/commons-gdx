package com.gemserk.commons.artemis.systems;

import com.artemis.Entity;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.gemserk.commons.artemis.components.RenderableComponent;
import com.gemserk.commons.artemis.components.SpriteComponent;
import com.gemserk.commons.gdx.camera.Libgdx2dCamera;

public class RenderLayerSpriteBatchImpl implements RenderLayer {

	private static final Class<RenderableComponent> renderableComponentClass = RenderableComponent.class;
	private static final Class<SpriteComponent> spriteComponentClass = SpriteComponent.class;

	private final SpriteBatch spriteBatch;
	private final OrderedByLayerEntities orderedByLayerEntities;
	private final Libgdx2dCamera camera;

	public RenderLayerSpriteBatchImpl(int minLayer, int maxLayer, Libgdx2dCamera camera, SpriteBatch spriteBatch) {
		this.camera = camera;
		this.spriteBatch = spriteBatch;
		this.orderedByLayerEntities = new OrderedByLayerEntities(minLayer, maxLayer);
	}

	public RenderLayerSpriteBatchImpl(int minLayer, int maxLayer, Libgdx2dCamera camera) {
		this(minLayer, maxLayer, camera, new SpriteBatch());
	}

	@Override
	public void init() {

	}

	@Override
	public void dispose() {
		spriteBatch.dispose();
	}

	@Override
	public boolean belongs(Entity entity) {
		RenderableComponent renderableComponent = entity.getComponent(renderableComponentClass);
		return orderedByLayerEntities.belongs(renderableComponent.getLayer());
	}

	@Override
	public void add(Entity entity) {
		orderedByLayerEntities.add(entity);
	}

	@Override
	public void remove(Entity entity) {
		orderedByLayerEntities.remove(entity);
	}

	@Override
	public void render() {
		camera.apply(spriteBatch);
		spriteBatch.begin();
		for (int i = 0; i < orderedByLayerEntities.size(); i++) {
			Entity entity = orderedByLayerEntities.get(i);
			SpriteComponent spriteComponent = entity.getComponent(spriteComponentClass);
			Sprite sprite = spriteComponent.getSprite();
			sprite.setColor(spriteComponent.getColor());
			sprite.draw(spriteBatch);
		}
		spriteBatch.end();
	}

}