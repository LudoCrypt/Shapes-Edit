package net.ludocrypt.shapesedit.command.argument;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.google.common.collect.Lists;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import net.minecraft.block.BlockState;
import net.minecraft.command.argument.BlockStateArgumentType;

public class BlockstateListArgument implements ArgumentType<List<BlockState>> {

	public BlockstateListArgument() {

	}

	public static BlockstateListArgument blockList() {
		return new BlockstateListArgument();
	}

	@SuppressWarnings("unchecked")
	public static List<BlockState> getBlockList(CommandContext<?> context, String name) {
		return context.getArgument(name, List.class);
	}

	@Override
	public List<BlockState> parse(StringReader reader) throws CommandSyntaxException {
		ArrayList<BlockState> list = Lists.newArrayList();

		int length = reader.readInt();

		while (length > 0) {
			reader.expect(' ');
			BlockStateArgumentType.blockState().parse(reader);
			length--;
		}

		return list;
	}

	@Override
	public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
		return ArgumentType.super.listSuggestions(context, builder);
	}

}
