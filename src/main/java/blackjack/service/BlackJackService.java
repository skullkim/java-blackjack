package blackjack.service;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.stream.Collectors;

import blackjack.domain.role.Dealer;
import blackjack.domain.DealerDrawable;
import blackjack.domain.card.Deck;
import blackjack.domain.card.Hand;
import blackjack.domain.Outcome;
import blackjack.domain.role.Player;
import blackjack.domain.RedrawChoice;
import blackjack.domain.role.Role;
import blackjack.dto.DealerTurnDto;
import blackjack.dto.FinalResultDto;
import blackjack.dto.PlayerStatusDto;
import blackjack.dto.PlayerTurnDto;
import blackjack.dto.TableStatusDto;

public class BlackJackService {

	public static final String BUST_MESSAGE = "파산";
	public static final int BUST = 0;
	public static final int OPTIMIZED_WINNING_NUMBER = 21;

	private List<Role> players;
	private Role dealer;
	private Deck deck;
	private ListIterator<Role> it;
	private Role currentPlayer;

	public void initBlackJack() {
		deck = new Deck();
		dealer = new Dealer(new Hand(), DealerDrawable::chooseDraw);
	}

	public void joinPlayers(final List<String> names) {
		players = names.stream()
			.map(name -> new Player(name, new Hand()))
			.collect(Collectors.toList());
		initPlayerIterator();
	}

	private void initPlayerIterator() {
		it = players.listIterator();
		nextPlayer();
	}

	public TableStatusDto distributeCardToDealer() {
		dealer.draw(deck.draw(), 1);
		final TableStatusDto dealerStatus = TableStatusDto.from(dealer);
		dealer.draw(deck.draw(), 1);
		return dealerStatus;
	}

	public List<TableStatusDto> distributeCardToPlayers() {
		List<TableStatusDto> playerStatuses = new ArrayList<>();
		for (Role player : players) {
			player.draw(deck.draw(), 2);
			playerStatuses.add(TableStatusDto.from(player));
		}
		return playerStatuses;
	}

	public PlayerTurnDto whoseTurn() {
		return PlayerTurnDto.from(currentPlayer);
	}

	public PlayerStatusDto drawPlayer(final RedrawChoice answer) {
		if (answer == RedrawChoice.NO) {
			PlayerStatusDto status = PlayerStatusDto.from(false, hasNextPlayer(), currentPlayer);
			nextPlayer();
			return status;
		}
		currentPlayer.draw(deck.draw(), 1);
		PlayerStatusDto status = PlayerStatusDto.from(currentPlayer.canDraw(), hasNextPlayer(), currentPlayer);
		if (!currentPlayer.canDraw()) {
			nextPlayer();
		}
		return status;
	}

	private boolean hasNextPlayer() {
		return it.hasNext();
	}

	private void nextPlayer() {
		if (hasNextPlayer()) {
			currentPlayer = it.next();
		}
	}

	public DealerTurnDto drawDealer() {
		if (!dealer.canDraw()) {
			return DealerTurnDto.from(dealer, false, Dealer.CAN_NOT_DRAW_STANDARD);
		}
		dealer.draw(deck.draw(), 1);
		return DealerTurnDto.from(dealer, true, Dealer.CAN_DRAW_STANDARD);
	}

	public FinalResultDto calculateFinalResult() {
		for (Role player : players) {
			Outcome outcome = judge(player);
			player.recordCompeteResult(outcome);
			dealer.recordCompeteResult(outcome.getOppositeOutcome());
		}
		return FinalResultDto.from(dealer, players);
	}

	private Outcome judge(Role player) {
		return Outcome.of(player.calculateFinalScore(), dealer.calculateFinalScore());
	}
}
