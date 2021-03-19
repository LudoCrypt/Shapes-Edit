package net.ludocrypt.shapesedit.command;

import static com.mojang.brigadier.arguments.DoubleArgumentType.doubleArg;
import static com.mojang.brigadier.arguments.DoubleArgumentType.getDouble;
import static net.ludocrypt.shapesedit.command.argument.BlockstateListArgument.blockList;
import static net.ludocrypt.shapesedit.command.argument.BlockstateListArgument.getBlockList;
import static net.ludocrypt.shapesedit.command.argument.DirectionAxisArgument.axis;
import static net.ludocrypt.shapesedit.command.argument.DirectionAxisArgument.getAxis;
import static net.ludocrypt.shapesedit.command.argument.ShapeArgument.getShape;
import static net.ludocrypt.shapesedit.command.argument.ShapeArgument.shape;
import static net.minecraft.command.argument.BlockPosArgumentType.blockPos;
import static net.minecraft.command.argument.BlockPosArgumentType.getLoadedBlockPos;
import static net.minecraft.command.argument.BlockStateArgumentType.blockState;
import static net.minecraft.command.argument.BlockStateArgumentType.getBlockState;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.terraformersmc.terraform.shapes.api.Position;
import com.terraformersmc.terraform.shapes.api.Quaternion;
import com.terraformersmc.terraform.shapes.impl.filler.RandomSimpleFiller;
import com.terraformersmc.terraform.shapes.impl.filler.SimpleFiller;
import com.terraformersmc.terraform.shapes.impl.layer.pathfinder.AddLayer;
import com.terraformersmc.terraform.shapes.impl.layer.pathfinder.ExcludeLayer;
import com.terraformersmc.terraform.shapes.impl.layer.pathfinder.IntersectLayer;
import com.terraformersmc.terraform.shapes.impl.layer.pathfinder.SubtractLayer;
import com.terraformersmc.terraform.shapes.impl.layer.transform.DilateLayer;
import com.terraformersmc.terraform.shapes.impl.layer.transform.NoiseTranslateLayer;
import com.terraformersmc.terraform.shapes.impl.layer.transform.RotateLayer;
import com.terraformersmc.terraform.shapes.impl.layer.transform.TranslateLayer;
import com.terraformersmc.terraform.shapes.impl.validator.AirValidator;
import com.terraformersmc.terraform.shapes.impl.validator.SafelistValidator;

import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.ludocrypt.shapesedit.access.ShapeAccess;
import net.ludocrypt.shapesedit.access.ValidatorAccess;
import net.ludocrypt.shapesedit.shape.filler.ListFiller;
import net.ludocrypt.shapesedit.shape.layer.transform.BendLayer;
import net.ludocrypt.shapesedit.shape.layer.transform.HelixBendLayer;
import net.ludocrypt.shapesedit.shape.validator.BlacklistValidator;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.world.TestableWorld;

public class ShapesEditCommands {

	public static LiteralCommandNode<ServerCommandSource> SHAPE;

	public static LiteralCommandNode<ServerCommandSource> FINISH_SHAPE;

	public static LiteralCommandNode<ServerCommandSource> LAYER;

	public static LiteralCommandNode<ServerCommandSource> PATHFINDER_LAYER;
	public static LiteralCommandNode<ServerCommandSource> TRANSFORM_LAYER;

	public static LiteralCommandNode<ServerCommandSource> ADD_LAYER;
	public static LiteralCommandNode<ServerCommandSource> EXCLUDE_LAYER;
	public static LiteralCommandNode<ServerCommandSource> INTERSECT_LAYER;
	public static LiteralCommandNode<ServerCommandSource> SUBTRACT_LAYER;

	public static LiteralCommandNode<ServerCommandSource> DILATE_LAYER;
	public static LiteralCommandNode<ServerCommandSource> ROTATE_DEGREES_LAYER;
	public static LiteralCommandNode<ServerCommandSource> ROTATE_LAYER;
	public static LiteralCommandNode<ServerCommandSource> BEND_LAYER;
	public static LiteralCommandNode<ServerCommandSource> HELIX_BEND_LAYER;
	public static LiteralCommandNode<ServerCommandSource> TRANSLATE_LAYER;
	public static LiteralCommandNode<ServerCommandSource> NOISE_TRANSLATE_LAYER;

	public static LiteralCommandNode<ServerCommandSource> VALIDATOR;

	public static LiteralCommandNode<ServerCommandSource> AIR_VALIDATOR;
	public static LiteralCommandNode<ServerCommandSource> BLACKLIST_VALIDATOR;
	public static LiteralCommandNode<ServerCommandSource> SAFELIST_VALIDATOR;

	public static LiteralCommandNode<ServerCommandSource> FILLER;

	public static LiteralCommandNode<ServerCommandSource> SIMPLE_FILLER;
	public static LiteralCommandNode<ServerCommandSource> RANDOM_SIMPLE_FILLER;
	public static LiteralCommandNode<ServerCommandSource> LIST_FILLER;

	public static void init() {
		CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
			SHAPE = registerShape(dispatcher);

			ADD_LAYER = registerAddLayer(dispatcher);
			EXCLUDE_LAYER = registerExcludeLayer(dispatcher);
			INTERSECT_LAYER = registerIntersectLayer(dispatcher);
			SUBTRACT_LAYER = registerSubtractLayer(dispatcher);

			DILATE_LAYER = registerDilateLayer(dispatcher);
			ROTATE_DEGREES_LAYER = registerRotateDegreesLayer(dispatcher);
			ROTATE_LAYER = registerRotateLayer(dispatcher);
			BEND_LAYER = registerBendLayer(dispatcher);
			HELIX_BEND_LAYER = registerHelixBendLayer(dispatcher);
			TRANSLATE_LAYER = registerTranslateLayer(dispatcher);
			NOISE_TRANSLATE_LAYER = registerNoiseTranslateLayer(dispatcher);

			PATHFINDER_LAYER = registerPathfinderLayers(dispatcher);
			TRANSFORM_LAYER = registerTransformLayers(dispatcher);

			LAYER = registerLayer(dispatcher);

			SIMPLE_FILLER = registerSimpleFiller(dispatcher);
			RANDOM_SIMPLE_FILLER = registerRandomSimpleFiller(dispatcher);
			LIST_FILLER = registerListFiller(dispatcher);

			FILLER = registerFiller(dispatcher);

			AIR_VALIDATOR = registerAirValidator(dispatcher);
			BLACKLIST_VALIDATOR = registerBlacklistValidator(dispatcher);
			SAFELIST_VALIDATOR = registerSafelistValidator(dispatcher);

			VALIDATOR = registerValidator(dispatcher);

			FINISH_SHAPE = registerFinishShape(dispatcher);
		});
	}

	public static LiteralCommandNode<ServerCommandSource> registerShape(CommandDispatcher<ServerCommandSource> dispatcher) {
		return dispatcher.register(literal("shape").requires((context) -> {
			return context.hasPermissionLevel(2);
		}));
	}

	public static LiteralCommandNode<ServerCommandSource> registerLayer(CommandDispatcher<ServerCommandSource> dispatcher) {
		return dispatcher.register(literal("shape").requires((context) -> {
			return context.hasPermissionLevel(2);
		}).then(literal("layer").redirect(PATHFINDER_LAYER)).then(literal("layer").redirect(TRANSFORM_LAYER)));
	}

	public static LiteralCommandNode<ServerCommandSource> registerPathfinderLayers(CommandDispatcher<ServerCommandSource> dispatcher) {
		return dispatcher.register(literal("shape").requires((context) -> {
			return context.hasPermissionLevel(2);
		}).then(literal("layer").then(literal("add_layer").redirect(ADD_LAYER)).then(literal("exclude_layer").redirect(EXCLUDE_LAYER)).then(literal("intersect_layer").redirect(INTERSECT_LAYER)).then(literal("subtract_layer").redirect(SUBTRACT_LAYER))));
	}

	public static LiteralCommandNode<ServerCommandSource> registerTransformLayers(CommandDispatcher<ServerCommandSource> dispatcher) {
		return dispatcher.register(literal("shape").requires((context) -> {
			return context.hasPermissionLevel(2);
		}).then(literal("layer").then(literal("dilate_layer").redirect(DILATE_LAYER)).then(literal("scale_layer").redirect(DILATE_LAYER)).then(literal("noise_translate_layer").redirect(NOISE_TRANSLATE_LAYER)).then(literal("rotate_layer").redirect(ROTATE_LAYER)).then(literal("translate_layer").redirect(TRANSLATE_LAYER)).then(literal("bend_layer").redirect(BEND_LAYER)).then(literal("helix_bend_layer").redirect(HELIX_BEND_LAYER))));
	}

	public static LiteralCommandNode<ServerCommandSource> registerValidator(CommandDispatcher<ServerCommandSource> dispatcher) {
		return dispatcher.register(literal("shape").requires((context) -> {
			return context.hasPermissionLevel(2);
		}).then(literal("finish").then(literal("validate").then(literal("air").redirect(AIR_VALIDATOR)).then(literal("blacklist").redirect(BLACKLIST_VALIDATOR)).then(literal("safelist").redirect(SAFELIST_VALIDATOR)))));
	}

	public static LiteralCommandNode<ServerCommandSource> registerAirValidator(CommandDispatcher<ServerCommandSource> dispatcher) {
		return dispatcher.register(literal("shape").requires((context) -> {
			return context.hasPermissionLevel(2);
		}).then(literal("finish").then(literal("validate").then(literal("air").redirect(SHAPE, (context) -> {
			return ValidatorAccess.setValidator(context.getSource(), new AirValidator((TestableWorld) ((ServerCommandSource) context.getSource()).getWorld()));
		})))));
	}

	public static LiteralCommandNode<ServerCommandSource> registerBlacklistValidator(CommandDispatcher<ServerCommandSource> dispatcher) {
		return dispatcher.register(literal("shape").requires((context) -> {
			return context.hasPermissionLevel(2);
		}).then(literal("finish").then(literal("validate").then(literal("blacklist").then(argument("list", blockList()).redirect(SHAPE, (context) -> {
			return ValidatorAccess.setValidator(context.getSource(), new BlacklistValidator((TestableWorld) ((ServerCommandSource) context.getSource()).getWorld(), getBlockList(context, "list")));
		}))))));
	}

	public static LiteralCommandNode<ServerCommandSource> registerSafelistValidator(CommandDispatcher<ServerCommandSource> dispatcher) {
		return dispatcher.register(literal("shape").requires((context) -> {
			return context.hasPermissionLevel(2);
		}).then(literal("finish").then(literal("validate").then(literal("safelist").then(argument("list", blockList()).redirect(SHAPE, (context) -> {
			return ValidatorAccess.setValidator(context.getSource(), new SafelistValidator((TestableWorld) ((ServerCommandSource) context.getSource()).getWorld(), getBlockList(context, "list")));
		}))))));
	}

	public static LiteralCommandNode<ServerCommandSource> registerFiller(CommandDispatcher<ServerCommandSource> dispatcher) {
		return dispatcher.register(literal("shape").requires((context) -> {
			return context.hasPermissionLevel(2);
		}).then(literal("finish").then(literal("fill").then(literal("simple").redirect(SIMPLE_FILLER)).then(literal("random_simple").redirect(RANDOM_SIMPLE_FILLER)).then(literal("list").redirect(LIST_FILLER)))));
	}

	public static LiteralCommandNode<ServerCommandSource> registerSimpleFiller(CommandDispatcher<ServerCommandSource> dispatcher) {
		return dispatcher.register(literal("shape").requires((context) -> {
			return context.hasPermissionLevel(2);
		}).then(literal("finish").then(literal("fill").then(literal("simple").then(argument("state", blockState()).executes((context) -> {
			ShapeAccess.getSourceShape(context.getSource()).validate(ValidatorAccess.getValidator(context.getSource()), (validShape) -> {
				validShape.fill(new SimpleFiller(context.getSource().getWorld(), getBlockState(context, "state").getBlockState()));
			});
			return 1;
		}))))));
	}

	public static LiteralCommandNode<ServerCommandSource> registerRandomSimpleFiller(CommandDispatcher<ServerCommandSource> dispatcher) {
		return dispatcher.register(literal("shape").requires((context) -> {
			return context.hasPermissionLevel(2);
		}).then(literal("finish").then(literal("fill").then(literal("random_simple").then(argument("state", blockState()).then(argument("probability", doubleArg()).executes((context) -> {
			ShapeAccess.getSourceShape(context.getSource()).validate(ValidatorAccess.getValidator(context.getSource()), (validShape) -> {
				validShape.fill(new RandomSimpleFiller(context.getSource().getWorld(), getBlockState(context, "state").getBlockState(), context.getSource().getWorld().getRandom(), (float) getDouble(context, "probability")));
			});
			return 1;
		})))))));
	}

	public static LiteralCommandNode<ServerCommandSource> registerListFiller(CommandDispatcher<ServerCommandSource> dispatcher) {
		return dispatcher.register(literal("shape").requires((context) -> {
			return context.hasPermissionLevel(2);
		}).then(literal("finish").then(literal("fill").then(literal("list").then(argument("list", blockList()).executes((context) -> {
			ShapeAccess.getSourceShape(context.getSource()).validate(ValidatorAccess.getValidator(context.getSource()), (validShape) -> {
				validShape.fill(new ListFiller(context.getSource().getWorld(), context.getSource().getWorld().getRandom(), getBlockList(context, "list")));
			});
			return 1;
		}))))));
	}

	public static LiteralCommandNode<ServerCommandSource> registerFinishShape(CommandDispatcher<ServerCommandSource> dispatcher) {
		return dispatcher.register(literal("shape").requires((context) -> {
			return context.hasPermissionLevel(2);
		}).then(literal("finish").redirect(FINISH_SHAPE, (context) -> {
			ShapeAccess.setSourceShape(context.getSource(), ShapeAccess.getSourceShape(context.getSource()));
			return context.getSource();
		})));
	}

	public static LiteralCommandNode<ServerCommandSource> registerAddLayer(CommandDispatcher<ServerCommandSource> dispatcher) {
		return dispatcher.register(literal("shape").requires((context) -> {
			return context.hasPermissionLevel(2);
		}).then(literal("layer").then(literal("add_layer").then(argument("shape", shape()).redirect(SHAPE, (context) -> {
			return ShapeAccess.setSourceShape(context.getSource(), ShapeAccess.getSourceShape(context.getSource()).applyLayer(new AddLayer(getShape(context, "shape"))));
		})))));
	}

	public static LiteralCommandNode<ServerCommandSource> registerExcludeLayer(CommandDispatcher<ServerCommandSource> dispatcher) {
		return dispatcher.register(literal("shape").requires((context) -> {
			return context.hasPermissionLevel(2);
		}).then(literal("layer").then(literal("exclude_layer").then(argument("shape", shape()).redirect(SHAPE, (context) -> {
			return ShapeAccess.setSourceShape(context.getSource(), ShapeAccess.getSourceShape(context.getSource()).applyLayer(new ExcludeLayer(getShape(context, "shape"))));
		})))));
	}

	public static LiteralCommandNode<ServerCommandSource> registerIntersectLayer(CommandDispatcher<ServerCommandSource> dispatcher) {
		return dispatcher.register(literal("shape").requires((context) -> {
			return context.hasPermissionLevel(2);
		}).then(literal("layer").then(literal("intersect_layer").then(argument("shape", shape()).redirect(SHAPE, (context) -> {
			return ShapeAccess.setSourceShape(context.getSource(), ShapeAccess.getSourceShape(context.getSource()).applyLayer(new IntersectLayer(getShape(context, "shape"))));
		})))));
	}

	public static LiteralCommandNode<ServerCommandSource> registerSubtractLayer(CommandDispatcher<ServerCommandSource> dispatcher) {
		return dispatcher.register(literal("shape").requires((context) -> {
			return context.hasPermissionLevel(2);
		}).then(literal("layer").then(literal("subtract_layer").then(argument("shape", shape()).redirect(SHAPE, (context) -> {
			return ShapeAccess.setSourceShape(context.getSource(), ShapeAccess.getSourceShape(context.getSource()).applyLayer(new SubtractLayer(getShape(context, "shape"))));
		})))));
	}

	public static LiteralCommandNode<ServerCommandSource> registerDilateLayer(CommandDispatcher<ServerCommandSource> dispatcher) {
		return dispatcher.register(literal("shape").requires((context) -> {
			return context.hasPermissionLevel(2);
		}).then(literal("layer").then(literal("dilate_layer").then(argument("x", doubleArg()).then(argument("y", doubleArg()).then(argument("z", doubleArg()).redirect(SHAPE, (context) -> {
			return ShapeAccess.setSourceShape(context.getSource(), ShapeAccess.getSourceShape(context.getSource()).applyLayer(new DilateLayer(Position.of(getDouble(context, "x"), getDouble(context, "y"), getDouble(context, "z")))));
		})))))));
	}

	public static LiteralCommandNode<ServerCommandSource> registerRotateDegreesLayer(CommandDispatcher<ServerCommandSource> dispatcher) {
		return dispatcher.register(literal("shape").requires((context) -> {
			return context.hasPermissionLevel(2);
		}).then(literal("layer").then(literal("rotate_degrees_layer").then(argument("x", doubleArg()).then(argument("y", doubleArg()).then(argument("z", doubleArg()).redirect(SHAPE, (context) -> {
			return ShapeAccess.setSourceShape(context.getSource(), ShapeAccess.getSourceShape(context.getSource()).applyLayer(new RotateLayer(Quaternion.of(getDouble(context, "x"), getDouble(context, "y"), getDouble(context, "z"), true))));
		})))))));
	}

	public static LiteralCommandNode<ServerCommandSource> registerRotateLayer(CommandDispatcher<ServerCommandSource> dispatcher) {
		return dispatcher.register(literal("shape").requires((context) -> {
			return context.hasPermissionLevel(2);
		}).then(literal("layer").then(literal("rotate_layer").then(argument("w", doubleArg()).then(argument("i", doubleArg()).then(argument("j", doubleArg()).then(argument("k", doubleArg()).redirect(SHAPE, (context) -> {
			return ShapeAccess.setSourceShape(context.getSource(), ShapeAccess.getSourceShape(context.getSource()).applyLayer(new RotateLayer(Quaternion.of(getDouble(context, "w"), getDouble(context, "i"), getDouble(context, "j"), getDouble(context, "k")))));
		}))))))));
	}

	public static LiteralCommandNode<ServerCommandSource> registerBendLayer(CommandDispatcher<ServerCommandSource> dispatcher) {
		return dispatcher.register(literal("shape").requires((context) -> {
			return context.hasPermissionLevel(2);
		}).then(literal("layer").then(literal("bend_layer").then(argument("arc", doubleArg()).then(argument("height", doubleArg()).then(argument("axis", axis()).redirect(SHAPE, (context) -> {
			return ShapeAccess.setSourceShape(context.getSource(), ShapeAccess.getSourceShape(context.getSource()).applyLayer(new BendLayer(getDouble(context, "arc"), getDouble(context, "height"), getAxis(context, "axis"))));
		})))))));
	}

	public static LiteralCommandNode<ServerCommandSource> registerHelixBendLayer(CommandDispatcher<ServerCommandSource> dispatcher) {
		return dispatcher.register(literal("shape").requires((context) -> {
			return context.hasPermissionLevel(2);
		}).then(literal("layer").then(literal("helix_bend_layer").then(argument("height", doubleArg()).then(argument("radius", doubleArg()).then(argument("axis", axis()).redirect(SHAPE, (context) -> {
			return ShapeAccess.setSourceShape(context.getSource(), ShapeAccess.getSourceShape(context.getSource()).applyLayer(new HelixBendLayer(getDouble(context, "height"), getDouble(context, "radius"), getAxis(context, "axis"))));
		})))))));
	}

	public static LiteralCommandNode<ServerCommandSource> registerTranslateLayer(CommandDispatcher<ServerCommandSource> dispatcher) {
		return dispatcher.register(literal("shape").requires((context) -> {
			return context.hasPermissionLevel(2);
		}).then(literal("layer").then(literal("translate_layer").then(argument("pos", blockPos()).redirect(SHAPE, (context) -> {
			return ShapeAccess.setSourceShape(context.getSource(), ShapeAccess.getSourceShape(context.getSource()).applyLayer(new TranslateLayer(Position.of(getLoadedBlockPos(context, "pos")))));
		})))));
	}

	public static LiteralCommandNode<ServerCommandSource> registerNoiseTranslateLayer(CommandDispatcher<ServerCommandSource> dispatcher) {
		return dispatcher.register(literal("shape").requires((context) -> {
			return context.hasPermissionLevel(2);
		}).then(literal("layer").then(literal("noise_translate_layer").then(argument("magnitude", doubleArg()).redirect(SHAPE, (context) -> {
			return ShapeAccess.setSourceShape(context.getSource(), ShapeAccess.getSourceShape(context.getSource()).applyLayer(new NoiseTranslateLayer(getDouble(context, "magnitude"), ((ServerCommandSource) context.getSource()).getWorld().getRandom())));
		})))));
	}

}
