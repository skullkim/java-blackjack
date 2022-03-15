package blackjack.service;

import java.util.List;

import blackjack.domain.RedrawChoice;
import blackjack.domain.card.Deck;
import blackjack.domain.role.Role;

public class BlackJackService {

	public static final String BUST_MESSAGE = "파산";
	public static final int BUST = 0;
	public static final int OPTIMIZED_WINNING_NUMBER = 21;

	private Deck deck;
	private Roles roles;

	public void initBlackJack() {
		roles = new Roles();
		deck = new Deck();
		roles.initDealer();
	}

	public void joinPlayers(final List<String> names) {
		roles.joinPlayers(names);
	}

	public Role distributeCardToDealer() {
		return roles.distributeCardToDealer(deck);
	}

	public List<Role> distributeCardToPlayers() {
		return roles.distributeCardToPlayers(deck);
	}

	public String whoseTurn() {
		return roles.getCurrentPlayerName();
	}

	public Role drawPlayer(final RedrawChoice answer, final String name) {
		return roles.drawPlayer(deck, answer, name);
	}

	public Role drawDealer() {
		return roles.drawDealer(deck);
	}

	public List<Role> calculatePlayerResult() {
		return roles.calculatePlayerResult();
	}

	public Role calculateDealerResult() {
		return roles.calculateDealerResult();
	}
}
