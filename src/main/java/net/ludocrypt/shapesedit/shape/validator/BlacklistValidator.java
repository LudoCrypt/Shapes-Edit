package net.ludocrypt.shapesedit.shape.validator;

import java.util.Arrays;
import java.util.List;

import com.terraformersmc.terraform.shapes.api.Position;
import com.terraformersmc.terraform.shapes.api.validator.AllMeetValidator;

import net.minecraft.block.BlockState;
import net.minecraft.world.TestableWorld;

public class BlacklistValidator extends AllMeetValidator {

	private final List<BlockState> blockedStates;
	private final TestableWorld testableWorld;

	public BlacklistValidator(TestableWorld world, List<BlockState> blockedStates) {
		this.blockedStates = blockedStates;
		this.testableWorld = world;
	}

	public BlacklistValidator(TestableWorld world, BlockState... blockedStates) {
		this(world, Arrays.asList(blockedStates));
	}

	public static BlacklistValidator of(TestableWorld world, List<BlockState> blockedStates) {
		return new BlacklistValidator(world, blockedStates);
	}

	public static BlacklistValidator of(TestableWorld world, BlockState... blockedStates) {
		return new BlacklistValidator(world, blockedStates);
	}

	@Override
	public boolean test(Position position) {
		return testableWorld.testBlockState(position.toBlockPos(), (state) -> !blockedStates.contains(state));
	}
}
