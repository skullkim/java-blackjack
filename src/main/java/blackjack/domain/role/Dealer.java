package blackjack.domain.role;

import java.util.function.Supplier;

import blackjack.domain.BlackJack;
import blackjack.domain.card.Hand;

public class Dealer extends Role {

	private static final int CAN_DRAW_STANDARD = 16;
	private static final String DEALER_NAME = "딜러";

	private final Supplier<Boolean> drawable;

	public Dealer(final Hand hand, final Supplier<Boolean> drawable) {
		super(DEALER_NAME, hand);
		this.drawable = drawable;
	}

	@Override
	public boolean canDraw() {
		if (hand.calculateOptimalScore() >= BlackJack.OPTIMIZED_WINNING_NUMBER) {
			return false;
		}
		if (hand.calculateOptimalScore() <= CAN_DRAW_STANDARD) {
			return true;
		}
		if (!hand.hasAce()) {
			return false;
		}
		return drawable.get();
	}

	@Override
	public int getDrawStandard() {
		return CAN_DRAW_STANDARD;
	}
}
