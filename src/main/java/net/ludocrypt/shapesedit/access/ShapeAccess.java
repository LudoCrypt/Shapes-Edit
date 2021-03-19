package net.ludocrypt.shapesedit.access;

import com.terraformersmc.terraform.shapes.api.Shape;

import net.minecraft.server.command.ServerCommandSource;

public interface ShapeAccess {

	public Shape getShape();

	public ServerCommandSource setShape(Shape shape);

	public ServerCommandSource resetShape();

	public static Shape getSourceShape(Object source) {
		return ((ShapeAccess) ((ServerCommandSource) source)).getShape();
	}

	public static ServerCommandSource setSourceShape(Object source, Shape shape) {
		return ((ShapeAccess) ((ServerCommandSource) source)).setShape(shape);
	}

	public static ServerCommandSource resetSourceShape(Object source) {
		return ((ShapeAccess) ((ServerCommandSource) source)).resetShape();
	}

}
