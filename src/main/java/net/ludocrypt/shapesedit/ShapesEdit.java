package net.ludocrypt.shapesedit;

import com.terraformersmc.terraform.shapes.api.Position;
import com.terraformersmc.terraform.shapes.api.Shape;

import net.fabricmc.api.ModInitializer;
import net.ludocrypt.shapesedit.command.ShapesEditCommands;
import net.minecraft.util.Identifier;

public class ShapesEdit implements ModInitializer {

	public static final Shape DEFAULT_SHAPE = Shape.of((point) -> false, Position.of(0, 0, 0), Position.of(0, 0, 0));

	@Override
	public void onInitialize() {
		ShapesEditCommands.init();
	}

	public static Identifier id(String id) {
		return new Identifier("shapesedit", id);
	}

}
