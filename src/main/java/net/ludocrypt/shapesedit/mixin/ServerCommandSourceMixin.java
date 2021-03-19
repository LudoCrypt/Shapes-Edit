package net.ludocrypt.shapesedit.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import com.terraformersmc.terraform.shapes.api.Shape;
import com.terraformersmc.terraform.shapes.api.validator.Validator;

import net.ludocrypt.shapesedit.ShapesEdit;
import net.ludocrypt.shapesedit.access.ShapeAccess;
import net.ludocrypt.shapesedit.access.ValidatorAccess;
import net.ludocrypt.shapesedit.shape.validator.BoolValidator;
import net.minecraft.server.command.ServerCommandSource;

@Mixin(ServerCommandSource.class)
public abstract class ServerCommandSourceMixin implements ShapeAccess, ValidatorAccess {

	@Unique
	private Shape shape = ShapesEdit.DEFAULT_SHAPE;

	@Unique
	private Validator validator = new BoolValidator(true);

	@Override
	public Shape getShape() {
		return this.shape;
	}

	@Override
	public ServerCommandSource setShape(Shape shape) {
		this.shape = shape;
		return ((ServerCommandSource) (Object) this);
	}

	@Override
	public ServerCommandSource resetShape() {
		this.shape = ShapesEdit.DEFAULT_SHAPE;
		return ((ServerCommandSource) (Object) this);
	}

	@Override
	public Validator getValidator() {
		return this.validator;
	}

	@Override
	public ServerCommandSource setValidator(Validator validator) {
		this.validator = validator;
		return ((ServerCommandSource) (Object) this);
	}

}
