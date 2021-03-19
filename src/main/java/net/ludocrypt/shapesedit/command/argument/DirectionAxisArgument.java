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

import net.minecraft.command.CommandSource;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Direction.Axis;

public class DirectionAxisArgument implements ArgumentType<Direction.Axis> {

	private static final Set<String> AXIS = Sets.newHashSet("X", "Y", "Z");

	public DirectionAxisArgument() {

	}

	public static DirectionAxisArgument axis() {
		return new DirectionAxisArgument();
	}

	public static Direction.Axis getAxis(CommandContext<?> context, String name) {
		return context.getArgument(name, Direction.Axis.class);
	}

	@Override
	public Axis parse(StringReader reader) throws CommandSyntaxException {
		String axisString = reader.readString();
		return Direction.Axis.valueOf(axisString);
	}

	@Override
	public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
		return CommandSource.suggestMatching(AXIS, builder);
	}

	@Override
	public Collection<String> getExamples() {
		return AXIS;
	}

}
