package game.observer;

public enum EventType {
  PLAYER_CHOICE, // data list = player, card, location, well, pond, next player
  PLAYER_DISCARD, // data list = player, card, well
  CARDS_CLEARED, // data list = cards discarded
  WELL_FLIPPED, // data list = empty
  POND_FLIPPED, // data list = empty
  TURN_OVER, // data list = empty
  ROUND_OVER, // data list = rendState
  GAME_OVER // data list = winner, points
}
