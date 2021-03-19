package net.ludocrypt.shapesedit.command.argument;

import java.util.Collection;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import com.google.common.collect.Sets;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.terraformersmc.terraform.shapes.api.Shape;
import com.terraformersmc.terraform.shapes.impl.Shapes;

import net.ludocrypt.shapesedit.ShapesEdit;
import net.minecraft.command.CommandSource;

public class ShapeArgument implements ArgumentType<Shape> {

	private static final Set<String> SHAPES = Sets.newHashSet("rectangle", "ellipse", "elliptical_prism", "rectangular_prism", "triangular_prism", "rectangular_pyramid", "elliptical_pyramid", "ellipsoid", "hemi_ellipsoid");

	public ShapeArgument() {

	}

	public static ShapeArgument shape() {
		return new ShapeArgument();
	}

	public static Shape getShape(CommandContext<?> context, String name) {
		return context.getArgument(name, Shape.class);
	}

	@Override
	public Shape parse(StringReader reader) throws CommandSyntaxException {

		String string = reader.readUnquotedString();

		switch (string) {
		case "rectangle":
			reader.expect(' ');
			double w1 = reader.readDouble();
			reader.expect(' ');
			double l1 = reader.readDouble();
			return Shapes.rectangle(w1, l1);
		case "ellipse":
			reader.expect(' ');
			double w2 = reader.readDouble();
			reader.expect(' ');
			double l2 = reader.readDouble();
			return Shapes.ellipse(w2, l2);
		case "elliptical_prism":
			reader.expect(' ');
			double w3 = reader.readDouble();
			reader.expect(' ');
			double l3 = reader.readDouble();
			reader.expect(' ');
			double h3 = reader.readDouble();
			return Shapes.ellipticalPrism(w3, l3, h3);
		case "rectangular_prism":
			reader.expect(' ');
			double w4 = reader.readDouble();
			reader.expect(' ');
			double l4 = reader.readDouble();
			reader.expect(' ');
			double h4 = reader.readDouble();
			return Shapes.rectanglarPrism(w4, l4, h4);
		case "triangular_prism":
			reader.expect(' ');
			double w5 = reader.readDouble();
			reader.expect(' ');
			double l5 = reader.readDouble();
			reader.expect(' ');
			double h5 = reader.readDouble();
			return Shapes.triangularPrism(w5, l5, h5);
		case "rectangular_pyramid":
			reader.expect(' ');
			double w6 = reader.readDouble();
			reader.expect(' ');
			double l6 = reader.readDouble();
			reader.expect(' ');
			double h6 = reader.readDouble();
			return Shapes.rectangularPyramid(w6, l6, h6);
		case "elliptical_pyramid":
			reader.expect(' ');
			double w7 = reader.readDouble();
			reader.expect(' ');
			double l7 = reader.readDouble();
			reader.expect(' ');
			double h7 = reader.readDouble();
			return Shapes.ellipticalPyramid(w7, l7, h7);
		case "ellipsoid":
			reader.expect(' ');
			double w8 = reader.readDouble();
			reader.expect(' ');
			double l8 = reader.readDouble();
			reader.expect(' ');
			double h8 = reader.readDouble();
			return Shapes.ellipsoid(w8, l8, h8);
		case "hemi_ellipsoid":
			reader.expect(' ');
			double w9 = reader.readDouble();
			reader.expect(' ');
			double l9 = reader.readDouble();
			reader.expect(' ');
			double h9 = reader.readDouble();
			return Shapes.hemiEllipsoid(w9, l9, h9);
		}

		return ShapesEdit.DEFAULT_SHAPE;
	}

	@Override
	public Collection<String> getExamples() {
		return SHAPES;
	}

	@Override
	public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
		return CommandSource.suggestMatching(SHAPES, builder);
	}

}
