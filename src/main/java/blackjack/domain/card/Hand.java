package blackjack.domain.card;

import java.util.ArrayList;
import java.util.List;

import blackjack.domain.BlackJack;

public class Hand {

	private static final int ACE_AS_ELEVEN = 10;
	private static final int THE_NUMBER_OF_INITIAL_CARD = 2;

	private final List<Card> cards;

	public Hand() {
		this(new ArrayList<>());
	}

	public Hand(final List<Card> cards) {
		this.cards = cards;
	}

	public void addCard(final Card card) {
		cards.add(card);
	}

	public int calculateScore() {
		int totalScore = cards.stream()
			.mapToInt(Card::getScore)
			.sum();
		if (isBust(totalScore)) {
			return BlackJack.BUST_SCORE;
		}
		if (hasAce()) {
			return calculateScoreWithAce(totalScore, totalScore + ACE_AS_ELEVEN);
		}
		return totalScore;
	}

	private boolean isBust(final int totalScore) {
		return totalScore > BlackJack.OPTIMIZED_WINNING_NUMBER;
	}

	public boolean hasAce() {
		return cards.stream()
			.anyMatch(Card::isAce);
	}

	private int calculateScoreWithAce(final int aceAsOneScore, final int aceAsElevenScore) {
		if (isBust(aceAsElevenScore)) {
			return aceAsOneScore;
		}
		return aceAsElevenScore;
	}

	public List<Card> getCards() {
		return new ArrayList<>(cards);
	}

	public boolean isBlackJack() {
		return calculateScore() == BlackJack.OPTIMIZED_WINNING_NUMBER
			&& cards.size() == THE_NUMBER_OF_INITIAL_CARD;
	}
}
