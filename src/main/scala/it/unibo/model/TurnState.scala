package it.unibo.model

/** Represents the possible states of a player's turn. */
enum TurnState:

  /** The player must choose an action. */
  case WaitingForAction

  /** An action has been performed and the player may continue the turn. */
  case ActionDone

  /** The turn is complete and can be ended. */
  case TurnComplete
