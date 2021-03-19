package net.ludocrypt.shapesedit.shape.validator;

import com.terraformersmc.terraform.shapes.api.Shape;
import com.terraformersmc.terraform.shapes.api.validator.Validator;

public class BoolValidator implements Validator {

	public final boolean generate;

	public BoolValidator(boolean generate) {
		this.generate = generate;
	}

	@Override
	public boolean validate(Shape shape) {
		return generate;
	}

}
