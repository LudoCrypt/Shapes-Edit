package net.ludocrypt.shapesedit.access;

import com.terraformersmc.terraform.shapes.api.validator.Validator;

import net.minecraft.server.command.ServerCommandSource;

public interface ValidatorAccess {

	public Validator getValidator();

	public ServerCommandSource setValidator(Validator validator);

	public static Validator getValidator(Object source) {
		return ((ValidatorAccess) ((ServerCommandSource) source)).getValidator();
	}

	public static ServerCommandSource setValidator(Object source, Validator validator) {
		return ((ValidatorAccess) ((ServerCommandSource) source)).setValidator(validator);
	}

}
